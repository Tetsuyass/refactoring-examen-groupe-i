package fr.efrei.residence;

/**
 * Fabrique : seul endroit qui sait créer un logement à partir de son type.
 */
public class FabriqueLogement {

    public static final int PLACES_MIN_COLOCATION = 2;

    /**
     * @param param nombre de places pour une colocation, surface du balcon pour un T2 (ignoré sinon)
     * @return le logement créé, ou {@code null} si le type n'est pas géré
     */
    public static Logement creer(TypeLogement type, String numero, double surface, int etage, boolean meuble, int param) {
        return switch (type) {
            case STUDIO -> new Studio(numero, surface, etage, meuble);
            case T1 -> new T1(numero, surface, etage, meuble);
            case T2 -> new T2(numero, surface, etage, meuble, param);
            case COLOCATION -> new Colocation(numero, surface, etage, meuble, Math.max(param, PLACES_MIN_COLOCATION));
            default -> null;
        };
    }
}
