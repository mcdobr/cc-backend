FROM openjdk:11.0.7-jre-slim-buster
MAINTAINER Mircea Dobreanu

# Create folder and copy
RUN mkdir -p /opt/backend
ARG JAR_FILE
COPY ./target/${JAR_FILE} /opt/backend/
ENV JAR_PATH="/opt/backend/${JAR_FILE}"

ARG MONGO_CLUSTER_URI
ENV MONGO_CLUSTER_URI=${MONGO_CLUSTER_URI}

CMD java -jar $JAR_PATH
EXPOSE 8080
