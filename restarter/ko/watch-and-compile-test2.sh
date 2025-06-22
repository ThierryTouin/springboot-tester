#!/bin/bash

# === CONFIGURATION ===
MODULES="springboot-tester-exposition,springboot-tester-domain,springboot-tester-application"
MAIN_MODULE="springboot-tester-exposition"
PROFILE="dev"
JAVA_FILES_LIST="restarter/java_files.txt"
PID_FILE="restarter/spring.pid" # File to store the PID

# === FONCTIONS ===

start_spring() {
  echo "[DEBUG] start_spring() Lancement de Spring Boot avec le profil '$PROFILE'..."
  mvn spring-boot:run -Dspring-boot.run.profiles="$PROFILE" -pl "$MAIN_MODULE" > restarter/spring.log 2>&1 &

  echo "[INFO] Attente du démarrage du processus Java Spring Boot..."

  local current_spring_pid="" # Use a local variable for finding the PID
  for i in {1..20}; do
    current_spring_pid=$(ps aux | grep '[j]ava' | grep -- "-cp" | grep -F "springboot-tester-exposition" | awk '{print $2}' | head -n1)
    echo "[DEBUG] PID trouvé ? $current_spring_pid"
    if [[ -n "$current_spring_pid" ]]; then
      echo "[INFO] Spring Boot lancé avec le PID $current_spring_pid"
      echo "$current_spring_pid" > "$PID_FILE" # Store the PID in the file
      return
    fi
    sleep 1
  done

  echo "[ERREUR] Impossible de trouver le processus Java Spring Boot"
  rm -f "$PID_FILE" # Clean up PID file if startup fails
  exit 1 # Exit if Spring fails to start, to prevent entr from looping endlessly
}

stop_spring() {
  # Read the PID from the file
  local spring_pid_to_stop=""
  if [[ -f "$PID_FILE" ]]; then
    spring_pid_to_stop=$(cat "$PID_FILE")
  fi

  echo "[DEBUG] stop_spring() with PID from file: $spring_pid_to_stop"

  if [[ -n "$spring_pid_to_stop" ]] && ps -p "$spring_pid_to_stop" > /dev/null; then
    echo "[INFO] Arrêt de Spring Boot (PID $spring_pid_to_stop)..."
    kill -9 "$spring_pid_to_stop"
    wait "$spring_pid_to_stop" 2>/dev/null || true
    rm -f "$PID_FILE" # Remove the PID file after stopping
  else
    echo "[WARN] Aucun PID actif trouvé pour Spring Boot ou le fichier PID est manquant/vide."
    rm -f "$PID_FILE" 2>/dev/null || true # Ensure PID file is removed if process not found
  fi
}

compile_and_restart() {
  echo "[INFO] Modifications détectées. Compilation..."
  # Ensure the restarter directory exists for PID_FILE
  mkdir -p restarter
  mvn install -pl "$MODULES" -am || { echo "[ERREUR] La compilation a échoué."; return 1; }

  echo "[INFO] Pause pour laisser Maven finir d’écrire les JARs..."
  sleep 5

  echo "[INFO] Redémarrage de Spring Boot..."
  stop_spring
  start_spring
}

# === PRÉPARATION ===

if [[ ! -f "$JAVA_FILES_LIST" ]]; then
  mkdir -p restarter
  echo "Indexation des fichiers Java..."
  find . -name "*.java" \
    -not -path "*/target/*" \
    -not -path "*/.idea/*" \
    -not -path "*/.git/*" \
    -not -path "*/build/*" \
    > "$JAVA_FILES_LIST"
fi


# Export the variables that functions might need access to
export MODULES
export MAIN_MODULE
export PROFILE
export PID_FILE

# --- Ensure functions are available when entr executes ---
# We need to export the functions themselves if they are to be called directly
# by `bash -c 'function_name'`. However, a cleaner way is to source the script itself.
# Let's change the entr command to source the current script.

# === LANCEMENT ===

# Ensure the PID file is clean before initial start
rm -f "$PID_FILE" 2>/dev/null

echo "[INFO] Lancement initial de Spring Boot..."
start_spring

# Read the PID from the file for display
initial_spring_pid=""
if [[ -f "$PID_FILE" ]]; then
  initial_spring_pid=$(cat "$PID_FILE")
fi
echo "[INFO] Spring Boot (PID $initial_spring_pid)"

echo "Watching for changes..."

# --- CRITICAL CHANGE HERE ---
# Instead of `bash -c 'compile_and_restart'`, we tell entr to execute
# the current script, and that script will then call compile_and_restart.
# We pass the function name as an argument to the script itself.
# This assumes the script is named something like `restart_watcher.sh`
# Let's use a common pattern for self-sourcing scripts.

# Get the absolute path of the current script
SCRIPT_PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/$(basename "${BASH_SOURCE[0]}")"

cat "$JAVA_FILES_LIST" | entr -n bash -c '
  echo "[ENTR] Changement détecté. Sourcing du script : '"$SCRIPT_PATH"'"
  if source "'"$SCRIPT_PATH"'"; then
    echo "[ENTR] Script sourcé avec succès. Appel de compile_and_restart..."
    compile_and_restart
  else
    echo "[ERREUR] Échec du sourcing du script."
    exit 1
  fi
'


# --- Clean up on script exit ---
# This trap will ensure Spring Boot is stopped when you exit the main script
trap 'echo "[INFO] Script terminé. Arrêt de Spring Boot si actif..."; stop_spring' EXIT