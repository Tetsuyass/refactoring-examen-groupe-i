package fr.efrei.residence;

/**
 * Option fibre : un prix fixe qui dépend du tarif du logement.
 */
public class DecorateurFibre extends DecorateurLoyer {

    private final TarifLogement tarif;

    public DecorateurFibre(ComposanteLoyer composante, TarifLogement tarif) {
        super(composante);
        this.tarif = tarif;
    }

    @Override
    public double montant() {
        return composante.montant() + tarif.prixFibre();
    }
}
