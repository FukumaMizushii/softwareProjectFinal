# ================================================================
# Multi-stage Dockerfile for E-Book Management System
# Stage 1: Build the JAR with Maven
# Stage 2: Run with a lean JRE image
# ================================================================

# --- Stage 1: Build ---
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copy pom.xml first and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B -q

# Copy source and build
COPY src ./src
RUN mvn -B -DskipTests clean package -q

# --- Stage 2: Run ---
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create uploads directory for book PDFs and cover images
RUN mkdir -p /app/uploads/books /app/uploads/covers

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/ebook-management-system-1.0.0.jar app.jar

# Railway injects $PORT at runtime; default to 8080
EXPOSE 8080

# IMPORTANT: Use shell form (not exec/JSON form) so ${PORT} is expanded at runtime
ENTRYPOINT ["sh", "-c", "java -Xmx400m -Xms200m -Dserver.port=${PORT:-8080} -jar app.jar"]
