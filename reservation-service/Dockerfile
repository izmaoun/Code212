# ═══════════════════════════════════════════════════════════════
# ÉTAPE 1 : BUILD
# ═══════════════════════════════════════════════════════════════

FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /build

# Copier TOUT le projet
COPY . .

# Builder uniquement le module reservation-service
RUN mvn -B -pl reservation-service -am clean package -DskipTests

# ═══════════════════════════════════════════════════════════════
# ÉTAPE 2 : RUNTIME
# ═══════════════════════════════════════════════════════════════

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /build/reservation-service/target/reservation-service.jar app.jar

EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8083/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]