#!/bin/bash

CONTAINER_NAME="springboot-tester"    # adapte selon ton container
JAR_PATH_IN_CONTAINER="/app/app.jar"  # chemin du jar dans le container
DEST_PATH_ON_HOST="./app.jar"          # chemin où tu veux copier localement

echo "[INFO] Copie du jar depuis le container '$CONTAINER_NAME'..."

docker cp "$CONTAINER_NAME:$JAR_PATH_IN_CONTAINER" "$DEST_PATH_ON_HOST"

if [[ $? -eq 0 ]]; then
  echo "[SUCCESS] Jar copié vers '$DEST_PATH_ON_HOST'"
else
  echo "[ERROR] Échec de la copie"
fi
