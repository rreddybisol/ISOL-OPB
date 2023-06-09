#!/usr/bin/env bash

set -e

# These are production secrets
echo "$GPG_SECRET_KEY" | base64 --decode | $GPG_EXECUTABLE --import --no-tty --batch --yes || true
echo "$GPG_OWNERTRUST" | base64 --decode | $GPG_EXECUTABLE --import-ownertrust --no-tty --batch --yes || true

./mvnw --no-transfer-progress --settings scripts/mvn-release-settings.xml deploy -Prelease-candidate -DskipTests -B -U || exit 1
