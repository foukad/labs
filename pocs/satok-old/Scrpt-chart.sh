#!/bin/bash

# Vérification des arguments
if [ "$#" -ne 2 ]; then
  echo "Usage: $0 <chart_path> <output_directory>"
  exit 1
fi

CHART_PATH=$1
OUTPUT_DIR=$2

# Liste des environnements
ENVIRONMENTS=("dev" "stg" "prd")

# Créer le répertoire de sortie
mkdir -p "$OUTPUT_DIR"

# Génération des manifests pour chaque environnement
for ENV in "${ENVIRONMENTS[@]}"; do
  VALUES_FILE="values-${ENV}.yaml"
  
  if [ ! -f "$VALUES_FILE" ]; then
    echo "⚠️  Fichier de valeurs non trouvé : $VALUES_FILE. Ignoré."
    continue
  fi

  OUTPUT_FILE="${OUTPUT_DIR}/manifests-${ENV}.yaml"

  echo "📦 Génération des manifests pour l'environnement: $ENV"
  helm template "$ENV" "$CHART_PATH" -f "$VALUES_FILE" > "$OUTPUT_FILE"

  if [ $? -eq 0 ]; then
    echo "✅ Manifests générés : $OUTPUT_FILE"
  else
    echo "❌ Erreur lors de la génération pour $ENV"
  fi
done
