#!/usr/bin/env sh
echo 'Getting project version to tag docker image...'
version=$(mvn --quiet help:evaluate -Dexpression=project.version -DforceStdout)

echo 'Building docker image...'
docker build --tag cc-backend:${version} \
  --tag cc-backend .
