#!/bin/bash

set -eu

cd $(dirname $0)

sbt fullOptJS

if [[ -f lambda.zip ]]; then
  rm lambda.zip
fi

if [[ -d lambda ]]; then
  rm -rf lambda
fi

mkdir lambda
cp target/scala-3.3.1/aws-lambda-scala-js-exercise-opt/* lambda/
cp -r node_modules lambda/
cd lambda
zip -r ../lambda.zip *
