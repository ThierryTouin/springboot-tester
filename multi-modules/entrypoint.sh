#!/bin/bash
echo "Contenu du MANIFEST.MF dans app.jar :"
unzip -p app.jar META-INF/MANIFEST.MF
echo "----"
echo "Fichiers contenus dans app.jar :"
unzip -l app.jar | head -n 50
echo "----"

# Démarrage de l'application
exec java -jar app.jar
