package fr.efrei.residence;

/**
 * Option assurance habitation : +9 €.
 */
public class DecorateurAssurance extends DecorateurLoyer {

    public static final int PRIX_ASSURANCE_HABITATION = 9;

    public DecorateurAssurance(ComposanteLoyer composante) {
        super(composante);
    }

    @Override
    public double montant() {
        return composante.montant() + PRIX_ASSURANCE_HABITATION;
    }
}
