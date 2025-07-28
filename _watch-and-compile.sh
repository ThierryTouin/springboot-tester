#!/bin/bash

# === CONFIGURATION ===
MODULES="springboot-tester-exposition,springboot-tester-domain,springboot-tester-application"
MAIN_MODULE="springboot-tester-exposition"
PROFILE="dev"
JAVA_FILES_LIST="restarter/java_files.txt"
PID_FILE="restarter/spring.pid" # Fichier pour stocker le PID

# === FONCTIONS ===

start_spring() {
    echo "[DEBUG] start_spring() Lancement de Spring Boot avec le profil '$PROFILE'..."
    mvn spring-boot:run -Dspring-boot.run.profiles="$PROFILE" -pl "$MAIN_MODULE" > restarter/spring.log 2>&1 &

    echo "[INFO] Attente du démarrage du processus Java Spring Boot..."

    local current_spring_pid="" # Variable locale pour trouver le PID
    for i in {1..20}; do
        current_spring_pid=$(ps aux | grep '[j]ava' | grep -- "-cp" | grep -F "springboot-tester-exposition" | awk '{print $2}' | head -n1)
        echo "[DEBUG] PID trouvé ? $current_spring_pid"
        if [[ -n "$current_spring_pid" ]]; then
            echo "[INFO] Spring Boot lancé avec le PID $current_spring_pid"
            echo "$current_spring_pid" > "$PID_FILE" # Stocker le PID dans le fichier
            return
        fi
        sleep 1
    done

    echo "[ERREUR] Impossible de trouver le processus Java Spring Boot"
    rm -f "$PID_FILE" # Nettoyer le fichier PID en cas d'échec du démarrage
    exit 1 # Quitter si Spring ne démarre pas, pour éviter une boucle sans fin avec entr
}

stop_spring() {
    # Lire le PID depuis le fichier
    local spring_pid_to_stop=""
    if [[ -f "$PID_FILE" ]]; then
        spring_pid_to_stop=$(cat "$PID_FILE")
    fi

    echo "[DEBUG] stop_spring() avec le PID depuis le fichier : $spring_pid_to_stop"

    if [[ -n "$spring_pid_to_stop" ]] && ps -p "$spring_pid_to_stop" > /dev/null; then
        echo "[INFO] Arrêt de Spring Boot (PID $spring_pid_to_stop)..."
        kill -9 "$spring_pid_to_stop"
        wait "$spring_pid_to_stop" 2>/dev/null || true
        rm -f "$PID_FILE" # Supprimer le fichier PID après l'arrêt
    else
        echo "[WARN] Aucun PID actif trouvé pour Spring Boot ou le fichier PID est manquant/vide."
        rm -f "$PID_FILE" 2>/dev/null || true # S'assurer que le fichier PID est supprimé si le processus n'est pas trouvé
    fi
}

compile_and_restart() {
    echo "[INFO] Modifications détectées. Compilation..."
    # Assurez-vous que le répertoire restarter existe pour le PID_FILE
    mkdir -p restarter
    mvn install -pl "$MODULES" -am || { echo "[ERREUR] La compilation a échoué."; return 1; }

    echo "[INFO] Pause pour laisser Maven finir d'écrire les JARs..."
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

# Exporter les variables que les fonctions pourraient avoir besoin d'utiliser
export MODULES
export MAIN_MODULE
export PROFILE
export PID_FILE

# === LANCEMENT ===

# S'assurer que le fichier PID est propre avant le démarrage initial
rm -f "$PID_FILE" 2>/dev/null

echo "[INFO] Lancement initial de Spring Boot..."
start_spring

# Lire le PID depuis le fichier pour l'affichage
initial_spring_pid=""
if [[ -f "$PID_FILE" ]]; then
    initial_spring_pid=$(cat "$PID_FILE")
fi
echo "[INFO] Spring Boot (PID $initial_spring_pid)"

echo "Surveillance des changements..."

# Fonction pour surveiller les changements
watch_changes() {
    while inotifywait -q -e modify -r $(cat "$JAVA_FILES_LIST" | xargs -n1 dirname | sort | uniq | tr '\n' ' '); do
        echo "[INFO] Modifications détectées. Compilation et redémarrage..."
        compile_and_restart
    done
}

# Démarrer la surveillance des changements
watch_changes

# Nettoyer à la sortie du script
trap 'echo "[INFO] Script terminé. Arrêt de Spring Boot si actif..."; stop_spring' EXIT
