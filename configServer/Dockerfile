# Start with a base image containing Java runtime
FROM openjdk:21-jdk-slim

# Information about who maintains the image
LABEL "org.opencontainers.image.authors"="sifeddine.com"

# Install curl
RUN apt-get update && apt-get install -y curl && apt-get clean && rm -rf /var/lib/apt/lists/*

# Add the application's jar to the image
COPY target/configServer-0.0.1-SNAPSHOT.jar configServer-0.0.1-SNAPSHOT.jar

# Execute the application
ENTRYPOINT ["java", "-jar", "configServer-0.0.1-SNAPSHOT.jar"]
