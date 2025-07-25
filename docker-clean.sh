#!/bin/bash

APP_NAME="springboot-tester"  # Nom logique du projet

echo "[INFO] Arrêt et suppression du conteneur..."
docker-compose down --remove-orphans

echo "[INFO] Suppression des images construites localement..."
docker images | grep "$APP_NAME" | awk '{ print $3 }' | xargs -r docker rmi -f

echo "[INFO] Nettoyage des volumes non utilisés..."
docker volume prune -f

echo "[INFO] Nettoyage terminé."
