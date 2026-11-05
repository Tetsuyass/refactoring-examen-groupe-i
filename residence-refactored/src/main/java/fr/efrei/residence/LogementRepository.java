package fr.efrei.residence;

import java.util.List;

/**
 * Le parc de logements.
 */
public interface LogementRepository {

    void ajouter(Logement l);

    /**
     * @return le logement, ou {@code null} si le numéro est inconnu
     */
    Logement trouver(String numero);

    /**
     * @return tous les logements, dans leur ordre d'ajout
     */
    List<Logement> tous();
}
