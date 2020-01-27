package evenements;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Chargement d'un fichier CSV basique, avec une ligne d'entête, et dont toutes
 * les lignes ont le même nombre de champs champs par ligne. Avec gestion du
 * caractère d'échappement, hormis les retours à la ligne au sein d'un champ
 * échappé.
 */
class FormatCsv {

    /**
     * Nom du fichier CSV.
     */
    final String nomFichierCsv;

    /**
     * Nombre de champs d'une ligne
     */
    int nbChamps;

    /**
     * Données du fichier CSV (par ligne puis par champ).
     */
    final ArrayList<HashMap<String, String>> donnees;

    /**
     * Caractère séparateur de champs au sein d'une ligne
     */
    final char separateur;

    /**
     * Champs du fichier CSV (donnés par la ligne d'entête).
     */
    ArrayList<String> nomsChamps;

    /**
     * Caractère d'échappement, permettant d'utiliser le séparateur au sein d'un
     * champ. Ce caractère d'échappement ne doit apparaître qu'en début et fin
     * de champ. Il ne peut être lui-même échappé, et est donc interdit dans les
     * contenus.
     */
    final static char CARACTERE_ECHAPPEMENT = '"';

    /**
     * Constructeur.
     *
     * @param nomFichierCsv nom du fichier CSV
     * @param separateur séparateur de champs
     */
    public FormatCsv(final String nomFichierCsv, final char separateur) {
        this.nomFichierCsv = nomFichierCsv;
        this.separateur = separateur;
        this.donnees = new ArrayList<>();
        this.nomsChamps = new ArrayList<>();
    }

    /**
     * Lire une ligne du fichier CSV.
     *
     * @param ligne ligne sous forme de chaîne de caractères
     * @return la ligne découpée en champs
     */
    ArrayList<String> traiterLigne(final String ligne) {
        ArrayList<String> liste = new ArrayList<>();
        if (ligne != null) {
            // allez hop, un automate !
            int i = 0;
            final int DEBUT_CHAMP = 0;
            final int DANS_CHAMP_NON_ECHAPPE = 1;
            final int DANS_CHAMP_ECHAPPE = 2;
            int etat = DEBUT_CHAMP;
            StringBuilder champ = new StringBuilder();
            char lettre = ' ';
            while (i < ligne.length()) {
                lettre = ligne.charAt(i);
                boolean terminerChamp = false;
                switch (etat) {
                    case DEBUT_CHAMP:
                        if (lettre == CARACTERE_ECHAPPEMENT) {
                            etat = DANS_CHAMP_ECHAPPE;
                        } else if (lettre == separateur) {
                            terminerChamp = true;
                        } else {
                            champ.append(lettre);
                            etat = DANS_CHAMP_NON_ECHAPPE;
                        }
                        break;
                    case DANS_CHAMP_NON_ECHAPPE:
                        if (lettre == CARACTERE_ECHAPPEMENT) {
                            throw new IllegalArgumentException("CSV incorrect : guillemets dans un champ "
                                    + "qui ne commence pas par des guillemets : " + ligne);
                        } else if (lettre == separateur) {
                            terminerChamp = true;
                        } else {
                            champ.append(lettre);
                        }
                        break;
                    case DANS_CHAMP_ECHAPPE:
                        if (lettre == CARACTERE_ECHAPPEMENT) {
                            if (i == ligne.length() - 1) {
                                // dernier caractère de la ligne
                                terminerChamp = true;
                            } else {
                                // on regarde le caractère suivant pour lever l'ambiguïté
                                i++;
                                lettre = ligne.charAt(i);
                                if (lettre == CARACTERE_ECHAPPEMENT) {
                                    // guillemets en double : échappés
                                    champ.append(CARACTERE_ECHAPPEMENT);
                                } else if (lettre == separateur) {
                                    terminerChamp = true;
                                } else {
                                    throw new IllegalArgumentException("Guillemets incorrects : " + ligne);
                                }
                            }
                        } else {
                            champ.append(lettre);
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("État incorrect");
                }
                if (terminerChamp) {
                    liste.add(champ.toString());
                    champ = new StringBuilder();
                    etat = DEBUT_CHAMP;
                }
                i++;
            }
            if (lettre != CARACTERE_ECHAPPEMENT && etat != DANS_CHAMP_ECHAPPE) {
                liste.add(champ.toString());
            }
            if (liste.size() != nbChamps) {
                throw new IllegalArgumentException(
                        "La ligne suivante du fichier CSV ne contient pas "
                        + nbChamps + " champs séparés par " + separateur
                        + " : " + ligne);
            }
        }
        return liste;
    }

    /**
     * Lire la ligne d'entête du fichier CSV.
     *
     * @param ligne ligne sous forme de chaîne de caractères
     */
    void traiterEntete(final String ligne) {
        if (ligne == null) {
            throw new IllegalArgumentException("Pas de ligne d'entête");
        }
        String[] champs = ligne.split(String.valueOf(separateur), nbChamps);
        nomsChamps = new ArrayList<>();
        for (String champ : champs) {
            nomsChamps.add(champ);
        }
        nbChamps = nomsChamps.size();
    }

    /**
     * Lecture du fichier CSV
     */
    @SuppressWarnings("ConvertToTryWithResources")
    void lire() {
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(nomFichierCsv)));
            String ligne = br.readLine();
            if (ligne != null) {
                traiterEntete(ligne);
                ligne = br.readLine();
                while (ligne != null) {
                    final ArrayList<String> ligneTraitee = traiterLigne(ligne);
                    if (ligneTraitee != null) {
                        HashMap<String, String> donneesDeLaLigne = new HashMap<>();
                        for (int k = 0; k < nomsChamps.size(); k++) {
                            if (nomsChamps.size() >= k) {
                                donneesDeLaLigne.put(nomsChamps.get(k), ligneTraitee.get(k));
                            } else {
                                donneesDeLaLigne.put(nomsChamps.get(k), null);
                            }
                        }
                        donnees.add(donneesDeLaLigne);
                        ligne = br.readLine();
                    } else {
                        ligne = null;
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * Nombre de lignes du fichier (suppose qu'il a été lu).
     *
     * @return nombre de lignes du fichier
     */
    int nbLignes() {
        return donnees.size();
    }
}
