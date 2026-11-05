package fr.efrei.residence;

/**
 * Boursier : remise par échelon, garanti par l'État, ni dépôt ni frais de dossier, exonéré de pénalités.
 */
public class ProfilBoursier implements StrategieProfil {

    public static final int ECHELON_MIN = 0;
    public static final int ECHELON_MAX = 7;
    public static final double REMISE_PAR_ECHELON = 0.05;
    public static final double PLAFOND_REMISE = 0.30;
    public static final int DUREE_PREAVIS = 15;

    @Override
    public String verifierInscription(int echelonBourse, String telephone) {
        if (echelonBourse < ECHELON_MIN || echelonBourse > ECHELON_MAX) {
            return "échelon de bourse invalide";
        }
        return null;
    }

    @Override
    public double appliquerRemise(double loyer, int echelonBourse) {
        double taux = echelonBourse * REMISE_PAR_ECHELON;
        if (taux > PLAFOND_REMISE) taux = PLAFOND_REMISE;
        return loyer * (1 - taux);
    }

    @Override
    public boolean estSolvable(double ressourcesMensuelles, double loyerChargesComprises) {
        return true; // garantie Visale
    }

    @Override
    public double calculerCaution(double loyerHorsCharges) {
        return 0;
    }

    @Override
    public double getFraisDossier() {
        return 0;
    }

    @Override
    public int getDureePreavis() {
        return DUREE_PREAVIS;
    }

    @Override
    public double calculerPenaliteRetard(double loyerChargesComprises) {
        return 0;
    }

    @Override
    public boolean recoitCopieEmailEnAnglais() {
        return false;
    }
}
