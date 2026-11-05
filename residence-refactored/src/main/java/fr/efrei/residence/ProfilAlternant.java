package fr.efrei.residence;

/**
 * Alternant : remise de 10 %, solvabilité 2 × le loyer CC, préavis de 15 jours.
 */
public class ProfilAlternant extends ProfilStandard {

    public static final double COEFFICIENT_REMISE = 0.90;
    public static final int COEFFICIENT_SOLVABILITE = 2;
    public static final int DUREE_PREAVIS = 15;

    @Override
    public double appliquerRemise(double loyer, int echelonBourse) {
        return loyer * COEFFICIENT_REMISE;
    }

    @Override
    public boolean estSolvable(double ressourcesMensuelles, double loyerChargesComprises) {
        return ressourcesMensuelles >= loyerChargesComprises * COEFFICIENT_SOLVABILITE;
    }

    @Override
    public int getDureePreavis() {
        return DUREE_PREAVIS;
    }
}
