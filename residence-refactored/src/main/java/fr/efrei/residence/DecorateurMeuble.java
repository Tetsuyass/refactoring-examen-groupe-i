package fr.efrei.residence;

/**
 * Logement meublé : ×1,15.
 */
public class DecorateurMeuble extends DecorateurLoyer {

    public static final double COEFFICIENT_MEUBLE = 1.15;

    public DecorateurMeuble(ComposanteLoyer composante) {
        super(composante);
    }

    @Override
    public double montant() {
        return composante.montant() * COEFFICIENT_MEUBLE;
    }
}
