package fr.efrei.residence;

public class CalculateurLoyer {


    //Charges
    int charges = 35;
    int chargesColoc = 25;

    /**
     * Loyer mensuel hors charges, options comprises, remise déduite.
     */
    public double calculerLoyer(Logement l, Profil profil, int echelon, boolean parking, boolean menage,
                                boolean laverie, boolean fibre, boolean assurance) {

        //Loyers au mètre carré
        int loyerM2Studio = 14;
        int loyerM2T1 = 13;
        int loyerM2T2 = 12;
        int loyerM2Colocation = 11;

        //Majorations
        int majorationLoyerT1 = 50;
        int majorationLoyerT2 = 100;

        //Seuils
        int seuilMinimumLoyerStudio = 350;

        //Options : coefficients
        double meubleCoeff = 1.15;
        double rdcCoeff = 0.95;
        double menageCoeff = 1.08;
        double menageCollocCoeff = 1.04;

        //Options : seuils fixes
        int parkingPrix = 45;
        int laveriePrix = 12;
        int fibrePrix = 20;
        int assuranceHabitationPrix = 9;

        //remises
        double boursierParEchelon = 0.05;
        double plafondRemiseBourse = 0.30;
        double alternantCoeff = 0.90;

        // loyer de base selon le type
        double loyer;
        if (l.getType().equals(TypeLogement.STUDIO)) {
            loyer = l.getSurface() * loyerM2Studio;
            if (loyer < seuilMinimumLoyerStudio) loyer = seuilMinimumLoyerStudio;
        } else if (l.getType().equals(TypeLogement.T1)) {
            loyer = l.getSurface() * loyerM2T1 + majorationLoyerT1;
        } else if (l.getType().equals(TypeLogement.T2)) {
            loyer = l.getSurface() * loyerM2T2 + majorationLoyerT2;
        } else if (l.getType().equals(TypeLogement.COLOCATION)) {
            loyer = l.getSurface() * loyerM2Colocation / l.getNombrePlaces();
        } else {
            throw new IllegalArgumentException("Type de logement inconnu : " + l.getType());
        }

        if (l.isMeuble()) loyer = loyer * meubleCoeff;
        if (l.getEtage() == 0) loyer = loyer * rdcCoeff;

        // options
        if (menage) {
            if (l.getType().equals(TypeLogement.COLOCATION)) {
                loyer = loyer * menageCollocCoeff; // parties communes déjà nettoyées
            } else {
                loyer = loyer * menageCoeff;
            }
        }
        if (parking) loyer = loyer + parkingPrix;
        if (laverie) loyer = loyer + laveriePrix;
        if (fibre && !l.getType().equals(TypeLogement.COLOCATION)) loyer = loyer + fibrePrix; // fibre incluse en coloc
        if (assurance) loyer = loyer + assuranceHabitationPrix;

        // remise selon le profil
        if (profil.equals(Profil.BOURSIER)) {
            double taux = echelon * boursierParEchelon;
            if (taux > plafondRemiseBourse) taux = plafondRemiseBourse;
            loyer = loyer * (1 - taux);
        } else if (profil.equals(Profil.ALTERNANT)) {
            loyer = loyer * alternantCoeff;
        }

        return Math.round(loyer * 100.0) / 100.0;
    }

    public double calculerCharges(Logement l) {
        if (l.getType().equals(TypeLogement.COLOCATION)) return chargesColoc;
        return charges;
    }
}
