# ═══════════════════════════════════════════════════════════════
# ÉTAPE 1 : BUILD — Compiler le code Java avec Maven
# ═══════════════════════════════════════════════════════════════

# Image de base : Maven 3.9 + JDK 17
FROM maven:3.9-eclipse-temurin-17 AS build

# Répertoire de travail dans le conteneur
WORKDIR /build

# Copier le POM parent (nécessaire pour le multi-modules)
COPY ../pom.xml /build/pom.xml

# Copier le POM du module reservation-service
COPY reservation-service/pom.xml /build/reservation-service/pom.xml

# Se placer dans le module
WORKDIR /build/reservation-service

# Télécharger les dépendances Maven (cache Docker)
RUN mvn -B dependency:go-offline

# Copier le code source
COPY reservation-service/src /build/reservation-service/src

# Compiler + packager (sans tests)
RUN mvn -B clean package -DskipTests

# ═══════════════════════════════════════════════════════════════
# ÉTAPE 2 : RUNTIME — Image légère pour exécuter le JAR
# ═══════════════════════════════════════════════════════════════

# Image de base : JRE 17 Alpine
FROM eclipse-temurin:17-jre-alpine

# Répertoire de travail
WORKDIR /app

# Utilisateur non-root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copier le JAR compilé
COPY --from=build /build/reservation-service/target/reservation-service.jar app.jar

# Port du microservice
EXPOSE 8081

# Healthcheck
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8083/actuator/health || exit 1

# Démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]