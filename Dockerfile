# Etapa 1: Compilar el proyecto
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /DailyFlow-Backend

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecutar la aplicación
FROM eclipse-temurin:21-jdk

WORKDIR /DailyFlow-Backend

COPY --from=build /DailyFlow-Backend/target/backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
