# ═══════════════════════════════════════════════════════════════
# ÉTAPE 1 : BUILD — Compiler le code Java avec Maven
# ═══════════════════════════════════════════════════════════════

# Image de base : Maven 3.9 + JDK 17 (Temurin = OpenJDK officiel)
FROM maven:3.9-eclipse-temurin-17 AS build

# Définir le répertoire de travail dans le conteneur
WORKDIR /build

# Copier le POM parent (nécessaire pour le multi-modules Maven)
COPY ../pom.xml /build/pom.xml

# Copier le POM du module resource-service
COPY resource-service/pom.xml /build/resource-service/pom.xml

# Se placer dans le module pour la suite
WORKDIR /build/resource-service

# Télécharger les dépendances Maven (mise en cache Docker)
RUN mvn -B dependency:go-offline

# Copier le code source du module
COPY resource-service/src /build/resource-service/src

# Compiler + packager le JAR (on saute les tests pour accélérer)
RUN mvn -B clean package -DskipTests

# ═══════════════════════════════════════════════════════════════
# ÉTAPE 2 : RUNTIME — Image légère pour exécuter le JAR
# ═══════════════════════════════════════════════════════════════

# Image de base : JRE 17 Alpine (très légère, ~50 Mo)
FROM eclipse-temurin:17-jre-alpine

# Répertoire de travail de l'application
WORKDIR /app

# Créer un utilisateur non-root (bonne pratique de sécurité)
RUN addgroup -S spring && adduser -S spring -G spring

# Basculer sur cet utilisateur non-root
USER spring:spring

# Copier le JAR compilé depuis l'étape "build" vers /app/app.jar
COPY --from=build /build/resource-service/target/resource-service.jar app.jar

# Déclarer le port exposé (documentation, pas de publication réelle)
EXPOSE 8082

# Healthcheck : Docker vérifie périodiquement que le service répond
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8082/actuator/health || exit 1

# Commande de démarrage du conteneur
ENTRYPOINT ["java", "-jar", "app.jar"]