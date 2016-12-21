#!/bin/sh

TAG=leansys/jdk8-busybox

echo "Building..."
docker build -t $TAG .

echo "To test:"
docker run --rm $TAG java -version