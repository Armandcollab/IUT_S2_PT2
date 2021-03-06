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
        genererPagesSalles();
        genererPageEvenements();

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
        String sallesHtml = "<ul>";

        for (String e : BaseDeDonnees.obtenirSallesParEtages().keySet()) {
            sallesHtml += "<h1 style=\"color : red\">" + e + "</h1>";

            for (Salle s : BaseDeDonnees.obtenirSallesParEtages().get(e)) {
                sallesHtml += "<li><a href=\"salle-" + s.nom + ".html\">" + s.nom + "</a></li>";
            }

        }

        sallesHtml += "</ul>";

        return sallesHtml;
    }

    /**
     * Génère la page du plan
     */
    static void genererPlan() {
        QRCode.ecrireQRCode("www/imgs/qr_code/qr-plan.png", dossierTemplates +"plan.html");

        valeursMotsCles.put("[[SALLES]]", listeSallesHtml(null));
        valeursMotsCles.put("[[QR_CODE]]", "./imgs/qr_code/qr-plan.png");
        traiterTemplate("plan.html");
    }

    /**
     * Créé le code HTML d'une liste de séances
     */
    static String seancesVersListeHtml(ArrayList<Seance> seances) {
        String salleHtml = "<p></p><ul>";
        for (Seance e : seances) {
            salleHtml += "<li><p><span style=\"font-weight : bolder;\"> " + e.titre + " </span> : " + e.description + "</p>"
                    + "<p>Démarre à " + Seance.dateVersHeure(e.dateDebut) + " / Fini à " + Seance.dateVersHeure(e.dateFin) + "</p></li>";
        }
        salleHtml += "</ul>";
        return salleHtml;
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
        for (Salle e : BaseDeDonnees.obtenirSalles()) {
            if (!BaseDeDonnees.obtenirSeancesSalleDate(e.nom, aujourdhui).isEmpty()) {
                valeursMotsCles.put("[[SEANCES]]", seancesVersListeHtml(BaseDeDonnees.obtenirSeancesSalleDate(e.nom, aujourdhui)));
            } else {
                valeursMotsCles.put("[[SEANCES]]", "<p> Pas de séances prévus pour aujourd'hui</p>");
            }
            valeursMotsCles.put("[[SALLE]]", e.nom);
            valeursMotsCles.put("[[QR_CODE_SALLE]]", "./imgs/qr_code/qr-salle-" + e.nom + ".png");
            QRCode.ecrireQRCode("www/imgs/qr_code/qr-salle-" + e.nom + ".png", dossierTemplates + "salle-" + e.nom + ".html");
            traiterTemplate("salle.html", "salle-" + e.nom + ".html");
            valeursMotsCles.remove("[[SEANCES]]");
        }
    }

    /**
     * Retourne le code HTML qui affiche les évènements
     */
    static String listeEvenementsHtml() {
        String evenementsHtml = "";

        for (Evenement e : BaseDeDonnees.obtenirEvenements()) {
            evenementsHtml += "<a style=\"font-size : 3em\" href=\"evenement-" + e.nomCourt + ".html\">" + e.nom + " :</a>"
                    + "<h2>" + e.description + "</h2>";
        }
        return evenementsHtml;
    }

    /**
     * Génère la page des événements et chaque page d'événement
     */
    static void genererPageEvenements() {
        valeursMotsCles.put("[[EVENEMENTS]]", listeEvenementsHtml());
        traiterTemplate("evenements.html");
        for (Evenement e : BaseDeDonnees.obtenirEvenements()) {
            genererPageEvenement(e);
        }
    }

    /**
     * Génère la page d'un événement
     */
    static void genererPageEvenement(Evenement evenement) {
        if (BaseDeDonnees.obtenirSeancesEvenement(evenement.nomCourt).isEmpty()) {
            valeursMotsCles.put("[[DESCRIPTION]]", "Il n'y a pas encore de séances prévues pour cet événement.");
        } else {
            valeursMotsCles.put("[[DESCRIPTION]]", seancesVersListeHtml(BaseDeDonnees.obtenirSeancesEvenement(evenement.nomCourt)));
        }
        valeursMotsCles.put("[[QR_CODE_EVENT]]", "./imgs/qr_code/qr-evenement-" + evenement.nomCourt + ".png");
        QRCode.ecrireQRCode("www/imgs/qr_code/qr-evenement-" + evenement.nomCourt + ".png", dossierTemplates + "qr-evenement-" + evenement.nomCourt + ".html");
        valeursMotsCles.put("[[EVENEMENT]]", evenement.nomCourt);
        traiterTemplate("evenement.html", "evenement-" + evenement.nomCourt + ".html");
    }
}
