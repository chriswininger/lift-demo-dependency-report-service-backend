#!/usr/bin/env bash

echo "docker: building the backend api"

docker build -t dependency-report-service-backend:latest .


#./mvnw clean package docker:build
