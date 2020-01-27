#!/bin/bash

# Recopie des templates vers le dossier cible
rm -rf www
cp -R templates/ www/

# Lancement de la génération du site
scripts/executer.sh generer
