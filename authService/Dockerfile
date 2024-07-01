FROM openjdk:19-jdk-alpine
ARG JAR_FILE=target/*.jar

COPY target/authService-0.0.1.jar auth-service.jar


# Run the JAR file
ENTRYPOINT ["java", "-jar", "auth-service.jar"]