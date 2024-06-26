FROM openjdk:19-jdk-alpaine
ARG JAR_FILE=target/*.jar

COPY target/country-weather-service-0.0.1-SNAPSHOT.jar /app/country-weather-service.jar


# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/country-weather-service.jar"]