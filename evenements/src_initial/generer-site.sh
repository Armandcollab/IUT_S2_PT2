#!/bin/bash

bash scripts/importer.sh
bash scripts/generer.sh

rm -r /mnt/raddix/www/perso/2020/$USER/*
cp -r www/* /mnt/raddix/www/perso/2020/$USER

firefox http://info-timide.iut.u-bordeaux.fr/perso/2020/$USER
