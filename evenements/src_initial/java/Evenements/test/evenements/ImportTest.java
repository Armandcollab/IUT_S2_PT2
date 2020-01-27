package evenements;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ImportTest {

    @Before
    public void initialisation() {
        BaseDeDonnees.connexion();
    }

    @Test
    public void importSallesTest() {
        if (Jalon.jalon >= Jalon.JALON_SALLES_ETAGES) {
            BaseDeDonnees.vider();

            Main.importerSalles("./test/evenements/ressources/salles.csv");

            ArrayList<Salle> salles = BaseDeDonnees.obtenirSalles();
            assertEquals("2 salles importées", 2, salles.size());
            salles.sort((s1, s2) -> s1.nom.compareTo(s2.nom));

            if (salles.size() == 2) {
                Salle csv1 = salles.get(0);

                assertEquals("csv1", csv1.nom);
                assertEquals("RDC", csv1.etage);
                assertEquals(1, csv1.Xhautgauche);
                assertEquals(2, csv1.Yhautgauche);
                assertEquals(3, csv1.largeur);
                assertEquals(4, csv1.hauteur);

                Salle csv2 = salles.get(1);

                assertEquals("csv2", csv2.nom);
                assertEquals("1er", csv2.etage);
                assertEquals(5, csv2.Xhautgauche);
                assertEquals(6, csv2.Yhautgauche);
                assertEquals(7, csv2.largeur);
                assertEquals(8, csv2.hauteur);
            }
        }
    }

    @Test
    public void importerSeancesTest() {
        if (Jalon.jalon >= Jalon.JALON_SEANCES) {
            BaseDeDonnees.vider();
            Main.importerSalles("./test/evenements/ressources/salles.csv");
            Main.importerSeances("./test/evenements/ressources/seances.csv");

            ArrayList<Seance> seances = BaseDeDonnees.obtenirSeancesContrainte("TRUE");

            assertEquals(1, seances.size());

            if (1 == seances.size()) {
                Seance seance = seances.get(0);
                String[] salles = {"csv1"};

                assertEquals("une-seance", seance.titre);
                assertEquals("description de la séance", seance.description);

                assertEquals(new Date(120, 0, 24, 14, 00, 00), seance.dateDebut);
                assertEquals(new Date(120, 0, 24, 18, 00, 00), seance.dateFin);

                assertEquals(1, seance.salles.length);
                if (1 == seance.salles.length) {
                    assertEquals("csv1", seance.salles[0]);
                }
                assertEquals(seance.type, "type");
                assertEquals(seance.promotion, "promotion");
            }
        }
    }

    @Test
    public void importerEvenementTest() {
        if (Jalon.jalon >= Jalon.JALON_EVENEMENTS) {
            BaseDeDonnees.vider();

            Main.importerSalles("./test/evenements/ressources/salles.csv");
            Main.importerEvenements("./test/evenements/ressources/evenement.csv");
            Main.importerSeances("./test/evenements/ressources/seance-evt.csv");

            ArrayList<Evenement> evenements = BaseDeDonnees.obtenirEvenements();
            assertEquals(1, evenements.size());

            if (1 == evenements.size()) {
                Evenement evenement = evenements.get(0);

                assertEquals("evt", evenement.nomCourt);
                assertEquals("Evenement", evenement.nom);
                assertEquals("Un événement", evenement.description);

                ArrayList<Seance> seances = BaseDeDonnees.obtenirSeancesEvenement("evt");
                assertEquals("Récupération des séances de l'événement", 1, seances.size());

                if (1 == seances.size()) {
                    assertEquals("une séance de evt", seances.get(0).titre);
                }
            }
        }
    }
}
