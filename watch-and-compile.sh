#!/bin/bash

# === CONFIGURATION ===
MODULES="springboot-tester-exposition,springboot-tester-domain,springboot-tester-application"
MAIN_MODULE="springboot-tester-exposition"
PROFILE="dev"
JAVA_FILES_LIST="restarter/java_files.txt"

# === VARIABLES ===
SPRING_PID=""

# === FONCTIONS ===

start_spring() {
  echo "[DEBUG] start_spring() Lancement de Spring Boot avec le profil '$PROFILE'..."
  mvn spring-boot:run -Dspring-boot.run.profiles="$PROFILE" -pl "$MAIN_MODULE" > restarter/spring.log 2>&1 &

  echo "[INFO] Attente du démarrage du processus Java Spring Boot..."

  for i in {1..20}; do
    SPRING_PID=$(ps aux | grep '[j]ava' | grep -- "-cp" | grep -F "springboot-tester-exposition" | awk '{print $2}' | head -n1)
    echo "[DEBUG] PID trouvé ? $SPRING_PID"
    if [[ -n "$SPRING_PID" ]]; then
      echo "[INFO] Spring Boot lancé avec le PID $SPRING_PID"
      return
    fi
    sleep 1
  done

  echo "[ERREUR] Impossible de trouver le processus Java Spring Boot"
}



stop_spring() {
  echo "[DEBUG] stop_spring() with $SPRING_PID"
  if [[ -n "$SPRING_PID" ]] && ps -p "$SPRING_PID" > /dev/null; then
    echo "[INFO] Arrêt de Spring Boot (PID $SPRING_PID)..."
    kill -9 "$SPRING_PID"
    wait "$SPRING_PID" 2>/dev/null || true
    SPRING_PID=""
  else
    echo "[WARN] Aucun PID actif trouvé pour Spring Boot"
  fi
}


compile_and_restart() {
  echo "[INFO] Modifications détectées. Compilation..."
  mvn install -pl "$MODULES" -am || return

  echo "[INFO] Pause pour laisser Maven finir d’écrire les JARs..."
  sleep 5

  echo "[INFO] Redémarrage de Spring Boot..."
  stop_spring
  start_spring
}

# === PRÉPARATION ===

mkdir -p restarter
echo "Indexation des fichiers Java..."
find . -name "*.java" > "$JAVA_FILES_LIST"

export -f compile_and_restart
export -f start_spring
export -f stop_spring
export MODULES
export MAIN_MODULE
export PROFILE

# === LANCEMENT ===

#start_spring

echo "[INFO] Spring Boot (PID $SPRING_PID)"

echo "Watching for changes..."
cat "$JAVA_FILES_LIST" | entr -n bash -c 'compile_and_restart'
