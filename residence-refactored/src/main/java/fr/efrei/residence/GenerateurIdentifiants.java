package fr.efrei.residence;

/**
 * Attribue les identifiants séquentiels des étudiants et des baux.
 */
public class GenerateurIdentifiants {

    private int compteurEtudiant = 0;
    private int compteurBail = 0;

    public String prochainIdEtudiant() {
        compteurEtudiant++;
        return "ETU-" + compteurEtudiant;
    }

    public String prochainIdBail() {
        compteurBail++;
        return "BAIL-" + String.format("%03d", compteurBail);
    }
}
