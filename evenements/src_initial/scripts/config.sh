if [ ! -f scripts/parameters.sh ]; then
    echo "Erreur: fichier parameters.sh non trouvé"
    exit 1
fi

source "scripts/parameters.sh"

# Chemins
export PATH_TEMPLATE="templates/"
export PATH_SITE="site/"

# tester qu'on est dans le répertoire du projet
if [ ! -d java ]; then
        echo "Vous n'êtes pas dans le répertoire du projet. Abandon."
        exit 1
fi

# ClassPath java
case "$(uname -s)" in
    MINGW*) export CLASSPATH="lib/mysql-connector-java-8.0.13.jar\;lib/javase-3.4.0.jar;lib/core-3.4.0.jar\;java/Evenements/bin/";;
    Linux*) export CLASSPATH="$PWD/lib/mysql-connector-java-8.0.13.jar:$PWD/lib/javase-3.4.0.jar:$PWD/lib/core-3.4.0.jar:$PWD/java/Evenements/bin/";;
    Darwin*) export CLASSPATH="$PWD/lib/mysql-connector-java-8.0.13.jar:$PWD/lib/javase-3.4.0.jar:$PWD/lib/core-3.4.0.jar:$PWD/java/Evenements/bin/";;
    *) echo "Système non supporté";;
esac

