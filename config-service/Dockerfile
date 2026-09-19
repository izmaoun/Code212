FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# ⚠️ SUPPRIMER ces 2 lignes :
# RUN addgroup -S spring && adduser -S spring -G springgit
# USER spring:spring

# Reste identique
COPY --from=build /build/target/config-service.jar app.jar
EXPOSE 9999
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:9999/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]