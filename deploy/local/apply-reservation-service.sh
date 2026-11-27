#!/usr/bin/env bash
set -euo pipefail

cd -- "$(dirname -- "${BASH_SOURCE[0]}")/../.."

kube() {
    kubectl --context kind-code212 -n code212 "$@"
}

kube rollout status deployment/config-service --timeout=60s
kube rollout status deployment/discovery-service --timeout=60s
kube rollout status deployment/resource-service --timeout=60s

kind load docker-image code212/reservation-service:24 --name code212

secret=$(kube get secret reservation-db-credentials --ignore-not-found -o name)
if [[ -z "$secret" ]]; then
    password_file=$(mktemp)
    trap 'rm -f -- "$password_file"' EXIT
    password=$(openssl rand -hex 24)
    printf '%s' "$password" > "$password_file"
    kube create secret generic reservation-db-credentials --from-file="password=$password_file"
fi

kube apply -f deploy/local/reservation-db.yaml
kube rollout status deployment/reservation-db --timeout=180s
kube apply -f deploy/local/reservation-service.yaml
kube rollout status deployment/reservation-service --timeout=240s
