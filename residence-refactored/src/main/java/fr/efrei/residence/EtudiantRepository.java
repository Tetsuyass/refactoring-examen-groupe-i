package fr.efrei.residence;

/**
 * Persistance des étudiants.
 */
public interface EtudiantRepository {

    void sauvegarder(Etudiant e);

    /**
     * @return l'étudiant, ou {@code null} si l'email est inconnu
     */
    Etudiant trouverParEmail(String email);

    /**
     * @return l'étudiant, ou {@code null} si l'identifiant est inconnu
     */
    Etudiant trouverParId(String id);
}
