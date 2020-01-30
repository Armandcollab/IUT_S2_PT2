
package evenements;


/**
 * Gestion des jalons.
 */
class Jalon {
    /**
     * Intégration des salles (coordonnées et des étages)
     */
    static int JALON_SALLES_ETAGES = 2;

    /**
     * Intégration des séances
     */
    static int JALON_SEANCES = 3;

    /**
     * Intégration des événements
     */
    static int JALON_EVENEMENTS = 4;

    /**
     * Interrogation de l'API et ajout des QR codes
     */
    static int JALON_API_QR_CODES = 5;

    /**
     * Intégration HTML (vue en plan, vue des séances)
     */
    static int JALON_INTEGRATION_HTML = 6;

    /**
     * Plan en temps réel
     */
    static int JALON_PLAN_TEMPS_REEL = 7;

    /**
     * Fichiers iCalendar
     */
    static int JALON_ICAL = 8;

    /**
     * Le jalon en cours de développement.
     */
    static int jalon = 6;
}
