package fr.efrei.residence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Parc de logements conservé en mémoire.
 */
public class LogementRepositoryEnMemoire implements LogementRepository {

    private final List<Logement> logements = new ArrayList<>();

    @Override
    public void ajouter(Logement l) {
        logements.add(l);
    }

    @Override
    public Logement trouver(String numero) {
        for (Logement l : logements) {
            if (l.getNumero().equals(numero)) return l;
        }
        return null;
    }

    @Override
    public List<Logement> tous() {
        return Collections.unmodifiableList(logements);
    }
}
