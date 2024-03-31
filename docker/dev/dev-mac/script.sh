#!/bin/bash

docker-compose stop
docker-compose rm -f
docker rmi sjh9708/didacto-app-dev:latest
docker-compose up -d
