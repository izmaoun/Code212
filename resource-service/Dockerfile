# ═══════════════════════════════════════════════════════════════
# ÉTAPE 1 : BUILD — Compiler le projet avec Maven
# ═══════════════════════════════════════════════════════════════

# Image de base : Maven + JDK 17
FROM maven:3.9-eclipse-temurin-17 AS build

# Répertoire de travail dans le conteneur
WORKDIR /build

# ───────────────────────────────────────────────────────────────
# Copier TOUT le projet (parent + modules)
# ⚠️ Le contexte de build = racine du projet
# ───────────────────────────────────────────────────────────────
COPY . .

# ───────────────────────────────────────────────────────────────
# Builder uniquement le module resource-service
# -pl resource-service  → builder ce module
# -am                   → "also make" : builder aussi ses dépendances (le parent)
# -DskipTests           → ignorer les tests (accélère le build)
# ───────────────────────────────────────────────────────────────
RUN mvn -B -pl resource-service -am clean package -DskipTests

# ═══════════════════════════════════════════════════════════════
# ÉTAPE 2 : RUNTIME — Image légère pour exécuter le JAR
# ═══════════════════════════════════════════════════════════════

# Image JRE 17 Alpine (légère, ~50 Mo)
FROM eclipse-temurin:17-jre-alpine

# Répertoire de travail
WORKDIR /app

# Créer un utilisateur non-root (sécurité)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copier le JAR depuis l'étape de build
COPY --from=build /build/resource-service/target/resource-service.jar app.jar

# Port exposé
EXPOSE 8082

# Healthcheck : Docker vérifie que le service répond
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8082/actuator/health || exit 1

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]