package fr.efrei.residence;

public class CalculateurLoyer {

    //Coefficients
    public static final double COEFFICIENT_MEUBLE = 1.15;
    public static final double COEFFICIENT_REZ_DE_CHAUSSEE = 0.95;

    //Options : prix fixes
    public static final int PRIX_PARKING = 45;
    public static final int PRIX_LAVERIE = 12;
    public static final int PRIX_ASSURANCE_HABITATION = 9;

    /**
     * Loyer mensuel hors charges, options comprises, remise déduite.
     */
    public double calculerLoyer(Logement l, Profil profil, int echelon, boolean parking, boolean menage,
                                boolean laverie, boolean fibre, boolean assurance) {
        TarifLogement tarif = FabriqueTarifLogement.pour(l.getType());

        // loyer de base selon le type
        double loyer = tarif.loyerDeBase(l);

        if (l.isMeuble()) loyer = loyer * COEFFICIENT_MEUBLE;
        if (l.getEtage() == 0) loyer = loyer * COEFFICIENT_REZ_DE_CHAUSSEE;

        // options
        if (menage) loyer = loyer * tarif.coefficientMenage();
        if (parking) loyer = loyer + PRIX_PARKING;
        if (laverie) loyer = loyer + PRIX_LAVERIE;
        if (fibre) loyer = loyer + tarif.prixFibre();
        if (assurance) loyer = loyer + PRIX_ASSURANCE_HABITATION;

        // remise selon le profil
        loyer = FabriqueStrategieProfil.pour(profil).appliquerRemise(loyer, echelon);

        return Math.round(loyer * 100.0) / 100.0;
    }

    public double calculerCharges(Logement l) {
        return FabriqueTarifLogement.pour(l.getType()).charges();
    }
}
