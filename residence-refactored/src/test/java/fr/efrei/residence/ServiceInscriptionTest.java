package fr.efrei.residence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ServiceInscriptionTest {

    private final EtudiantRepository etudiants = new EtudiantRepositoryEnMemoire(new ConnexionBase());
    private final ServiceInscription service =
            new ServiceInscription(new ValidateurInscription(etudiants), etudiants, new GenerateurIdentifiants());

    private Etudiant inscrire(String email) {
        return service.inscrireEtudiant("Martin", "Alice", email, null, null, Profil.STANDARD, 0, 1500, false, Canal.EMAIL);
    }

    @Test
    void lEtudiantInscritEstEnregistre() {
        Etudiant e = inscrire("a@mail.fr");

        assertEquals(e, etudiants.trouverParId("ETU-1"));
    }

    @Test
    void uneInscriptionRefuseeNeConsommePasDIdentifiant() {
        inscrire("a@mail.fr");

        assertNull(inscrire("invalide"));

        assertEquals("ETU-2", inscrire("b@mail.fr").getId());
    }
}
