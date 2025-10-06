#!/bin/bash

# Liste des JARs produits (à adapter si besoin)
# Variables Maven (adapte selon ton projet)
GROUP_PATH="com/touin/thierry/sb/test"  # groupId 'com.example' devient 'com/example'
VERSION="0.0.1-SNAPSHOT"

# Liste des JARs installés dans ~/.m2/repository
JARS=(
  "$HOME/.m2/repository/$GROUP_PATH/springboot-tester-exposition/$VERSION/springboot-tester-exposition-$VERSION.jar"
  "$HOME/.m2/repository/$GROUP_PATH/springboot-tester-domain/$VERSION/springboot-tester-domain-$VERSION.jar"
  "$HOME/.m2/repository/$GROUP_PATH/springboot-tester-application/$VERSION/springboot-tester-application-$VERSION.jar"
)


# Log de tous les jars surveillés
echo "Liste des JARs surveillés dans le dépôt Maven local:"
for jar in "${JARS[@]}"; do
  echo " - $jar"
done

echo "Indexation des fichiers Java..."
find . -name "*.java" > java_files.txt

echo "Watching for changes..."
cat java_files.txt | entr -r bash -c '
  echo "[INFO] Compilation Maven en cours..."
  mvn install -pl springboot-tester-exposition,springboot-tester-domain,springboot-tester-application -am || exit 1

  echo "[INFO] Vérification des JARs générés..."
  
  stable=0
  while [[ $stable -eq 0 ]]; do
    declare -A hash1 hash2
    stable=1

    # Premier hash
    for jar in "${JARS[@]}"; do
      [[ -f "$jar" ]] && hash1["$jar"]=$(sha256sum "$jar" | awk '\''{print $1}'\'') || stable=0
    done

    sleep 2

    # Second hash
    for jar in "${JARS[@]}"; do
      [[ -f "$jar" ]] && hash2["$jar"]=$(sha256sum "$jar" | awk '\''{print $1}'\'') || stable=0
    done

    # Comparaison
    for jar in "${JARS[@]}"; do
      if [[ "${hash1[$jar]}" != "${hash2[$jar]}" ]]; then
        echo "[WARN] Fichier instable : $jar"
        stable=0
      fi
    done
  done

  echo "[INFO] Tous les JARs sont stables. Déclenchement du redémarrage..."
  mkdir -p ./restarter/trigger
  touch ./restarter/trigger/.trigger.restart
'
