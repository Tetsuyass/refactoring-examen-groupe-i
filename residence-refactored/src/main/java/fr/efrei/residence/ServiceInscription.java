package fr.efrei.residence;

/**
 * Enregistre les nouveaux étudiants.
 */
public class ServiceInscription {

    private final ValidateurInscription validateur;
    private final EtudiantRepository etudiants;
    private final GenerateurIdentifiants identifiants;

    public ServiceInscription(ValidateurInscription validateur, EtudiantRepository etudiants,
                              GenerateurIdentifiants identifiants) {
        this.validateur = validateur;
        this.etudiants = etudiants;
        this.identifiants = identifiants;
    }

    /**
     * @return l'étudiant inscrit, ou {@code null} si l'inscription est refusée
     */
    public Etudiant inscrireEtudiant(String nom, String prenom, String email, String telephone, String adresse,
                                     Profil profil, int echelonBourse, double ressources, boolean pmr,
                                     Canal canalPrefere) {
        String refus = validateur.motifDeRefus(nom, email, telephone, adresse, profil, echelonBourse, canalPrefere);
        if (refus != null) {
            System.out.println("Inscription refusée : " + refus);
            return null;
        }
        Etudiant e = new Etudiant(identifiants.prochainIdEtudiant(), nom, prenom, email, telephone, adresse, profil,
                echelonBourse, ressources, pmr, canalPrefere);
        etudiants.sauvegarder(e);
        System.out.println("Étudiant inscrit : " + e.getId() + " " + e.getNomComplet() + " (" + profil + ")");
        return e;
    }
}
