package evenements;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class BaseDeDonneesTest {

    @Before
    public void initialisation() {
        BaseDeDonnees.connexion();
    }

    @Test
    public void testImportSalle() {
        if (Jalon.jalon == 1) {
            BaseDeDonnees.vider();
            Salle salleTest = new Salle("test", 12, 45, 56, 78, "RDC");
            BaseDeDonnees.importerSalle(salleTest);

            ArrayList<Salle> salles = BaseDeDonnees.obtenirSalles();

            assertEquals("Une salle insérée", 1, salles.size());

            if (salles.size() == 1) {
                Salle salle = salles.get(0);

                assertEquals("Nom de la salle inséré", "test", salle.nom);

                if (Jalon.jalon >= Jalon.JALON_SALLES_ETAGES) {
                    assertEquals("Coordonnées de la salle insérée", 12, salle.Xhautgauche);
                    assertEquals("Coordonnées de la salle insérée", 45, salle.Yhautgauche);
                    assertEquals("Coordonnées de la salle insérée", 56, salle.largeur);
                    assertEquals("Coordonnées de la salle insérée", 78, salle.hauteur);
                    assertEquals("Étage de la salle insérée", "RDC", salle.etage);
                }
            }
        }
    }

    @Test
    public void testSallesParEtage() {
        if (Jalon.jalon >= Jalon.JALON_SALLES_ETAGES) {
            BaseDeDonnees.vider();

            BaseDeDonnees.importerSalle(new Salle("test1", 12, 45, 56, 78, "RDC"));
            BaseDeDonnees.importerSalle(new Salle("test2", 12, 45, 56, 78, "RDC"));
            BaseDeDonnees.importerSalle(new Salle("test3", 12, 45, 56, 78, "1er"));

            HashMap<String, ArrayList<Salle>> sallesParEtages = BaseDeDonnees.obtenirSallesParEtages();

            assertEquals("Deux étages", 2, sallesParEtages.size());

            assertTrue("L'étage RDC est dans sallesParEtages", sallesParEtages.containsKey("RDC"));
            assertTrue("L'étage 1er est dans sallesParEtages", sallesParEtages.containsKey("1er"));

            if (sallesParEtages.containsKey("RDC")) {
                assertEquals("2 salles au RDC", 2, sallesParEtages.get("RDC").size());
            }
            if (sallesParEtages.containsKey("1er")) {
                assertEquals("1 salle au 1er", 1, sallesParEtages.get("1er").size());

                assertEquals("La salle test3 est au premier", "test3", sallesParEtages.get("1er").get(0).nom);
            }
        }
    }

    @Test
    public void testImportSeance() {
        if (Jalon.jalon >= Jalon.JALON_SEANCES) {
            BaseDeDonnees.vider();
            BaseDeDonnees.importerSalle(new Salle("test", 12, 45, 56, 78, "RDC"));
            String[] salles = {"test"};
            Date debut = new Date(120, 0, 25, 14, 00, 00);
            Date fin = new Date(120, 0, 25, 18, 00, 00);

            BaseDeDonnees.importerSeance(new Seance("Une séance", "Description",
                    debut, fin, "Un type", salles, "INFO_S1", 0)
            );

            ArrayList<Seance> seances = BaseDeDonnees.obtenirSeancesContrainte("TRUE");

            assertEquals("Une séance importée", 1, seances.size());

            if (1 == seances.size()) {
                Seance seance = seances.get(0);

                assertEquals("Une séance", seance.titre);
                assertEquals("Description", seance.description);
                assertEquals(debut, seance.dateDebut);
                assertEquals(fin, seance.dateFin);
                assertEquals("Un type", seance.type);
                assertEquals(1, seance.salles.length);
                if (1 == seance.salles.length) {
                    assertEquals("test", seance.salles[0]);
                }
                assertEquals("INFO_S1", seance.promotion);
            }
        }
    }

    @Test
    public void testObtenirSeancesDateSalle() {
        if (Jalon.jalon >= Jalon.JALON_SEANCES) {
            BaseDeDonnees.vider();
            BaseDeDonnees.importerSalle(new Salle("test", 12, 45, 56, 78, "RDC"));
            String[] salles = {"test"};

            BaseDeDonnees.importerSeance(new Seance("Une séance", "Description",
                    new Date(120, 0, 25, 14, 00, 00), new Date(120, 0, 25, 18, 00, 00),
                    "Un type", salles, "INFO_S1", 0)
            );
            BaseDeDonnees.importerSeance(new Seance("Une séance le lendemain", "Description",
                    new Date(120, 0, 26, 14, 00, 00), new Date(120, 0, 26, 18, 00, 00),
                    "Un type", salles, "INFO_S1", 0)
            );

            ArrayList<Seance> seances = BaseDeDonnees.obtenirSeancesSalleDate("test", new Date(120, 0, 25, 14, 00, 00));

            assertEquals("Une séance obtenue", 1, seances.size());

            if (1 == seances.size()) {
                Seance seance = seances.get(0);
                assertEquals("Une séance", seance.titre);
            }
        }
    }

    @Test
    public void testImportEvenement() {
        if (Jalon.jalon >= Jalon.JALON_EVENEMENTS) {
            BaseDeDonnees.vider();
            BaseDeDonnees.importerEvenement(new Evenement("evt", "Evenement", "Un événement"));

            ArrayList<Evenement> evenements = BaseDeDonnees.obtenirEvenements();

            assertEquals(1, evenements.size());

            if (1 == evenements.size()) {
                Evenement evenement = evenements.get(0);

                assertEquals("evt", evenement.nomCourt);
                assertEquals("Evenement", evenement.nom);
                assertEquals("Un événement", evenement.description);

                Integer idEvt = BaseDeDonnees.idDeLevenement("evt");

                assertNotNull(idEvt);

                if (!idEvt.equals(null)) {
                    BaseDeDonnees.importerSalle(new Salle("test", 12, 45, 56, 78, "RDC"));
                    String[] salles = {"test"};
                    BaseDeDonnees.importerSeance(new Seance("une séance de evt", "description",
                            new Date(120, 0, 1, 14, 0, 0), new Date(120, 0, 1, 18, 0, 0),
                            "un cours", salles, "une promotion", idEvt));

                    ArrayList<Seance> seances = BaseDeDonnees.obtenirSeancesEvenement("evt");

                    assertEquals("Récupération des séances de l'événement", 1, seances.size());

                    if (1 == seances.size()) {
                        assertEquals("une séance de evt", seances.get(0).titre);
                    }
                }
            }
        }
    }
}
