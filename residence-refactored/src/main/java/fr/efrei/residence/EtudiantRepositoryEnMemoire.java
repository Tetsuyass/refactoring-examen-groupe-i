package fr.efrei.residence;

import java.util.HashMap;
import java.util.Map;

/**
 * Étudiants conservés en mémoire, indexés par email.
 */
public class EtudiantRepositoryEnMemoire implements EtudiantRepository {

    private final ConnexionBase connexion;
    private final Map<String, Etudiant> etudiants = new HashMap<>();

    public EtudiantRepositoryEnMemoire(ConnexionBase connexion) {
        this.connexion = connexion;
    }

    @Override
    public void sauvegarder(Etudiant e) {
        connexion.executer("INSERT INTO etudiant (id, nom, email, profil) VALUES ('" + e.getId() + "', '" + e.getNom() + "', '" + e.getEmail() + "', '" + e.getProfil() + "')");
        etudiants.put(e.getEmail(), e);
    }

    @Override
    public Etudiant trouverParEmail(String email) {
        connexion.executer("SELECT * FROM etudiant WHERE email = '" + email + "'");
        return etudiants.get(email);
    }

    @Override
    public Etudiant trouverParId(String id) {
        connexion.executer("SELECT * FROM etudiant WHERE id = '" + id + "'");
        for (Etudiant e : etudiants.values()) {
            if (e.getId().equals(id)) return e;
        }
        return null;
    }
}
