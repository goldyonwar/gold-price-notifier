# Step 1: Build the application
FROM gradle:8.12-jdk17 AS builder
WORKDIR /app

# Copy dependency files first for better caching
COPY gradlew gradlew.bat settings.gradle.kts build.gradle.kts gradle.properties /app/
COPY gradle /app/gradle

# Download dependencies
RUN ./gradlew dependencies --no-daemon || true

# Copy source and build
COPY src /app/src
RUN ./gradlew installDist --no-daemon

# Step 2: Runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app

# Add a non-root user for security (Updated for Debian/Ubuntu)
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser
USER appuser

COPY --from=builder /app/build/install/gold-price-notifier /app/

ENTRYPOINT ["/app/bin/gold-price-notifier"]