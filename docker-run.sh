#!/usr/bin/env bash

docker run -p 3000:3000 \
  --name  dependency-report-service-backend \
  --rm \
  --net=host \
  dependency-report-service-backend
