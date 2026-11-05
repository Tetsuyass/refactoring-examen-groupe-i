package fr.efrei.residence;

/**
 * Option parking : +45 €.
 */
public class DecorateurParking extends DecorateurLoyer {

    public static final int PRIX_PARKING = 45;

    public DecorateurParking(ComposanteLoyer composante) {
        super(composante);
    }

    @Override
    public double montant() {
        return composante.montant() + PRIX_PARKING;
    }
}
