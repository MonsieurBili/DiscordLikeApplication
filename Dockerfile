# Etapa 1: Compilarea proiectului folosind Gradle
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
# Rulăm build-ul și sărim peste teste pentru a face deploy-ul mai rapid
RUN gradle clean build -x test --no-daemon

# Etapa 2: Rularea aplicației
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
# Aici este diferența majoră: Gradle pune fișierul jar în build/libs/
COPY --from=build /app/build/libs/*.jar app.jar

# Expunem portul backend-ului tău
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]