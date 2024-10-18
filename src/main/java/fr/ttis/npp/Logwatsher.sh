#!/bin/bash

# Variables
NAMESPACE="default"  # Nom du namespace Kubernetes à surveiller
APP_LABEL="my-microservice"  # Label de l'application pour filtrer les pods
LOG_FILE="/tmp/k8s_log_errors.log"  # Fichier temporaire pour stocker les erreurs
EMAIL_RECIPIENT="admin@example.com"  # Email pour envoyer les alertes
INTERVAL=60  # Intervalle en secondes entre les vérifications

# Fonction pour envoyer une alerte (par exemple, par email)
send_alert() {
    local message=$1
    echo "$message" | mail -s "K8s Error Alert" $EMAIL_RECIPIENT
}

# Fonction pour surveiller les logs
watch_logs() {
    echo "Monitoring logs for errors in namespace $NAMESPACE for app $APP_LABEL..."

    # Boucle infinie pour vérifier les logs à intervalles réguliers
    while true; do
        # Extraire les logs des pods contenant le label spécifié
        kubectl logs -l app=$APP_LABEL -n $NAMESPACE --all-containers --since=1m > $LOG_FILE

        # Rechercher les lignes contenant des erreurs
        ERRORS=$(grep -i 'error\|warn' $LOG_FILE)

        if [[ ! -z "$ERRORS" ]]; then
            # Si des erreurs sont trouvées, envoyer une alerte
            echo "Errors found! Sending alert..."
            send_alert "$ERRORS"
        else
            echo "No errors detected in the last check."
        fi

        # Pause avant la prochaine vérification
        sleep $INTERVAL
    done
}

# Exécuter le log watcher
watch_logs
