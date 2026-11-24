#!/usr/bin/env bash
set -euo pipefail

cd -- "$(dirname -- "${BASH_SOURCE[0]}")/../.."

kind load docker-image code212/config-service:24 --name code212
kubectl --context kind-code212 create namespace code212 --dry-run=client -o yaml \
    | kubectl --context kind-code212 apply -f -
kubectl --context kind-code212 apply -k deploy/local/config-server
kubectl --context kind-code212 -n code212 rollout status deployment/config-service --timeout=240s
