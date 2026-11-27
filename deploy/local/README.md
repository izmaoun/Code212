# Recréer Code212 sur kind

Ce déploiement installe les cinq microservices et les deux bases PostgreSQL dans un namespace du cluster local. Chaque nouvelle base reçoit son propre volume et un mot de passe généré sur place.

## Prérequis

- Docker, kind, kubectl, Bash et OpenSSL.
- Un cluster kind à un nœud nommé `code212`. Sur une nouvelle machine, le créer avec `kind create cluster --name code212`.
- Les cinq images `code212/<service>:24` du build Jenkins, dans Docker ou déjà chargées dans le nœud kind. Le script importe les images manquantes depuis Docker. PostgreSQL utilise l’image `postgres:16-alpine`.

## Installation depuis la racine du dépôt

Pour répéter l’installation avec des bases distinctes de celles du namespace habituel :

```bash
bash deploy/local/bootstrap.sh code212-rehearsal
kubectl --context kind-code212 -n code212-rehearsal get deployments,pvc
kubectl --context kind-code212 -n code212-rehearsal port-forward svc/gateway-service 18888:8888
```

Dans un autre terminal, vérifier les deux routes :

```bash
curl --fail http://localhost:18888/api/resources
curl --fail http://localhost:18888/api/reservations
```

Le namespace par défaut du script est `code212`. Une nouvelle exécution réutilise les secrets et les volumes du namespace choisi. Conserver les secrets avec les sauvegardes des bases. Les applications Argo CD de ce dépôt suivent le namespace habituel `code212`.
