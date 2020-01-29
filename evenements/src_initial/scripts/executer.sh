#!/bin/bash

# tester qu'on est dans le répertoire du projet
if [ ! -d java ]; then
	echo "Vous n'êtes pas dans le répertoire du projet. Abandon."
	exit 1
fi

# chemins
source ./scripts/config.sh

java evenements.Main $*
