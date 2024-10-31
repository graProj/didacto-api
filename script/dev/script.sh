#!/bin/bash

# Spring 프로젝트를 JAR 파일로 빌드하고 Dockerhub에 올리는 작업 자동화 스크립트

# Spring 프로젝트 루트 디렉토리 경로 설정
SPRING_PROJECT_DIR="../../"

# Spring 프로젝트 디렉토리로 이동
cd "$SPRING_PROJECT_DIR" || exit

# Gradle Wrapper를 사용하여 빌드 실행
./gradlew build

docker buildx build --platform=linux/amd64,linux/arm64 -t sjh9708/didacto-app-dev . --push
