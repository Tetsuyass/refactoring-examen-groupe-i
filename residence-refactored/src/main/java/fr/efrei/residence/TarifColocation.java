package fr.efrei.residence;

/**
 * Colocation : loyer par colocataire, ménage moins cher (parties communes déjà nettoyées), fibre incluse.
 */
public class TarifColocation implements TarifLogement {

    public static final int LOYER_M2 = 11;
    public static final double COEFFICIENT_MENAGE_COLOCATION = 1.04;
    public static final int CHARGES_COLOCATION = 25;

    @Override
    public double loyerDeBase(Logement l) {
        return l.getSurface() * LOYER_M2 / l.getNombrePlaces();
    }

    @Override
    public double coefficientMenage() {
        return COEFFICIENT_MENAGE_COLOCATION;
    }

    @Override
    public double prixFibre() {
        return 0;
    }

    @Override
    public double charges() {
        return CHARGES_COLOCATION;
    }
}
