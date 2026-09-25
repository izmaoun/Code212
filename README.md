# Code212 - Resource Reservation System

Plateforme microservices de gestion des ressources du centre CODE 212.

## Architecture

- **7 microservices** : config, discovery, gateway, resource, reservation, keycloak, 3 PostgreSQL
- **Spring Cloud** : Config Server, Eureka, Gateway
- **Sécurité** : OAuth2/JWT avec Keycloak
- **DevOps** : Jenkins, Kubernetes, Prometheus, Grafana

## Démarrage rapide

```bash
docker compose up -d


---

## 🎯 Étape 4 : Initialiser Git

```bash
cd ~/Desktop/Code212

# Configurer Git
git config user.name "Amine"
git config user.email "amine@code212.ma"

# Initialiser
git init
git add .
git commit -m "Initial commit - Microservices plateforme Code212"

# Vérifier
git log --oneline# Test Fri Sep 25 07:26:48 PM +01 2026
