package fr.efrei.residence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Baux conservés en mémoire, indexés par identifiant.
 */
public class BailRepositoryEnMemoire implements BailRepository {

    private final ConnexionBase connexion;
    private final Map<String, Bail> baux = new HashMap<>();

    public BailRepositoryEnMemoire(ConnexionBase connexion) {
        this.connexion = connexion;
    }

    @Override
    public void sauvegarder(Bail b) {
        connexion.executer("INSERT INTO bail (id, etudiant, logement, statut, loyer) VALUES ('" + b.getId() + "', '" + b.getEtudiant().getId() + "', '" + b.getLogement().getNumero() + "', '" + b.getStatut() + "', " + b.getLoyerChargesComprises() + ")");
        baux.put(b.getId(), b);
    }

    @Override
    public void mettreAJour(Bail b) {
        connexion.executer("UPDATE bail SET statut = '" + b.getStatut() + "', solde = " + b.soldeDu + " WHERE id = '" + b.getId() + "'");
    }

    @Override
    public Bail trouver(String id) {
        connexion.executer("SELECT * FROM bail WHERE id = '" + id + "'");
        return baux.get(id);
    }

    @Override
    public List<Bail> tous() {
        connexion.executer("SELECT * FROM bail");
        List<Bail> liste = new ArrayList<>(baux.values());
        liste.sort((a, b) -> a.getId().compareTo(b.getId()));
        return liste;
    }
}
