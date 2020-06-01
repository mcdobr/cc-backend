FROM openjdk:11.0.7-jre-slim-buster
MAINTAINER Mircea Dobreanu

# Create folder and copy
RUN mkdir -p /opt/backend
COPY ./target/backend-*.jar /opt/backend/

CMD java -jar /opt/backend/backend-*.jar
EXPOSE 8080
