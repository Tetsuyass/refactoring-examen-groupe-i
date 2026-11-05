package fr.efrei.residence;

import java.util.List;

/**
 * Persistance des baux.
 */
public interface BailRepository {

    void sauvegarder(Bail b);

    void mettreAJour(Bail b);

    /**
     * @return le bail, ou {@code null} si l'identifiant est inconnu
     */
    Bail trouver(String id);

    /**
     * @return tous les baux, triés par identifiant
     */
    List<Bail> tous();
}
