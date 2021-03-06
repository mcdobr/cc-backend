# Build from source
FROM maven:3.6.3-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn --file /home/app/pom.xml clean package

# Create the image
FROM openjdk:11.0.7-jre-slim
RUN mkdir -p /opt/backend
COPY --from=build /home/app/target/backend-*.jar /opt/backend/
# Run the jar
CMD java -jar /opt/backend/backend-*.jar
EXPOSE 8081

# Might have cache issue in layers
MAINTAINER Mircea Dobreanu
