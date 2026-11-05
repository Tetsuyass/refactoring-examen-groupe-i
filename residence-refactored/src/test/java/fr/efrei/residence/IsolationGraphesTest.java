package fr.efrei.residence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Deux graphes d'objets ne partagent aucun état : c'est ce qui rend {@code reinitialiser()} trivial.
 */
class IsolationGraphesTest {

    private static ServiceInscription nouvelleInscription() {
        EtudiantRepository etudiants = new EtudiantRepositoryEnMemoire(new ConnexionBase());
        return new ServiceInscription(new ValidateurInscription(etudiants), etudiants, new GenerateurIdentifiants());
    }

    private static Etudiant inscrire(ServiceInscription service) {
        return service.inscrireEtudiant("Martin", "Alice", "a@mail.fr", null, null, Profil.STANDARD, 0, 1500, false, Canal.EMAIL);
    }

    @Test
    void lesEtudiantsEtLesCompteursNeSontPasPartages() {
        assertEquals("ETU-1", inscrire(nouvelleInscription()).getId());
        assertEquals("ETU-1", inscrire(nouvelleInscription()).getId());
    }

    @Test
    void lesParcsNeSontPasPartages() {
        LogementRepository premier = new LogementRepositoryEnMemoire();
        LogementRepository second = new LogementRepositoryEnMemoire();

        Residence.chargerCatalogue(premier);
        new ServiceParc(premier).ajouterLogement(TypeLogement.STUDIO, "C002", 16, 0, false, 0);
        Residence.chargerCatalogue(second);

        assertEquals(Residence.CATALOGUE.size() + 1, premier.tous().size());
        assertEquals(Residence.CATALOGUE.size(), second.tous().size());
    }

    @Test
    void lesJournauxDesCanauxNeSontPasPartages() {
        SmsService premier = new SmsService();
        SmsService second = new SmsService();

        premier.envoyerSms("0601020304", "Bonjour");

        assertEquals(1, premier.nombreSmsEnvoyes());
        assertEquals(1, premier.getJournal().size());
        assertEquals(0, second.nombreSmsEnvoyes());
        assertEquals(0, second.getJournal().size());
    }

    @Test
    void lesTimbresNeSontPasPartages() {
        CourrierService premier = new CourrierService();
        CourrierService second = new CourrierService();

        premier.imprimerEtPoster("Alice MARTIN", "Rennes", "Objet", "Texte");

        assertEquals(CourrierService.PRIX_TIMBRE, premier.coutTimbres());
        assertEquals(0, second.coutTimbres());
    }
}
