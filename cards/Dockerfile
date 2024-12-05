#Start with a base image containing Java runtime
FROM openjdk:21-jdk-slim

#Information around who maintains the image
LABEL "org.opencontainers.image.authors"="sifeddine.com"

# Install curl
RUN apt-get update && apt-get install -y curl && apt-get clean && rm -rf /var/lib/apt/lists/*

# Add the application's jar to the image
COPY target/cards-0.0.1-SNAPSHOT.jar cards-0.0.1-SNAPSHOT.jar

# execute the application
ENTRYPOINT ["java", "-jar", "cards-0.0.1-SNAPSHOT.jar"]