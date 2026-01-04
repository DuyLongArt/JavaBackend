# Use a lightweight Java runtime instead of full Ubuntu
FROM eclipse-temurin:21-jdk-alpine 

LABEL authors="duylong"

# Set the working directory inside the container
WORKDIR /app

# 1. Copy your compiled jar file from your computer into the container
# Replace 'your-app.jar' with the actual name of your file
COPY build/libs/*.war app.war

# 2. Expose the port your backend runs on (usually 8080)
EXPOSE 8686

# 3. Actually start the Java application
ENTRYPOINT ["java", "-jar", "app.war"]