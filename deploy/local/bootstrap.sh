#!/usr/bin/env bash
# Install the local stack into a namespace of the existing single-node kind cluster.
set -euo pipefail
cd -- "$(dirname -- "${BASH_SOURCE[0]}")"
namespace=${1:-code212}
if [[ ! "$namespace" =~ ^code212(-[a-z0-9]+)*$ || ${#namespace} -gt 63 ]]; then
    echo "Expected a namespace such as code212 or code212-rehearsal" >&2
    exit 1
fi
k() { kubectl --context kind-code212 -n "$namespace" "$@"; }

tmp=$(mktemp -d)
trap 'rm -rf -- "$tmp"' EXIT
for service in config discovery resource reservation gateway; do
    image="code212/${service}-service:24"
    if ! docker exec code212-control-plane crictl inspecti "docker.io/$image" >/dev/null 2>&1; then
        kind load docker-image "$image" --name code212
    fi
done
# Import only the node's platform so a partially cached multi-platform image can be loaded.
if ! docker exec code212-control-plane crictl inspecti docker.io/code212/postgres:16-alpine >/dev/null 2>&1; then
    if ! docker exec code212-control-plane crictl inspecti docker.io/library/postgres:16-alpine >/dev/null 2>&1; then
        platform="linux/$(docker info --format '{{.Architecture}}' | sed 's/x86_64/amd64/; s/aarch64/arm64/')"
        if ! docker image inspect --platform "$platform" postgres:16-alpine >/dev/null 2>&1; then
            docker pull --platform "$platform" postgres:16-alpine
        fi
        docker image save --platform "$platform" postgres:16-alpine \
            | docker exec -i code212-control-plane ctr --namespace k8s.io images import \
                --platform "$platform" --snapshotter overlayfs -
    fi
    docker exec code212-control-plane ctr --namespace k8s.io images tag \
        docker.io/library/postgres:16-alpine docker.io/code212/postgres:16-alpine
fi

k create namespace "$namespace" --dry-run=client -o yaml | k apply -f -
for service in resource reservation; do
    secret="$service-db-credentials"
    existing_secret=$(k get secret "$secret" --ignore-not-found -o name)
    if [[ -z "$existing_secret" ]]; then
        existing_volume=$(k get pvc "$service-db-data" --ignore-not-found -o name)
        if [[ -n "$existing_volume" ]]; then
            echo "Restore $secret for the existing volume before continuing." >&2
            exit 1
        fi
        openssl rand -hex 24 | tr -d '\n' | k create secret generic "$secret" --from-file=password=/dev/stdin
    fi
done

cp -R config-server discovery gateway "$tmp/"
cp resource-db.yaml reservation-db.yaml resource-service.yaml reservation-service.yaml "$tmp/"
# Apply dependencies in order so Spring can fetch its configuration at startup.
for resource in config-server discovery/discovery-service.yaml resource-db.yaml reservation-db.yaml resource-service.yaml reservation-service.yaml gateway/gateway-service.yaml; do
    printf 'apiVersion: kustomize.config.k8s.io/v1beta1\nkind: Kustomization\nnamespace: %s\nresources:\n  - %s\n' "$namespace" "$resource" > "$tmp/kustomization.yaml"
    k apply -k "$tmp"
    deployment=$(basename "$resource" .yaml)
    if [[ "$resource" == config-server ]]; then deployment=config-service; fi
    k rollout status "deployment/$deployment" --timeout=300s
done
