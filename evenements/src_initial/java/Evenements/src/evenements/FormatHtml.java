package evenements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Random;

/**
 * Gestion du format HTML, en particulier des templates.
 */
class FormatHtml {

    /**
     * Traitement d'un fichier de template, c'est-à-dire lecture de ce fichier,
     * et génération d'un fichier Html à partir de celui-ci, en remplaçant les
     * mots-clés.
     *
     * @param nomFichierTemplateEnEntree nom du fichier de template en entrée
     * @param nomFichierHtmlEnSortie nom du fichier html en sortie
     * @param valeursMotsCles valeurs des mots-clés à remplacer
     */
    static void traiterTemplate(
            String nomFichierTemplateEnEntree,
            String nomFichierHtmlEnSortie,
            HashMap<String, String> valeursMotsCles) {
        // ouverture du fichier template en lecture
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(nomFichierTemplateEnEntree), Charset.forName("UTF-8")));
        } catch (FileNotFoundException e) {
            System.err.println("Fichier non trouvé : "
                    + nomFichierTemplateEnEntree + " : abandon.");
            System.exit(1);
        }
        // ouverture du fichier html en écriture
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(nomFichierHtmlEnSortie), Charset.forName("UTF-8")));
        } catch (IOException e) {
            System.err.println("Fichier impossible à ouvrir en écriture : "
                    + nomFichierHtmlEnSortie + " : abandon.");
            System.exit(1);
        }
        // remplacement des mots-clés
        try {
            String ligne = br.readLine();
            while (ligne != null) {
                bw.write(substituer(ligne, valeursMotsCles) + System.lineSeparator());
                ligne = br.readLine();
            }
            br.close();
            bw.close();
        } catch (IOException e) {
            System.err.println("Erreur en lecture/écriture : abandon.");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Dans une ligne, substituer tous les mots-clés par leur valeur. Limitation
     * : on ne substitue que la première occurence de chaque mot-clé.
     *
     * @param ligne la ligne à considérer
     * @param valeursMotsCles les mots-clés et leur valeur associée
     * @return la ligne obtenue en substituant les mots-clés par leur valeur
     */
    static String substituer(String ligne,
            HashMap<String, String> valeursMotsCles) {
        String ligneObtenue = ligne;
        if (ligne != null) {
            for (String cle : valeursMotsCles.keySet()) {
                ligneObtenue = ligneObtenue.replace(cle, valeursMotsCles.get(cle));
            }
        }
        return ligneObtenue;
    }

    /**
     * Renvoie une couleur aléatoire.
     *
     * @return une couleur aleatoire
     */
    static String couleurAleatoire() {
        String couleur = "#";
        for (int i = 0; i < 6; i++) {
            int code = (new Random()).nextInt(16); // entre 0 et 16
            couleur += decimalVersHexa(code);
        }
        return couleur;
    }

    /**
     * Convertit un chiffre décimal (base 10) en chiffre hexadécimal (base 16).
     *
     * @param decimal un chiffre décimal
     * @return le chiffre correspondant en hexadécimal
     */
    static char decimalVersHexa(int decimal) {
        char hexa;
        if (decimal < 10) {
            hexa = (char) ('0' + decimal);
        } else {
            hexa = (char) ('a' + (decimal - 10));
        }
        return hexa;
    }

    /**
     * Échapper les guillemets pour javascript : " devient &quot;.
     *
     * @param chaine chaîne dont on veut échapper les guillements
     * @return la même chaîne, mais avec les guillemets échappés
     */
    static String echapperGuillemetsJS(String chaine) {
        return chaine.replaceAll("\"", "&quot;");
    }
}
