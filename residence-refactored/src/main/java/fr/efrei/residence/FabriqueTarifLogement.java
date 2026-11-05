package fr.efrei.residence;

/**
 * Seul endroit qui choisit la stratégie de tarif selon le type de logement.
 */
public class FabriqueTarifLogement {

    public static TarifLogement pour(TypeLogement type) {
        return switch (type) {
            case STUDIO -> new TarifStudio();
            case T1 -> new TarifT1();
            case T2 -> new TarifT2();
            case COLOCATION -> new TarifColocation();
            default -> throw new IllegalArgumentException("Type de logement inconnu : " + type);
        };
    }
}
