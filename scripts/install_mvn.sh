#!/usr/bin/env bash

./mvnw install --no-transfer-progress -DskipTests=true -Dmaven.javadoc.skip=true -B -V || exit 1
