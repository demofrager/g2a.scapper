#!/bin/bash

BUILD_DIR=$PWD/build/install
PROJECT=G2A
DEPLOY=$BUILD_DIR/$PROJECT

# Clear previous builds files
./gradlew clean

# Install a distribution for deplayment in java
./gradlew installDist

echo "Copying products to: $DEPLOY"
cp products.db $DEPLOY

echo "Copying configs to: $DEPLOY"
cp -r config $DEPLOY

echo "Copying libs to: $DEPLOY"
cp -r libs $DEPLOY

echo "Copying cronjob to: $DEPLOY"
cp -r g2a-cron $DEPLOY

echo "Copying Dockefile to: $DEPLOY"
cp -r Dockefile $DEPLOY

# Copy to Raspberry
# scp -r "$DEPLOY" pi@192.168.1.2:g2aScrapper/
