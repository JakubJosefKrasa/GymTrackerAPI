FROM maven:3.8.1-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-jammy
COPY --from=build /app/target/*.jar ./app.jar

COPY src/main/java/com/kuba/GymTrackerAPI/lombok.config /lombok.config
COPY docker-application.yml /docker-application.yml

CMD ["java", "-jar", "-Dspring.config.location=/docker-application.yml", "app.jar"]