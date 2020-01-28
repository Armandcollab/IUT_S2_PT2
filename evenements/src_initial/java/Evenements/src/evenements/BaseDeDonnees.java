package evenements;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Connexion à la base de données
 */
class BaseDeDonnees {

    /**
     * Objet gérant la connexion à la base.
     */
    static Connection connexion = null;

    /**
     * Se connecte en utilisant les variables d'environnement comme paramètres
     */
    static void connexion() {
        connexion(System.getenv("MYSQL_HOST"),
                System.getenv("MYSQL_PORT"),
                System.getenv("MYSQL_USER"),
                System.getenv("MYSQL_PASSWORD"),
                System.getenv("MYSQL_DATABASE")
        );
    }

    /**
     * Établi la connexion avec la base de données
     *
     * @param hote
     * @param port
     * @param identifiant
     * @param motDePasse
     * @param base
     */
    static void connexion(String hote, String port, String identifiant, String motDePasse, String base) {
        String uri = "jdbc:mysql://" + hote + ":" + port
                + "/" + base + "?useSSL=false";

        if (!System.getProperty("os.name").startsWith("Windows")) {
            uri += "&useUnicode=true&characterEncoding=utf8";
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connexion = DriverManager.getConnection(uri, identifiant, motDePasse);
        } catch (ClassNotFoundException e) {
            e.printStackTrace(System.err);
            throw new IllegalArgumentException("Impossible de trouver le driver java MySQL");
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new IllegalArgumentException(
                    "Impossible de se connecter à la base de données : " + uri + " " + identifiant + "/" + motDePasse);
        }
        try {
            Statement stmt = connexion.createStatement();
            stmt.executeQuery("SET NAMES 'UTF8'");
            stmt.executeQuery("SET CHARACTER SET 'UTF8'");
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new IllegalArgumentException("Impossible de définir l'encodage");
        }
    }

    /**
     * Importe un étage dans la base de données
     *
     * @param nom le nom de l'étage
     */
    static void importerEtage(String nom) {
        throw new UnsupportedOperationException("A implémenter");
    }

    /**
     * Trouve l'id d'un étage dont le nom est donné
     */
    static int idDeLetage(String nom) {
        throw new UnsupportedOperationException("A implémenter");
    }

    /**
     * Trouve l'ID d'une salle
     */
    static int idDeLaSalle(String nom) {
        return trouverId("salle", "nom", nom);
    }

    /**
     * Trouver l'ID d'un événement
     */
    static int idDeLevenement(String nomCourt) {
        return trouverId("evenement", "nomCourt", nomCourt);
    }

    /**
     * Cherche un étage dans la base, si il existe le retourne, sinon il est
     * importé dans la base puis son id est retournée
     */
    static int importerOuTrouverEtage(String nom) {
        throw new UnsupportedOperationException("A implémenter");
    }

    /**
     * Importe les salles fournie en argument dans la base
     */
    static void importerSalle(Salle salle) {
        executerRequete("INSERT INTO salle (nom)"
                + " VALUES (\"" + echapper(salle.nom) + "\")",
                "Erreur lors de l'importation d'une salle");
    }

    /**
     * Importe la séance donnée en argument dans la base
     */
    static void importerSeance(Seance seance) {
        throw new UnsupportedOperationException("A implémenter");
    }

    /**
     * Importe un événement passé en paramètre dans la base
     */
    static void importerEvenement(Evenement evenement) {
        executerRequete("INSERT INTO evenement (nomcourt, nom, description)"
                + " VALUES ('" + echapper(evenement.nomCourt) + "," + echapper(evenement.nom) + "," + echapper(evenement.description)+ "')",
                "Erreur lors de l'importation d'une evenement");
    }

    /**
     * Récupère toutes les salles de la base de données
     */
    static ArrayList<Salle> obtenirSalles() {
        ArrayList<Salle> salles = new ArrayList<>();

        try {
            Statement stmt = connexion.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM salle");

            while (results.next()) {
                Salle salle = new Salle(
                        results.getString("nom"),
                        0,
                        0,
                        0,
                        0,
                        null
                );
                salles.add(salle);
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new IllegalArgumentException("Impossible d'obtenir la liste des"
                    + "salles");

        }

        return salles;
    }

    /**
     * Obtiens les salles de la base de données, et les regroupe par étage
     */
    static HashMap<String, ArrayList<Salle>> obtenirSallesParEtages() {
        throw new UnsupportedOperationException("A implémenter");
    }

    /**
     * Est-ce que la salle est occupee a une date donnée ?
     */
    static boolean salleOccupee(Salle salle, Date date, String evenement) {
        throw new UnsupportedOperationException("A implémenter");
    }

    /**
     * Obtiens les séances pour une contrainte donnée, par exemple
     * "seance.evenement_id = 4"
     */
    static ArrayList<Seance> obtenirSeancesContrainte(String contrainte) {
        throw new UnsupportedOperationException("A implémenter");
    }

    /**
     * Obtenir les séances qui ont lieu dans une salle donnée et à une date
     * donnée
     */
    static ArrayList<Seance> obtenirSeancesSalleDate(String salle, Date date) {
        throw new UnsupportedOperationException("A implémenter");
    }

    /**
     * Obtenir les séances qui ont lieu dans une salle donnée et à une date
     * donnée
     */
    static ArrayList<Seance> obtenirSeancesEvenement(String evenement) {
        throw new UnsupportedOperationException("A implémenter");
    }

    /**
     * Obtenir tous les événements depuis la base de données
     */
    static ArrayList<Evenement> obtenirEvenements() {
        ArrayList<Evenement> Event = new ArrayList<>();

        try {
            Statement stmt = connexion.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM evenement");

            while (results.next()) {
                Evenement event = new Evenement(
                        results.getString("nomCourt"),
                        results.getString("nom"),
                        results.getString("description")
                );
                Event.add(event);
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new IllegalArgumentException("Impossible d'obtenir la liste des"
                    + "evenements");

        }

        return Event;
    }

    /**
     * Essaie d'executer la requête SQL passée en paramètre, affiche le message
     * d'erreur en cas d'échec
     *
     * Si la requête insère une entrée en base, retourne son id
     */
    static Integer executerRequete(String sql, String messageDerreur) {
        Integer id = null;
        try {
            Statement stmt = connexion.createStatement();
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet result = stmt.getGeneratedKeys();
            if (result.next()) {
                id = result.getInt(1);
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new IllegalArgumentException(messageDerreur);
        }
        return id;
    }

    /**
     * Vide toutes les tables
     */
    static void vider() {
        viderTable("salle");
        viderTable("etage");
        viderTable("salle_seance");
        viderTable("seance");
        viderTable("evenement");
    }

    /**
     * Cherche l'id d'une entrée dans la base avec une condition donnée
     *
     * Par exemple trouverId("voiture", "immatriculation", "AB123CD")
     */
    static int trouverId(String table, String conditionChamp, String conditionValeur) {
        int id = 0;

        try {
            Statement stmt = connexion.createStatement();
            ResultSet results = stmt.executeQuery("SELECT id FROM " + table + " WHERE "
                    + conditionChamp + " = \"" + echapper(conditionValeur) + "\"");

            if (results.next()) {
                id = results.getInt("id");
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new IllegalArgumentException("Impossible d'obtenir la liste des"
                    + "salles");

        }

        return id;
    }

    /**
     * Supprimer tous les éléments d'une table donnée.
     *
     * @param table le nom de la table que l'on souhaite vider
     */
    static void viderTable(String table) {
        String requete = "DELETE FROM " + table;
        try {
            Statement stmt = connexion.createStatement();
            stmt.executeUpdate(requete);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new IllegalArgumentException(
                    "Erreur lors de la purge de la table " + table);
        }
    }

    /**
     * Échapper les guillemets simples pour les chaînes de caractères des
     * requêtes SQL.
     *
     * @param chaine chaîne de caractère dont on veut échapper les caractères
     * spéciaux
     * @return chaîne avec les caractères spéciaux échappés
     */
    static String echapper(String chaine) {
        return chaine.replaceAll("'", "''");
    }

    private static void executerRequete(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
