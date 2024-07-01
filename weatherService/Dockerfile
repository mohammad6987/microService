FROM openjdk:19-jdk-alpine
ARG JAR_FILE=target/*.jar

COPY target/weatherService-0.0.1.jar weather-service.jar


# Run the JAR file
ENTRYPOINT ["java", "-jar", "weather-service.jar"]