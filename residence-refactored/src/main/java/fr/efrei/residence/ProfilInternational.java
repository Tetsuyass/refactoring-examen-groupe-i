package fr.efrei.residence;

/**
 * International : téléphone international obligatoire, dépôt de 2 mois, frais de dossier de 150 €,
 * copie en anglais par email.
 */
public class ProfilInternational extends ProfilStandard {

    public static final String PREFIXE_TELEPHONE = "+";
    public static final int COEFFICIENT_CAUTION = 2;
    public static final int FRAIS_DOSSIER = 150;

    @Override
    public String verifierInscription(int echelonBourse, String telephone) {
        if (telephone == null || !telephone.startsWith(PREFIXE_TELEPHONE)) {
            return "numéro international (+XX) obligatoire";
        }
        return null;
    }

    @Override
    public double calculerCaution(double loyerHorsCharges) {
        return loyerHorsCharges * COEFFICIENT_CAUTION;
    }

    @Override
    public double getFraisDossier() {
        return FRAIS_DOSSIER;
    }

    @Override
    public boolean recoitCopieEmailEnAnglais() {
        return true;
    }
}
