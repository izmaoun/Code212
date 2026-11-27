#!/usr/bin/env bash
set -euo pipefail

cd -- "$(dirname -- "${BASH_SOURCE[0]}")/../.."

kube() {
    kubectl --context kind-code212 -n code212 "$@"
}

kube rollout status deployment/config-service --timeout=60s
kube rollout status deployment/discovery-service --timeout=60s

kind load docker-image code212/resource-service:24 --name code212

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

secret=$(kube get secret resource-db-credentials --ignore-not-found -o name)
if [[ -z "$secret" ]]; then
    password_file=$(mktemp)
    trap 'rm -f -- "$password_file"' EXIT
    password=$(openssl rand -hex 24)
    printf '%s' "$password" > "$password_file"
    kube create secret generic resource-db-credentials --from-file="password=$password_file"
fi

kube apply -f deploy/local/resource-db.yaml
kube rollout status deployment/resource-db --timeout=180s
kube apply -f deploy/local/resource-service.yaml
kube rollout status deployment/resource-service --timeout=240s
