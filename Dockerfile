# Etapa 1: Compilarea proiectului folosind Gradle
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
# Limităm memoria la 256MB ca să nu ne dea Render afară!
RUN gradle clean build -x test --no-daemon -Dorg.gradle.jvmargs="-Xmx256m"

# Etapa 2: Rularea aplicației
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]