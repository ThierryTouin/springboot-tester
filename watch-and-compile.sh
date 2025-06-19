#!/bin/bash

# Modules à compiler
MODULES="springboot-tester-exposition,springboot-tester-domain,springboot-tester-application"
MAIN_MODULE="springboot-tester-exposition"
PROFILE="dev"

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

# PID du process spring-boot:run
SPRING_PID=""

start_spring() {
  echo "[INFO] Lancement de Spring Boot avec le profil '$PROFILE'..."
  mvn spring-boot:run -Dspring-boot.run.profiles="$PROFILE" -pl "$MAIN_MODULE" &
  SPRING_PID=$!
  echo "[INFO] Spring Boot lancé avec le PID $SPRING_PID"
}

stop_spring() {
  if [[ -n "$SPRING_PID" ]] && ps -p $SPRING_PID > /dev/null; then
    echo "[INFO] Arrêt de Spring Boot (PID $SPRING_PID)..."
    kill $SPRING_PID
    wait $SPRING_PID 2>/dev/null
    SPRING_PID=""
  fi
}

check_jars_stable() {
  declare -A hash1 hash2
  stable=1

  for jar in "${JARS[@]}"; do
    [[ -f "$jar" ]] && hash1["$jar"]=$(sha256sum "$jar" | awk '{print $1}') || stable=0
  done

  sleep 2

  for jar in "${JARS[@]}"; do
    [[ -f "$jar" ]] && hash2["$jar"]=$(sha256sum "$jar" | awk '{print $1}') || stable=0
  done

  for jar in "${JARS[@]}"; do
    if [[ "${hash1[$jar]}" != "${hash2[$jar]}" ]]; then
      stable=0
    fi
  done

  return $stable
}

echo "Indexation des fichiers Java..."
find . -name "*.java" > java_files.txt

start_spring

echo "Watching for changes..."
cat java_files.txt | entr -n bash -c '
  echo "[INFO] Modifications détectées. Compilation..."
  mvn install -pl '"$MODULES"' -am || exit 1

  echo "[INFO] Vérification des JARs..."
  while true; do
    '"$(declare -f check_jars_stable)"'
    if check_jars_stable; then
      echo "[INFO] JARs stables, redémarrage..."
      '"$(declare -f stop_spring)"'
      stop_spring
      '"$(declare -f start_spring)"'
      start_spring
      break
    else
      echo "[INFO] En attente de stabilité des JARs..."
      sleep 1
    fi
  done
'
