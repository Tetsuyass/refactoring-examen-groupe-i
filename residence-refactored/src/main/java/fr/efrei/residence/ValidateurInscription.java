package fr.efrei.residence;

/**
 * Décide si une demande d'inscription est recevable.
 */
public class ValidateurInscription {

    private final EtudiantRepository etudiants;

    public ValidateurInscription(EtudiantRepository etudiants) {
        this.etudiants = etudiants;
    }

    /**
     * @return le motif de refus, ou {@code null} si la demande est recevable
     */
    public String motifDeRefus(String nom, String email, String telephone, String adresse, Profil profil,
                               int echelonBourse, Canal canalPrefere) {
        if (nom == null || nom.trim().isEmpty()) {
            return "nom obligatoire";
        }
        if (!Utils.emailValide(email)) {
            return "email invalide (" + email + ")";
        }
        if (etudiants.trouverParEmail(email) != null) {
            return email + " déjà inscrit";
        }
        String refus = FabriqueStrategieProfil.pour(profil).verifierInscription(echelonBourse, telephone);
        if (refus != null) {
            return refus;
        }
        if (canalPrefere == Canal.SMS && telephone == null) {
            return "téléphone obligatoire pour les SMS";
        }
        if (canalPrefere == Canal.COURRIER && (adresse == null || adresse.isEmpty())) {
            return "adresse obligatoire pour le courrier";
        }
        return null;
    }
}
