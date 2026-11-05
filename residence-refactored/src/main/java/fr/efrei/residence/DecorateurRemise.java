package fr.efrei.residence;

/**
 * Remise du profil de l'étudiant, sur le montant de toutes les couches précédentes.
 */
public class DecorateurRemise extends DecorateurLoyer {

    private final StrategieProfil strategie;
    private final int echelon;

    public DecorateurRemise(ComposanteLoyer composante, StrategieProfil strategie, int echelon) {
        super(composante);
        this.strategie = strategie;
        this.echelon = echelon;
    }

    @Override
    public double montant() {
        return strategie.appliquerRemise(composante.montant(), echelon);
    }
}
