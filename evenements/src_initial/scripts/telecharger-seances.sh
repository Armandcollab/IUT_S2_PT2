#!/bin/bash
# Importe les événements HyperPlanning depuis l'API pour les 30 prochains
# jours

source ./scripts/config.sh

DossierTelCSV=scripts/DossierTelCSV/
rm $DossierTelCSV/*

* "Lancement des requetes vers l'API"
for salle in $(./scripts/executer.sh lister-salles)
do 
    echo "* Importation des événements des 30 prochains jours de la salle $salle"

    for i in `seq 0 29`
    do
        wget -q -P $DossierTelCSV http://info-timide.iut.u-bordeaux.fr/projets/pt2_api/$(date +%Y-%m-%d --date="+$i day")/$salle.csv && mv $DossierTelCSV/$salle.csv $DossierTelCSV/$(date +%Y-%m-%d --date="+$i day")-$salle.csv
    done
done