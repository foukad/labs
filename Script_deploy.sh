#!/bin/bash

# Vérifie si l'environnement et le namespace sont fournis
if [ $# -ne 2 ]; then
  echo "Usage: $0 <environment> <namespace>"
  exit 1
fi

ENVIRONMENT=$1  # Exemple : ass, int, prd
NAMESPACE=$2    # Namespace Kubernetes

# Parcours des sous-répertoires
for dir in $(find . -type d); do
  # Recherche des fichiers correspondant à l'environnement
  for manifest in "$dir"/*-"$ENVIRONMENT".yml "$dir"/*-"$ENVIRONMENT".yaml 2>/dev/null; do
    if [ -f "$manifest" ]; then
      echo "Déploiement de $manifest dans le namespace $NAMESPACE..."
      
      # Remplacez ici par votre commande personnalisée si besoin
      kubectl apply -f "$manifest" -n "$NAMESPACE"

      # Vérifie si la commande a réussi
      if [ $? -ne 0 ]; then
        echo "Erreur lors du déploiement de $manifest"
        exit 1
      fi
    fi
  done
done

echo "Déploiement terminé avec succès."
