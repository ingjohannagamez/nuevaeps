# Use OpenJDK 17 slim as base image
FROM openjdk:17-slim

# Set the working directory inside the container
WORKDIR /app

# Install necessary packages
RUN apt-get update && apt-get install -y dos2unix

# Copy Maven wrapper and Pom.xml, converting line endings for Unix compatibility
COPY mvnw pom.xml ./
RUN dos2unix mvnw && chmod +x mvnw

# Copy Maven settings
COPY .mvn .mvn

# Copy the project files
COPY src src

# Package the application
RUN ./mvnw package -DskipTests

# Run the application
ENTRYPOINT ["java", "-jar", "target/logistica-0.0.1-SNAPSHOT.jar"]
