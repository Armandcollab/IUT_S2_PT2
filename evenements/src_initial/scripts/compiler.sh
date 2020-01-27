#!/bin/bash

#
# Compiler le programme Evenements.
#

# tester qu'on est dans le répertoire du projet
if [ ! -d java ]; then
	echo "Vous n'êtes pas dans le répertoire du projet. Abandon."
	exit 1
fi

# répertoires source et destination
REP_SRC=./java/Evenements/src
REP_DEST=./java/Evenements/bin
rm -rf $REP_DEST
mkdir -p $REP_DEST

source ./scripts/config.sh
case "$(uname -s)" in
    MINGW*) export CLASSPATH="$CLASSPATH;$REP_SRC";;
    Linux*) export CLASSPATH="$CLASSPATH:$REP_SRC";;
    Darwin*) export CLASSPATH="$CLASSPATH:$REP_SRC";;
    *) echo "Système non supporté";;
esac

# compilation
echo "* Compilation du programme..."
javac -d $REP_DEST $REP_SRC/evenements/Main.java

