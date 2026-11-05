package fr.efrei.residence;

/**
 * Option ménage : un pourcentage dont le coefficient dépend du tarif du logement.
 */
public class DecorateurMenage extends DecorateurLoyer {

    private final TarifLogement tarif;

    public DecorateurMenage(ComposanteLoyer composante, TarifLogement tarif) {
        super(composante);
        this.tarif = tarif;
    }

    @Override
    public double montant() {
        return composante.montant() * tarif.coefficientMenage();
    }
}
