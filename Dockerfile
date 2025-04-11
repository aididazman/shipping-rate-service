# ====== Stage 1: Build the application ======
FROM maven:3.9.5-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline

# Copy the source code
COPY src ./src

# Package the application
RUN ./mvnw clean package -DskipTests

# ====== Stage 2: Run the application ======
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=builder /app/target/shipping-rate-service-*.jar shipping-rate-service.jar

# Run the application
ENTRYPOINT ["java", "-jar", "shipping-rate-service.jar"]