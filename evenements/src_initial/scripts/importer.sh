#!/bin/bash
# Ce script sera executé toutes les heures afin de maintenir le
# site généré à jour

echo "* Compilation..."
./scripts/compiler.sh

echo "* Vidage de la base de données"
./scripts/executer.sh vider

echo "* Import des salles"
./scripts/executer.sh import-salles data/salles/salles.csv

echo "* Import des événements et de leurs séances"
./scripts/executer.sh import-evenements data/evenements/forum_stages.csv
./scripts/executer.sh import-evenements data/evenements/jpo.csv
./scripts/executer.sh import-seances data/evenements/forum_stages_seances.csv
./scripts/executer.sh import-seances data/evenements/jpo_seances.csv

echo "* Import des séances de l'API"
./scripts/telecharger-seances.sh
