# Étape 1 : build Maven
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests -Pmock


# Étape 2 : image finale
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copie uniquement le jar de l'exposition
ARG JAR_PATH="springboot-tester-exposition/target"
COPY --from=builder /build/${JAR_PATH}/springboot-tester-exposition-*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
