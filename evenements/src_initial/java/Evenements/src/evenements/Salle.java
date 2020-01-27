package evenements;

import java.util.Objects;

public class Salle {

    /**
     * Nom de la salle
     */
    public String nom;

    /**
     * Coordonnées du point X,Y supérieur gauche dans l'image de l'étage
     */
    public int Xhautgauche, Yhautgauche;

    /**
     * Largeur et hauteur de l'image dans l'image de l'étage
     */
    public int largeur, hauteur;

    /**
     * Nom de l'étage
     */
    String etage;

    /**
     * Instancie la salle
     */
    Salle(String nom, int Xhautgauche, int Yhautgauche, int largeur, int hauteur, String etage) {
        this.nom = nom;
        this.Xhautgauche = Xhautgauche;
        this.Yhautgauche = Yhautgauche;
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.etage = etage;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.nom);
        hash = 47 * hash + this.Xhautgauche;
        hash = 47 * hash + this.Yhautgauche;
        hash = 47 * hash + this.largeur;
        hash = 47 * hash + this.hauteur;
        hash = 47 * hash + Objects.hashCode(this.etage);
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
        final Salle other = (Salle) obj;
        if (this.Xhautgauche != other.Xhautgauche) {
            return false;
        }
        if (this.Yhautgauche != other.Yhautgauche) {
            return false;
        }
        if (this.largeur != other.largeur) {
            return false;
        }
        if (this.hauteur != other.hauteur) {
            return false;
        }
        if (!Objects.equals(this.nom, other.nom)) {
            return false;
        }
        if (!Objects.equals(this.etage, other.etage)) {
            return false;
        }
        return true;
    }
}
