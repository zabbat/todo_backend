# Use a small Java 21 base image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the JAR built by Gradle
COPY build/libs/*.jar app.jar

# Expose the port Spring Boot runs on
EXPOSE 8080

# Run the JAR with the given profile
ENTRYPOINT ["java", "-jar", "app.jar"]
