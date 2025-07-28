#!/bin/bash

# Se placer à la racine du projet
cd "$(dirname "$0")"

# Supprimer tous les dossiers nommés "target" dans l'arborescence
find . -type d -name "target" -exec rm -rf {} +

echo "Tous les répertoires 'target' ont été supprimés."
