package evenements;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Generateur {

    static Date aujourdhui;
    static String dossierTemplates;
    static String dossierSortie;
    static String adresseSiteWeb;
    static HashMap<String, String> valeursMotsCles;

    /**
     * Générer le site web
     */
    static void generer() {
        // Date d'aujourd'hui
        aujourdhui = new Date();

        // Peut être surchargée en utilisant la variable d'environement
        if (System.getenv("DATE_GENERATION") != null) {
            aujourdhui = Seance.chaineVersDate(System.getenv("DATE_GENERATION"));
        }

        valeursMotsCles = new HashMap<>();
        dossierTemplates = System.getenv("TEMPLATES_DIRECTORY");
        dossierSortie = System.getenv("WEBSITE_DIRECTORY");
        adresseSiteWeb = System.getenv("WEBSITE_URL");

        genererPlan();
        // genererPagesSalles();
        // genererPageEvenements();
    }

    /**
     * Traite un template en utilisant les valeurs des mots clés définis
     */
    static void traiterTemplate(String entree, String sortie) {
        FormatHtml.traiterTemplate(dossierTemplates + "/" + entree, dossierSortie
                + "/" + sortie, valeursMotsCles);
    }

    /**
     * Traite un template (même nom en entrée et sortie)
     */
    static void traiterTemplate(String page) {
        traiterTemplate(page, page);
    }

    /**
     * Retourne le code HTML qui affiche les salles
     */
    static String listeSallesHtml(String evenement) {
        if (evenement == null) {
            String sallesHtml = "<ul>";

            for (String e : BaseDeDonnees.obtenirSallesParEtages().keySet()) {
                sallesHtml += "<li>" + e + "</li>";
                sallesHtml += "<ul>";
                for (Salle s : BaseDeDonnees.obtenirSallesParEtages().get(e)) {
                    sallesHtml += "<li>" + s.nom + "</li>";
                }
                sallesHtml += "</ul>";
            }

            sallesHtml += "</ul>";

            return sallesHtml;
        } else {
            String planHtml = "<div class=\"etage\">";
            for (String e : BaseDeDonnees.obtenirSallesParEtages().keySet()) {
                planHtml += "<div class=\"etage_" + e + "\">";
                planHtml += "<p>Etage : " + e + "</p>";
                planHtml += "<img src=\"imgs/plan/" + e + ".png\" alt=\"" + e + "\" style=\" height:auto; width:;\">";
                for (Salle s : BaseDeDonnees.obtenirSallesParEtages().get(e)) {
                    int y = s.Yhautgauche / 6;
                    int x = s.Xhautgauche / 6;
                    int l = s.largeur / 6;
                    int h = s.hauteur / 6;
                    planHtml += "<a href=\"salle.html\" style=\"top: "
                            + y + "px; left: " + x + "px; height: "
                            + h + "px; width: "
                            + l + "px;\">" + s.nom + "</a>";
                }
                planHtml += "</div>";
            }
            planHtml += "</div>";
            return planHtml;
        }
    }

    /**
     * Génère la page du plan
     */
    static void genererPlan() {
        valeursMotsCles.put("[[SALLES]]", listeSallesHtml("truc"));
        traiterTemplate("plan.html");
    }

    /**
     * Créé le code HTML d'une liste de séances
     */
    static String seancesVersListeHtml(ArrayList<Seance> seances) {
        throw new UnsupportedOperationException("A implémenter");
    }

    /**
     * Génère le code HTML d'une liste de séances formatées pour l'affichage
     * esthétique
     */
    static String seancesVersHtmlFormate(ArrayList<Seance> seances) {
        throw new UnsupportedOperationException("A implémenter");
    }

    /**
     * Génère les pages des salles
     */
    static void genererPagesSalles() {
        throw new UnsupportedOperationException("A implémenter");
    }

    /**
     * Génère la page des événements et chaque page d'événement
     */
    static void genererPageEvenements() {
        throw new UnsupportedOperationException("A implémenter");
    }

    /**
     * Génère la page d'un événement
     */
    static void genererPageEvenement(Evenement evenement) {
        throw new UnsupportedOperationException("A implémenter");
    }
}
