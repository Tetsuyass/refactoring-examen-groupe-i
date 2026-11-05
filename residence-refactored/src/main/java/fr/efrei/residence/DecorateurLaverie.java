package fr.efrei.residence;

/**
 * Option laverie : +12 €.
 */
public class DecorateurLaverie extends DecorateurLoyer {

    public static final int PRIX_LAVERIE = 12;

    public DecorateurLaverie(ComposanteLoyer composante) {
        super(composante);
    }

    @Override
    public double montant() {
        return composante.montant() + PRIX_LAVERIE;
    }
}
