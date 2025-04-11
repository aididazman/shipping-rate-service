FROM eclipse-temurin:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the jar file
COPY target/shipping-rate-service-*.jar shipping-rate-service.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "shipping-rate-service.jar"]