package fr.efrei.residence;

/**
 * Stratégie : ce qui, dans le prix, dépend du type de logement.
 */
public interface TarifLogement {

    double COEFFICIENT_MENAGE = 1.08;
    int PRIX_FIBRE = 20;
    int CHARGES = 35;

    double loyerDeBase(Logement l);

    default double coefficientMenage() {
        return COEFFICIENT_MENAGE;
    }

    default double prixFibre() {
        return PRIX_FIBRE;
    }

    default double charges() {
        return CHARGES;
    }
}
