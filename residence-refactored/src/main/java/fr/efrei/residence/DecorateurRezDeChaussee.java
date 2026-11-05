package fr.efrei.residence;

/**
 * Rez-de-chaussée : ×0,95.
 */
public class DecorateurRezDeChaussee extends DecorateurLoyer {

    public static final double COEFFICIENT_REZ_DE_CHAUSSEE = 0.95;

    public DecorateurRezDeChaussee(ComposanteLoyer composante) {
        super(composante);
    }

    @Override
    public double montant() {
        return composante.montant() * COEFFICIENT_REZ_DE_CHAUSSEE;
    }
}
