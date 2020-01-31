package evenements;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Main {

    /**
     * Importe un fichier CSV
     */
    static void importerSalles(String fichier) {
        FormatCsv csv = new FormatCsv(fichier, ',');
        csv.lire();

        for (HashMap<String, String> donneesSalle : csv.donnees) {
            Salle salle = new Salle(
                    donneesSalle.get("Nom"),
                    Integer.parseInt(donneesSalle.get("Xhautgauche")),
                    Integer.parseInt(donneesSalle.get("Yhautgauche")),
                    Integer.parseInt(donneesSalle.get("Largeur")),
                    Integer.parseInt(donneesSalle.get("Hauteur")),
                    donneesSalle.get("Etage")
            );
            BaseDeDonnees.importerSalle(salle);
        }
    }

    /**
     * Importer les séances
     */
    static void importerSeances(String fichier) {
        FormatCsv csv = new FormatCsv(fichier, ',');
        csv.lire();

        for (HashMap<String, String> donneesSeance : csv.donnees) {
            String[] sallesTab = donneesSeance.get("Salles").split(",");
            Seance seance = new Seance(
                    donneesSeance.get("Titre"),
                    donneesSeance.get("Description"),
                    Seance.chaineVersDate(donneesSeance.get("DateDebut")),
                    Seance.chaineVersDate(donneesSeance.get("DateFin")),
                    donneesSeance.get("Type"),
                    sallesTab,
                    donneesSeance.get("Promotion"),
                    BaseDeDonnees.idDeLevenement(donneesSeance.get("Evenement"))
            );
            BaseDeDonnees.importerSeance(seance);
        }
    }

    /**
     * Importe les événements d'un fichier CSV dans la base de données
     */
    static void importerEvenements(String fichier) {
        FormatCsv csv = new FormatCsv(fichier, ',');
        csv.lire();

        for (HashMap<String, String> donneesEvent : csv.donnees) {
            Evenement event;
            event = new Evenement(
                    donneesEvent.get("NomCourt"),
                    donneesEvent.get("Nom"),
                    donneesEvent.get("Description"));
            BaseDeDonnees.importerEvenement(event);
        }
    }

    /**
     * Générer le site web
     */
    static void generer() {
        Generateur.generer();
    }

    /**
     * Vider la base
     */
    static void vider() {
        BaseDeDonnees.vider();
    }

    /**
     * Affiche la liste des salles
     */
    static void listerSalles() {
        for (Salle salle : BaseDeDonnees.obtenirSalles()) {
            System.out.println(salle.nom);
        }
    }

    /**
     * Vérifie que l'ensemble des variables d'environement requise au programme
     * sont définies
     */
    static boolean verifierVariablesEnvironement() {
        String[] noms = {
            "MYSQL_HOST", "MYSQL_USER", "MYSQL_PASSWORD", "MYSQL_DATABASE",
            "TEMPLATES_DIRECTORY", "WEBSITE_DIRECTORY"
        };

        for (String nom : noms) {
            if (System.getenv(nom) == null) {
                System.err.println("La variable d'environement " + nom + " n'est pas définie");
                return false;
            }
        }

        return true;
    }

    /**
     * Vérifie que le nombre d'argument fourni sont correct. La chaîne d'entrée
     * est la liste d'arguments attendus, par exemple "arg1 arg2 arg3"
     */
    static boolean verifierArgument(String[] args, String arguments) {
        if (args.length == 1) {
            System.err.println("Syntaxe: " + args[0] + " " + arguments);
            return false;
        }

        return true;
    }

    /**
     * Programme principal
     */
    public static void main(String[] args) throws Exception {
        if (verifierVariablesEnvironement()) {
            BaseDeDonnees.connexion();

            if (args.length != 0) {
                switch (args[0]) {
                    case "import-salles":
                        if (verifierArgument(args, "fichier-csv")) {
                            importerSalles(args[1]);
                        }
                        break;
                    case "import-seances":
                        if (verifierArgument(args, "fichier-csv")) {
                            importerSeances(args[1]);
                        }
                        break;
                    case "import-evenements":
                        if (verifierArgument(args, "fichier-csv")) {
                            importerEvenements(args[1]);
                        }
                        break;
                    case "generer":
                        generer();
                        break;
                    case "vider":
                        vider();
                        break;
                    case "lister-salles":
                        listerSalles();
                        break;
                    default:
                        System.err.println("Commande inconnue: " + args[0]);
                        break;
                }
            } else {
                System.err.println("Vous devez fournir un argument au programme");
            }
        }
    }

}
