# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar file into the container
COPY target/library-management-system-0.0.1-SNAPSHOT.jar /app/library-management-system.jar

# Expose the port that the application runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/library-management-system.jar"]
