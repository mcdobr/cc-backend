#!/usr/bin/env sh

echo 'Creating jar artifact...'
mvn clean package

echo 'Getting project version to tag docker image...'
version=$(mvn --quiet help:evaluate -Dexpression=project.version -DforceStdout)

echo 'Building docker image...'
docker build --tag cc-backend:${version} \
  --build-arg JAR_FILE=backend-${version}.jar \
  --build-arg MONGO_CLUSTER_URI=${MONGO_CLUSTER_URI} .
#docker run -p 8080:8080 -t cc-backend:${version}
