#!/bin/bash

echo "[INFO] Nettoyage des anciennes images..."
docker compose down

echo "[INFO] Construction et démarrage du conteneur..."
docker compose up --build
