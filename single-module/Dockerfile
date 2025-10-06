# Étape 1 : build Maven
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests -Pmock


# Étape 2 : image finale
FROM eclipse-temurin:17-jre

# Installation de unzip pour pouvoir inspecter le contenu du JAR
RUN apt-get update && apt-get install -y unzip && rm -rf /var/lib/apt/lists/*


WORKDIR /app

# Copie uniquement le jar de l'exposition
ARG JAR_PATH="./target"
COPY --from=builder /build/${JAR_PATH}/springboot-tester-*.jar app.jar


COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh
ENTRYPOINT ["/app/entrypoint.sh"]