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
     * Convertion d'un entier en pixel vers un entier en %
     *
     * @param coordPx la coordonnée en pixel
     * @return Retourne l'entier en %
     */
    static float convertionPxToPourcent(int coordPx, int coordPlan) {
        float nbPourcent = (coordPx * 100) / coordPlan;
        return nbPourcent;
    }

    /**
     * Retourne le code HTML qui affiche les salles
     */
    static String listeSallesHtml(String evenement) {
        int taillePlanL = 3581; // taille réelle d'un plan d'étage
        int taillePlanH = 1265;
        String planHtml = "<div class=\"etage\">";
        for (String e : BaseDeDonnees.obtenirSallesParEtages().keySet()) {
            planHtml += "<div class=\"etage_" + e + "\">";
            planHtml += "<p>Etage : " + e + "</p>";
            planHtml += "<div class=\"plan\">";
            planHtml += "<img src=\"imgs/plan/" + e + ".png\" alt=\"" + e + "\""
                    + "style=\" height:auto; width:"
                    + "35vw ;\">";
            for (Salle s : BaseDeDonnees.obtenirSallesParEtages().get(e)) {
                float y = convertionPxToPourcent(s.Yhautgauche, taillePlanH);
                float x = convertionPxToPourcent(s.Xhautgauche, taillePlanL);
                float l = convertionPxToPourcent(s.largeur, taillePlanL);
                int h = 31;
                planHtml += "<a href=\"salle-" + s.nom + "-" + Seance.dateVersChaine(aujourdhui) + ".html\" style=\"top:"
                        + y + "%; left: " + x + "%; height: "
                        + h + "%; width: "
                        + l + "%;\">" + s.nom + "</a>";
            }
            planHtml += "</div>";
            planHtml += "</div>";
        }
        planHtml += "</div>";
        return planHtml;

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
        String salleHtml = "<p></p><ul>";
        for (Seance e : seances) {
            salleHtml += "<li><p><span style=\"font-weight : bolder;\">" + e.titre + "</span> : " + e.description + "</p>"
                    + "<p>Démarre à " + Seance.dateVersHeure(e.dateDebut) + " / Fini à " + Seance.dateVersHeure(e.dateFin) + "</p></li>";
        }
        salleHtml += "</ul>";
        return salleHtml;
    }

    /**
     * Génère le code HTML d'une liste de séances formatées pour l'affichage
     * esthétique
     */
    static String seancesVersHtmlFormate(ArrayList<Seance> seances, Date date, String sNom) {
        String seance_Html = "";
        boolean cmp = true;
        
        if (seances.isEmpty()) {
            seance_Html += "<div class=\"nav_date\">";
            seance_Html += "<p>" + Seance.dateVersChaine(date) + "</p>"; // completer avec la récupération le la date actuelle
            
            seance_Html += "<a href=\"salle-" + sNom + "-" + Seance.dateVersChaine(Seance.lendemain(date)) + ".html\"><img src=\"imgs/right.png\" alt=\"rght\"></a>"; // accès à la page du jour suivant
            seance_Html += "<a href=\"salle-" + sNom + "-" + Seance.dateVersChaine(aujourdhui) + ".html\" id=\"auj\">aujourd'hui</a>";
            seance_Html += "</div>"; // fin class nav_salle
            seance_Html += "<div class=\"horaire\">";
            for (int i = 8; i <= 17; i++) {
                seance_Html += "<div class=\"echelle\">";
                seance_Html += "<p>" + i + ":00 </p>";
                seance_Html += "<hr>";
                seance_Html += "</div>"; // fin class echelle
            }
            seance_Html += "</div>"; // fin class horaires
        }
        for (Seance s : seances) {
            String[] tab_Deb = Seance.dateVersHeure(s.dateDebut).split(":");
            String[] tab_Fin = Seance.dateVersHeure(s.dateFin).split(":");
            seance_Html += "<div class=\"salle\">";
            if (cmp) {
                seance_Html += "<div class=\"nav_date\">";
                seance_Html += "<p>" + Seance.dateVersChaine(date) + "</p>";
                seance_Html += "<a href=\"salle-" + sNom + "-" + Seance.dateVersChaine(Seance.veille(date)) + ".html\"><img src=\"imgs/left.png\" alt=\"r\"></a>";
                seance_Html += "<a href=\"salle-" + sNom + "-" + Seance.dateVersChaine(Seance.lendemain(date)) + ".html\"><img src=\"imgs/right.png\" alt=\"rght\"></a>"; // accès à la page du jour suivant
                seance_Html += "<a href=\"salle-" + sNom + "-" + Seance.dateVersChaine(aujourdhui) + ".html\" id=\"auj\">aujourd'hui</a>";
                seance_Html += "</div>"; // fin class nav_salle
            }
            seance_Html += "<div class=\"planning\">";
            int top = (Integer.parseInt(tab_Deb[0]) - 8) * 10
                    + ((Integer.parseInt(tab_Deb[1])) / 10) + (Integer.parseInt(tab_Deb[0]) - 8);  //  placement séance 
            int height = (Integer.parseInt(tab_Fin[0]) - Integer.parseInt(tab_Deb[0])) * 10
                    + 10 + ((Integer.parseInt(tab_Deb[1]) - (60 - Integer.parseInt(tab_Fin[1]))) / 10); // taille de la séance en %
            seance_Html += "<div class=\"seance\" style=\"top:" + top + "%; height:" + height + "%; background-color:#" + s.couleur() + ";\">"; // dessin des séances
            seance_Html += "<h2>" + s.titre + "</h2>"; // ajouter le titre de la séance
            seance_Html += "<div class=\"description\">";
            seance_Html += "<p>" + s.description + "</p>"; // description de la séance import de la base
            seance_Html += "<p>" + s.type + "</p>";
            seance_Html += "<p>" + s.promotion + "</p>"; // promotion de la séance import de la base
            seance_Html += "</div>"; // fin class description
            // ajout de l'horaire de la séance A faire: convertion de la date en Heure
            seance_Html += "<p>" + Seance.dateVersHeure(s.dateDebut)
                    + "-->" + Seance.dateVersHeure(s.dateFin) + "</p>";
            seance_Html += "</div>"; // fin class seance
            if (cmp) {
                cmp = false;
                seance_Html += "<div class=\"horaire\">";
                for (int i = 8; i <= 17; i++) {
                    seance_Html += "<div class=\"echelle\">";
                    seance_Html += "<p>" + i + ":00 </p>";
                    seance_Html += "<hr>";
                    seance_Html += "</div>"; // fin class echelle
                }
                seance_Html += "</div>"; // fin class horaires
            }
            seance_Html += "</div>"; // fin class planning
            seance_Html += "</div>";
        }
        return seance_Html;
    }

    /**
     * Génère les pages des salles
     */
    static void genererPagesSalles() {
        Date jourSuivant = aujourdhui;
        Date jourPrecedent = aujourdhui;
        for (Salle e : BaseDeDonnees.obtenirSalles()) {
            valeursMotsCles.put("[[SALLE]]", e.nom);
            valeursMotsCles.put("[[DATE]]", seancesVersHtmlFormate(BaseDeDonnees.obtenirSeancesSalleDate(dossierSortie, jourSuivant), aujourdhui, e.nom));
            traiterTemplate("salle.html", "salle-" + e.nom + "-" + Seance.dateVersChaine(aujourdhui) + ".html");
            valeursMotsCles.remove("[[DATE]]");
            for (int i = 0; i <= 10; i++) {
                jourSuivant = Seance.lendemain(jourSuivant);
                valeursMotsCles.put("[[DATE]]", seancesVersHtmlFormate(BaseDeDonnees.obtenirSeancesSalleDate(dossierSortie, jourSuivant), jourSuivant, e.nom));
                traiterTemplate("salle.html", "salle-" + e.nom + "-" + Seance.dateVersChaine(jourSuivant) + ".html");
                valeursMotsCles.remove("[[DATE]]");
            }
            for (int i = 0; i <= 10; i++) {
                jourPrecedent = Seance.lendemain(jourPrecedent);
                valeursMotsCles.put("[[DATE]]", seancesVersHtmlFormate(BaseDeDonnees.obtenirSeancesSalleDate(dossierSortie, jourPrecedent), jourPrecedent, e.nom));
                traiterTemplate("salle.html", "salle-" + e.nom + "-" + Seance.dateVersChaine(jourPrecedent) + ".html");
                valeursMotsCles.remove("[[DATE]]");
            }
            jourSuivant = aujourdhui;
            jourPrecedent = aujourdhui;
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
     * Crée le code HTML qui affiche le programme d'un evenements
     */
    static String seancesEvenement(ArrayList<Seance> seances) {
        String seancesHTML = "<ul>";
        for (Seance e : seances) {
            seancesHTML += "<li>" + e.titre;
        }
        return seancesHTML;
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
        valeursMotsCles.put("[[EVENEMENT]]", evenement.nomCourt);
        traiterTemplate("evenement.html", "evenement-" + evenement.nomCourt + ".html");
    }
}
