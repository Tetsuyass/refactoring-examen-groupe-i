package fr.efrei.residence;

/**
 * Règles par défaut : aucune remise, solvabilité 3 × le loyer CC, dépôt d'un mois, préavis de 30 jours.
 */
public class ProfilStandard implements StrategieProfil {

    public static final int COEFFICIENT_SOLVABILITE = 3;
    public static final int FRAIS_DOSSIER = 100;
    public static final int DUREE_PREAVIS = 30;
    public static final double TAUX_PENALITE_RETARD = 0.05;
    public static final int PENALITE_RETARD_MIN = 10;

    @Override
    public String verifierInscription(int echelonBourse, String telephone) {
        return null;
    }

    @Override
    public double appliquerRemise(double loyer, int echelonBourse) {
        return loyer;
    }

    @Override
    public boolean estSolvable(double ressourcesMensuelles, double loyerChargesComprises) {
        return ressourcesMensuelles >= loyerChargesComprises * COEFFICIENT_SOLVABILITE;
    }

    @Override
    public double calculerCaution(double loyerHorsCharges) {
        return loyerHorsCharges;
    }

    @Override
    public double getFraisDossier() {
        return FRAIS_DOSSIER;
    }

    @Override
    public int getDureePreavis() {
        return DUREE_PREAVIS;
    }

    @Override
    public double calculerPenaliteRetard(double loyerChargesComprises) {
        double penalite = loyerChargesComprises * TAUX_PENALITE_RETARD;
        if (penalite < PENALITE_RETARD_MIN) penalite = PENALITE_RETARD_MIN;
        return Utils.arrondir(penalite);
    }

    @Override
    public boolean recoitCopieEmailEnAnglais() {
        return false;
    }
}
