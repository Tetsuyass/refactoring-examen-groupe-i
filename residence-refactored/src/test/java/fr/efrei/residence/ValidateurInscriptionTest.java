package fr.efrei.residence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ValidateurInscriptionTest {

    private final EtudiantRepository etudiants = new EtudiantRepositoryEnMemoire(new ConnexionBase());
    private final ValidateurInscription validateur = new ValidateurInscription(etudiants);

    @Test
    void demandeValideSansMotif() {
        assertNull(validateur.motifDeRefus("Martin", "a@mail.fr", null, null, Profil.STANDARD, 0, Canal.EMAIL));
    }

    @Test
    void nomVideRefuse() {
        assertEquals("nom obligatoire",
                validateur.motifDeRefus("  ", "a@mail.fr", null, null, Profil.STANDARD, 0, Canal.EMAIL));
    }

    @Test
    void emailInvalideRefuse() {
        assertEquals("email invalide (a-mail)",
                validateur.motifDeRefus("Martin", "a-mail", null, null, Profil.STANDARD, 0, Canal.EMAIL));
    }

    @Test
    void emailDejaInscritRefuse() {
        etudiants.sauvegarder(new Etudiant("ETU-1", "Martin", "Alice", "a@mail.fr", null, null, Profil.STANDARD, 0,
                1500, false, Canal.EMAIL));

        assertEquals("a@mail.fr déjà inscrit",
                validateur.motifDeRefus("Autre", "a@mail.fr", null, null, Profil.STANDARD, 0, Canal.EMAIL));
    }

    @Test
    void reglesDuProfilAppliquees() {
        assertEquals("échelon de bourse invalide",
                validateur.motifDeRefus("Martin", "a@mail.fr", null, null, Profil.BOURSIER, 8, Canal.EMAIL));
    }

    @Test
    void smsSansTelephoneRefuse() {
        assertEquals("téléphone obligatoire pour les SMS",
                validateur.motifDeRefus("Martin", "a@mail.fr", null, null, Profil.STANDARD, 0, Canal.SMS));
    }

    @Test
    void courrierSansAdresseRefuse() {
        assertEquals("adresse obligatoire pour le courrier",
                validateur.motifDeRefus("Martin", "a@mail.fr", null, "", Profil.STANDARD, 0, Canal.COURRIER));
    }
}
