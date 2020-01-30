package evenements;

import java.io.File;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class GenerateurTest {

    @Rule
    public TemporaryFolder outputFolder = new TemporaryFolder();

    @Before
    public void initialisation() {
        BaseDeDonnees.connexion();

        Generateur.valeursMotsCles = new HashMap<>();
        Generateur.aujourdhui = new Date(120, 0, 25, 15, 00, 00);
        Generateur.dossierTemplates = System.getenv("TEMPLATES_DIRECTORY");
        Generateur.adresseSiteWeb = "http://evenements.test/";
        Generateur.dossierSortie = outputFolder.getRoot().getAbsolutePath();
    }

    @Test
    public void listeSallesTest() {
        if (Jalon.jalon >= Jalon.JALON_SALLES_ETAGES) {
            BaseDeDonnees.vider();
            BaseDeDonnees.importerSalle(new Salle("test1", 12, 45, 56, 78, "RDC"));

            String html = Generateur.listeSallesHtml(null);
            assertTrue("Salle test1 présente dans le HTML généré", html.contains("test1"));
        }
    }

    @Test
    public void seancesHtmlTest() {
        if (Jalon.jalon >= Jalon.JALON_SEANCES) {
            ArrayList<Seance> seances = new ArrayList<>();
            String[] salles = {"test"};
            seances.add(new Seance("une séance", "description", new Date(120, 0, 25, 14, 00, 00), new Date(120, 0, 25, 18, 00, 00),
                    "type", salles, "INFO_S1", null));

            String html = Generateur.seancesVersListeHtml(seances);

            assertTrue(html.contains("une séance"));
            assertTrue(html.contains("14:00"));
        }
    }

    protected boolean verifierFichierExiste(String nomFichier) {
        boolean existe = (new File(Generateur.dossierSortie + "/" + nomFichier)).exists();
        assertTrue("Le fichier " + nomFichier + " est généré", existe);

        return existe;
    }

    protected String lireFichier(String nomFichier) {
        try {
            return new String(Files.readAllBytes(Paths.get(Generateur.dossierSortie + "/" + nomFichier)));
        } catch (IOException e) {
            return "";
        }
    }

    @Test
    public void generationPagesSallesTest() {
        if (Jalon.jalon >= Jalon.JALON_SEANCES) {
            BaseDeDonnees.vider();
            BaseDeDonnees.importerSalle(new Salle("test1", 12, 45, 56, 78, "RDC"));

            String[] salles = {"test1"};

            BaseDeDonnees.importerSeance(new Seance("une séance", "description",
                    new Date(120, 1, 25, 14, 00, 00), new Date(120, 1, 25, 18, 00, 00),
                    "type", salles, "INFO_S1", null));
            BaseDeDonnees.importerSeance(new Seance("une séance le lendemain", "description",
                    new Date(120, 1, 26, 14, 00, 00), new Date(120, 1, 26, 18, 00, 00),
                    "type", salles, "INFO_S1", null));
            Generateur.aujourdhui = Seance.chaineVersDate("2020-02-25 14:00:00");
            Generateur.genererPlan();
            Generateur.genererPagesSalles();

            if (verifierFichierExiste("salle-test1.html")) {
                String contenu = lireFichier("salle-test1.html");
                System.out.println(contenu);
                assertTrue("Le nom de la salle est contenu dans sa page générée", contenu.contains("test1"));
                assertTrue("La séance est dans la page générée", contenu.contains("une séance"));
                assertFalse("La séance du lendemain n'est pas dans la page générée", contenu.contains("une séance le lendemain"));

                if (Jalon.jalon >= Jalon.JALON_API_QR_CODES) {
                    assertTrue("Présence du lien QR Code", contenu.contains("qr-salle-test1.png"));
                    verifierFichierExiste("qr-salle-test1.png");
                }
            }

            if (verifierFichierExiste("plan.html")) {
                if (Jalon.jalon >= Jalon.JALON_API_QR_CODES) {
                    String contenu = lireFichier("plan.html");

                    assertTrue("Présence du lien QR Code", contenu.contains("qr-plan.png"));
                    verifierFichierExiste("qr-plan.png");
                }
            }
        }
    }

    @Test
    public void generationPageEvenementsTest() {
        if (Jalon.jalon >= Jalon.JALON_EVENEMENTS) {
            BaseDeDonnees.vider();
            BaseDeDonnees.importerSalle(new Salle("test1", 12, 45, 56, 78, "RDC"));

            String[] salles = {"test1"};

            BaseDeDonnees.importerEvenement(new Evenement("evt", "Evenement", "un événement"));
            BaseDeDonnees.importerSeance(new Seance("une séance de evt", "description",
                    new Date(2020, 01, 25, 14, 00, 00), new Date(2020, 01, 25, 18, 00, 00),
                    "type", salles, "INFO_S1", BaseDeDonnees.idDeLevenement("evt")));

            Generateur.genererPageEvenements();

            if (verifierFichierExiste("evenements.html")) {
                String contenu = lireFichier("evenements.html");
                assertTrue("Présence du nom de l'événement", contenu.contains("Evenement"));
                assertTrue("Présence de la description de l'événement", contenu.contains("un événement"));
                assertTrue("Lien vers la page événement générée", contenu.contains("evenement-evt.html"));
            }

            if (verifierFichierExiste("evenement-evt.html")) {
                String contenu = lireFichier("evenement-evt.html");

                assertTrue("Présence de la séance dans l'événement", contenu.contains("une séance de evt"));

                if (Jalon.jalon >= Jalon.JALON_API_QR_CODES) {
                    assertTrue("Présence du lien QR Code", contenu.contains("qr-evenement-evt.png"));
                    verifierFichierExiste("qr-evenement-evt.png");
                }
            }
        }
    }

    @Test
    public void generationDatesFuturesTest() {
        if (Jalon.jalon >= Jalon.JALON_INTEGRATION_HTML) {
            BaseDeDonnees.vider();
            BaseDeDonnees.importerSalle(new Salle("test1", 12, 45, 56, 78, "RDC"));

            Generateur.genererPagesSalles();

            if (verifierFichierExiste("salle-test1.html")) {
                String contenu = lireFichier("salle-test1.html");
                assertTrue(contenu.contains("salle-test1-2020-01-26.html"));

                if (verifierFichierExiste("salle-test1-2020-01-26.html")) {
                    String contenu2 = lireFichier("salle-test1-2020-01-26.html");

                    assertTrue(contenu2.contains("salle-test1.html"));
                    assertTrue(contenu2.contains("salle-test1-2020-01-27.html"));

                    if (verifierFichierExiste("salle-test1-2020-01-27.html")) {
                        String contenu3 = lireFichier("salle-test1-2020-01-27.html");

                        assertTrue(contenu3.contains("salle-test1-2020-01-26.html"));
                        assertTrue(contenu3.contains("salle-test1-2020-01-28.html"));
                    }
                }
            }
        }
    }

    @Test
    public void generationIcalTest() {
        if (Jalon.jalon >= Jalon.JALON_ICAL) {
            BaseDeDonnees.vider();
            BaseDeDonnees.importerSalle(new Salle("test1", 12, 45, 56, 78, "RDC"));

            String[] salles = {"test1"};

            BaseDeDonnees.importerEvenement(new Evenement("evt", "Evenement", "un événement"));
            BaseDeDonnees.importerSeance(new Seance("une séance de evt", "description",
                    new Date(2020, 01, 25, 14, 00, 00), new Date(2020, 01, 25, 18, 00, 00),
                    "type", salles, "INFO_S1", BaseDeDonnees.idDeLevenement("evt")));

            Generateur.genererPlan();
            Generateur.genererPageEvenements();

            verifierFichierExiste("seances.ics");

            if (verifierFichierExiste("evenement-evt.ics")) {
                String contenu = lireFichier("evenement-evt.ics");

                assertTrue(contenu.contains("BEGIN:VCALENDAR"));
                assertTrue(contenu.contains("BEGIN:VEVENT"));
                assertTrue(contenu.contains("une séance de evt"));
            }
        }
    }
}
