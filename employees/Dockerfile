# --------------------------
# Build Stage
# --------------------------
FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the application and skip tests
RUN mvn clean package -DskipTests

# --------------------------
# Run Stage
# --------------------------
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/employees-0.0.1-SNAPSHOT.jar app.jar

# Expose application port
EXPOSE 8000

# Run the Spring Boot application
ENTRYPOINT ["java","-jar","app.jar"]
