# --- STAGE 1: Build the application ---
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy gradle files first to leverage Docker caching
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Fix permissions and build the WAR file
RUN chmod +x ./gradlew
RUN ./gradlew bootWar -x test

# --- STAGE 2: Run the application ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy only the compiled WAR from the build stage
COPY --from=build /app/build/libs/*.war app.war

EXPOSE 8686

ENTRYPOINT ["java", "-jar", "app.war"]