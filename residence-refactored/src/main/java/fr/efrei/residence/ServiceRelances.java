package fr.efrei.residence;

/**
 * Relance les étudiants dont le bail présente un impayé.
 */
public class ServiceRelances {

    private final BailRepository baux;
    private final Notificateur notificateur;

    public ServiceRelances(BailRepository baux, Notificateur notificateur) {
        this.baux = baux;
        this.notificateur = notificateur;
    }

    public void relancerImpayes() {
        System.out.println("--- Relance des impayés");
        for (Bail b : baux.tous()) {
            if (b.soldeDu <= 0) continue;
            if (b.getStatut().equals(Statut.ANNULE)) continue;
            notificateur.relancer(b.getEtudiant(), b);
        }
    }
}
