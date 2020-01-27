package evenements;

import java.util.HashMap;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class FormatHtmlTest {

    @Test
    public void testSubstituer() {
        String ligne, souhaite;
        // un petit ensemble de mots-clés à substituer
        HashMap<String, String> valeursMotsCles = new HashMap<>();
        valeursMotsCles.put("[[TEST1]]", "valeur test 1");
        valeursMotsCles.put("[[TEST2]]", "valeur test 2");
        valeursMotsCles.put("[[TEST3]]", "");
        // chaîne vide
        ligne = "";
        souhaite = "";
        assertEquals(souhaite, FormatHtml.substituer(ligne, valeursMotsCles));
        // cas standard
        ligne = "du texte et puis [[TEST2]] et encore du texte";
        souhaite = "du texte et puis valeur test 2 et encore du texte";
        assertEquals(souhaite, FormatHtml.substituer(ligne, valeursMotsCles));
        // deux mots-clés différents sur la même ligne
        ligne = "du texte et puis [[TEST2]] et encore [[TEST1]]";
        souhaite = "du texte et puis valeur test 2 et encore valeur test 1";
        assertEquals(souhaite, FormatHtml.substituer(ligne, valeursMotsCles));
        // mot-clé de valeur vide
        ligne = "du texte et puis [[TEST3]] et encore du texte";
        souhaite = "du texte et puis  et encore du texte";
        assertEquals(souhaite, FormatHtml.substituer(ligne, valeursMotsCles));
    }
}
