#!/bin/bash

# Script à lancer dans le conteneur Docker (ou à l'extérieur si tu compiles localement)
# Il observe les fichiers .java et relance la compilation Maven dès qu’un changement est détecté

echo "Indexation des fichiers Java..."
find . -name "*.java" > java_files.txt

echo "Watching for changes..."
cat java_files.txt | entr -r sh -c 'mvn clean install -pl springboot-tester-exposition,springboot-tester-domain,springboot-tester-application -am && touch ./restarter/trigger/.trigger.restart'
