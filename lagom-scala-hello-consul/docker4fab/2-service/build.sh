#!/bin/bash

# args: service_name
function build() {
  echo "Building $1 ..."

  FILE=$1-impl-1.0-SNAPSHOT.tgz

  rm -f $FILE

  if [ ! -f "../../$1-impl/target/universal/$FILE" ]; then
    echo "$FILE not found. Let's build it..."
    pushd .
    cd  ../..
    sbt clean universal:packageZipTarball
    popd
  fi

  cp ../../$1-impl/target/universal/$FILE .

  docker build -t training/$1 --build-arg SVC_NAME=$1 .
}

build 'helloservice'
build 'greetingservice'
