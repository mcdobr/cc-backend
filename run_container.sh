#!/bin/zsh

source /home/mircea/.zshrc

if [ -z "${CC_DATABASE_URL}" ]; then
	echo "Missing database connection url. Aborting..."
	exit 1
fi

if [ -z "${CC_OAUTH2_CLIENT_ID}" ]; then
	echo "Missing OAuth2 client id. Aborting..."
	exit 1
fi

if [ -z "${CC_OAUTH2_CLIENT_SECRET}" ]; then
	echo "Missing OAuth2 client secret. Aborting..."
	exit 1
fi

if [ -z "${CC_CORS_ALLOWED_ORIGINS}" ]; then
	echo "Missing CORS allowed origins. Aborting..."
	exit 1
fi

docker run -p 8080:8080 \
	--env CC_DATABASE_URL=${CC_DATABASE_URL} \
	--env CC_OAUTH2_CLIENT_ID=${CC_OAUTH2_CLIENT_ID} \
	--env CC_OAUTH2_CLIENT_SECRET=${CC_OAUTH2_CLIENT_SECRET} \
	--env CC_CORS_ALLOWED_ORIGINS=${CC_CORS_ALLOWED_ORIGINS} \
	--tty cc-backend
