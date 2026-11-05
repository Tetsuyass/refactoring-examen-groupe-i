package fr.efrei.residence;

/**
 * Stratégie : toutes les règles qui dépendent du profil de l'étudiant.
 */
public interface StrategieProfil {

    /**
     * @return le motif de refus de l'inscription, ou {@code null} si le profil l'accepte
     */
    String verifierInscription(int echelonBourse, String telephone);

    double appliquerRemise(double loyer, int echelonBourse);

    boolean estSolvable(double ressourcesMensuelles, double loyerChargesComprises);

    double calculerCaution(double loyerHorsCharges);

    double getFraisDossier();

    int getDureePreavis();

    double calculerPenaliteRetard(double loyerChargesComprises);

    boolean recoitCopieEmailEnAnglais();
}
