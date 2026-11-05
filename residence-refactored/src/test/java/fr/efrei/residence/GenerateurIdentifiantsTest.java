package fr.efrei.residence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenerateurIdentifiantsTest {

    @Test
    void identifiantsEtudiantSequentiels() {
        GenerateurIdentifiants generateur = new GenerateurIdentifiants();

        assertEquals("ETU-1", generateur.prochainIdEtudiant());
        assertEquals("ETU-2", generateur.prochainIdEtudiant());
    }

    @Test
    void identifiantsBailSurTroisChiffres() {
        GenerateurIdentifiants generateur = new GenerateurIdentifiants();

        assertEquals("BAIL-001", generateur.prochainIdBail());
        assertEquals("BAIL-002", generateur.prochainIdBail());
    }

    @Test
    void lesDeuxSequencesSontIndependantes() {
        GenerateurIdentifiants generateur = new GenerateurIdentifiants();

        generateur.prochainIdEtudiant();

        assertEquals("BAIL-001", generateur.prochainIdBail());
    }
}
