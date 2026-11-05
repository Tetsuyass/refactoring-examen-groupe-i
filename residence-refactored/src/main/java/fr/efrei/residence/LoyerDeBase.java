package fr.efrei.residence;

/**
 * Le cœur de la chaîne : le loyer de base du logement, selon son tarif.
 */
public class LoyerDeBase implements ComposanteLoyer {

    private final TarifLogement tarif;
    private final Logement logement;

    public LoyerDeBase(TarifLogement tarif, Logement logement) {
        this.tarif = tarif;
        this.logement = logement;
    }

    @Override
    public double montant() {
        return tarif.loyerDeBase(logement);
    }
}
