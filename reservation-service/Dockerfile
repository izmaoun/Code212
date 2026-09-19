FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build

# Copier UNIQUEMENT le pom du module
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copier le code source
COPY src ./src

# Builder le module (PAS de -pl)
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=build /build/target/reservation-service.jar app.jar
EXPOSE 8083
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8083/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]