#Building stage
FROM openjdk:17-jdk-slim AS builder
WORKDIR /app

# Copy only the Gradle wrapper and build scripts
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle

# Download dependencies (this will be cached)
RUN ./gradlew --no-daemon build || return 0

#Copy all project files into the container
COPY . .

#Run Gradle to build the project
RUN ./gradlew clean build

FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Install dependencies
RUN apt-get update && apt-get install -y curl \
    && rm -rf /var/lib/apt/lists/*

# Copy the executable JAR file into the container
COPY --from=builder /app/build/libs/TrainerWorkloadService-*.jar app.jar

EXPOSE 8081