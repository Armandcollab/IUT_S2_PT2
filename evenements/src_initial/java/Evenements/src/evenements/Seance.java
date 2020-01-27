package evenements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

public class Seance {

    static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    static SimpleDateFormat icalFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");

    /**
     * Titre (intitulé) de la séance qui apparaîtra dans l'emploi du temps
     */
    String titre;

    /**
     * Description détaillée de la séance
     */
    String description;

    /**
     * Date de début et de fin de l'événement
     */
    Date dateDebut;
    Date dateFin;

    /**
     * Type de l'événement (Par exemple "TD", "Cours" ...)
     */
    String type;

    /**
     * Salles de la séance
     */
    String[] salles;

    /**
     * Promotion concernée
     */
    String promotion;

    /**
     * Id de l'événement associé, peut être null
     */
    Integer idEvenement;

    Seance(String titre, String description, Date dateDebut, Date dateFin,
            String type, String[] salles, String promotion, Integer idEvenement) {
        this.titre = titre;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.type = type;
        this.salles = salles;
        this.promotion = promotion;
        this.idEvenement = idEvenement;
    }

    /**
     * Formate la date en heure (par exemple 14:32)
     */
    static String dateVersHeure(Date date) {
        return timeFormat.format(date);
    }

    /**
     * Formate la date en heure iCal.
     */
    static String dateVersChaineIcal(Date date) {
        icalFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return icalFormat.format(date);
    }

    /**
     * Obtiens les minutes depuis minuit le jour en cours pour une date donnée.
     */
    static int dateVersMinutesJour(Date date) {
        SimpleDateFormat heures = new SimpleDateFormat("HH");
        SimpleDateFormat minutes = new SimpleDateFormat("mm");

        return Integer.parseInt(heures.format(date)) * 60 + Integer.parseInt(minutes.format(date));
    }

    /**
     * Formate une date avec son heure (par exemple 2020-06-22 14:32:24)
     */
    static String dateVersChaineAvecHeure(Date date) {
        return dateTimeFormat.format(date);
    }

    /**
     * Formate une date (par exemple 2020-06-22)
     */
    static String dateVersChaine(Date date) {
        return dateFormat.format(date);
    }

    /**
     * Représentation du matin d'une date donnée
     */
    static String dateVersChaineMatin(Date date) {
        return dateFormat.format(date) + " 00:00:00";
    }

    /**
     * Représentation du soir d'une date donnée
     */
    static String dateVersChaineSoir(Date date) {
        return dateFormat.format(date) + " 23:59:59";
    }

    /**
     * Le lendemain d'une date donnée
     */
    static Date lendemain(Date date) {
        long timestamp = date.getTime();
        timestamp += 24 * 3600 * 1000;

        return new Date(timestamp);
    }

    /**
     * La veille d'une date donnée
     */
    static Date veille(Date date) {
        long timestamp = date.getTime();
        timestamp -= 24 * 3600 * 1000;

        return new Date(timestamp);
    }

    /**
     * Convertit une date avec heure (par exemple 2020-06-22 14:32:24) en date
     * Java
     */
    static Date chaineVersDate(String chaine) {
        try {
            return dateTimeFormat.parse(chaine);
        } catch (ParseException e) {
            e.printStackTrace(System.err);
            throw new IllegalArgumentException("Impossible de convertir \"" + chaine + "\" en date");
        }
    }

    /**
     * Couleur de la séance, calculée à partir d'un hash du titre
     */
    public String couleur() {
        int hash = (this.titre.hashCode() * 178277739) & 0xffffff;
        hash &= 0x8080c0;

        return String.format("%06X", hash);
    }

    /**
     * Est-ce qu'une date donnée est contenue dans la séance?
     */
    public boolean contient(Date date) {
        return this.dateDebut.before(date) && this.dateFin.after(date);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.titre);
        hash = 23 * hash + Objects.hashCode(this.description);
        hash = 23 * hash + Objects.hashCode(this.dateDebut);
        hash = 23 * hash + Objects.hashCode(this.dateFin);
        hash = 23 * hash + Objects.hashCode(this.type);
        hash = 23 * hash + Arrays.deepHashCode(this.salles);
        hash = 23 * hash + Objects.hashCode(this.promotion);
        hash = 23 * hash + Objects.hashCode(this.idEvenement);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Seance other = (Seance) obj;
        if (!Objects.equals(this.titre, other.titre)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.promotion, other.promotion)) {
            return false;
        }
        if (!Objects.equals(this.dateDebut, other.dateDebut)) {
            return false;
        }
        if (!Objects.equals(this.dateFin, other.dateFin)) {
            return false;
        }
        if (!Arrays.deepEquals(this.salles, other.salles)) {
            return false;
        }
        if (!Objects.equals(this.idEvenement, other.idEvenement)) {
            return false;
        }
        return true;
    }

}
