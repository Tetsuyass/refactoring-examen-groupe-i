package fr.efrei.residence;

import fr.efrei.residence.enums.Option;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        ConnexionBase connexion = new ConnexionBase();
        EtudiantRepository etudiants = new EtudiantRepositoryEnMemoire(connexion);
        BailRepository baux = new BailRepositoryEnMemoire(connexion);
        LogementRepository logements = new LogementRepositoryEnMemoire();
        Residence.chargerCatalogue(logements);

        EmailService email = new EmailService();
        SmsService sms = new SmsService();
        CourrierService courrier = new CourrierService();
        Notificateur notificateur = new Notificateur(
                Map.of(Canal.EMAIL, email, Canal.SMS, sms, Canal.COURRIER, courrier));

        GenerateurIdentifiants identifiants = new GenerateurIdentifiants();
        CalculateurLoyer calculateur = new CalculateurLoyer(new AssembleurLoyer());

        ServiceParc parc = new ServiceParc(logements);
        ServiceInscription inscription = new ServiceInscription(new ValidateurInscription(etudiants), etudiants, identifiants);
        ServiceAttribution attribution = new ServiceAttribution(logements, baux, calculateur, identifiants, notificateur);
        ServiceCycleBail cycleBail = new ServiceCycleBail(baux, notificateur);
        ServiceRelances relances = new ServiceRelances(baux, notificateur);
        ServiceRapport rapport = new ServiceRapport(logements, baux, sms, courrier);
        FormateurQuittance quittances = new FormateurQuittance();

        parc.ajouterLogement(TypeLogement.STUDIO, "C002", 16, 0, false, 0);
        parc.ajouterLogement(TypeLogement.DUPLEX, "C003", 60, 1, true, 0);

        System.out.println();
        Etudiant alice = inscription.inscrireEtudiant("Martin", "Alice", "alice.martin@mail.fr", "0601020304", null,
                Profil.STANDARD, 0, 1500, false, Canal.EMAIL);
        Etudiant bilal = inscription.inscrireEtudiant("Haddad", "Bilal", "bilal.haddad@mail.fr", "0611223344", null,
                Profil.BOURSIER, 5, 450, false, Canal.SMS);
        Etudiant chloe = inscription.inscrireEtudiant("Durand", "Chloé", "chloe.durand@mail.fr", "0622334455", null,
                Profil.ALTERNANT, 0, 1100, true, Canal.EMAIL);
        Etudiant diego = inscription.inscrireEtudiant("Alvarez", "Diego", "diego.alvarez@mail.es", "+34612345678", null,
                Profil.INTERNATIONAL, 0, 3000, false, Canal.SMS);
        Etudiant emma = inscription.inscrireEtudiant("Petit", "Emma", "emma.petit@mail.fr", null,
                "12 rue des Lilas, 35000 Rennes", Profil.STANDARD, 0, 1300, false, Canal.COURRIER);
        Etudiant gaelle = inscription.inscrireEtudiant("Morel", "Gaëlle", "gaelle.morel@mail.fr", "0633445566", null,
                Profil.STANDARD, 0, 1000, false, Canal.EMAIL);
        Etudiant hugo = inscription.inscrireEtudiant("Leroy", "Hugo", "hugo.leroy@mail.fr", "0644556677", null,
                Profil.BOURSIER, 7, 300, true, Canal.EMAIL);
        inscription.inscrireEtudiant("Faure", "Farid", "farid.faure-at-mail.fr", "0655667788", null,
                Profil.STANDARD, 0, 1200, false, Canal.EMAIL);
        inscription.inscrireEtudiant("Wang", "Li", "li.wang@mail.cn", "0612121212", null,
                Profil.INTERNATIONAL, 0, 2000, false, Canal.EMAIL);
        inscription.inscrireEtudiant("Doublon", "Alice", "alice.martin@mail.fr", "0601020304", null,
                Profil.STANDARD, 0, 1500, false, Canal.EMAIL);

        System.out.println();
        LocalDate signature = LocalDate.of(2026, 8, 25);
        Bail bA = attribution.attribuerLogement(alice, TypeLogement.STUDIO, Set.of(Option.PARKING, Option.ASSURANCE), signature);
        Bail bB = attribution.attribuerLogement(bilal, TypeLogement.COLOCATION, Set.of(Option.MENAGE, Option.LAVERIE, Option.FIBRE), signature);
        Bail bC = attribution.attribuerLogement(chloe, TypeLogement.T1, Set.of(Option.LAVERIE, Option.FIBRE, Option.ASSURANCE), signature);
        Bail bD = attribution.attribuerLogement(diego, TypeLogement.T2, Set.of(Option.PARKING, Option.MENAGE, Option.FIBRE, Option.ASSURANCE), signature);
        Bail bE = attribution.attribuerLogement(emma, TypeLogement.COLOCATION, Set.of(Option.FIBRE), signature);
        Bail bG = attribution.attribuerLogement(gaelle, TypeLogement.STUDIO, Set.of(Option.MENAGE, Option.FIBRE), signature);
        Bail bH = attribution.attribuerLogement(hugo, TypeLogement.COLOCATION, Set.of(Option.MENAGE, Option.ASSURANCE), signature);

        System.out.println();
        LocalDate rentree = LocalDate.of(2026, 9, 1);
        cycleBail.entrerDansLesLieux(bA.getId(), rentree);
        cycleBail.entrerDansLesLieux(bB.getId(), rentree);
        cycleBail.entrerDansLesLieux(bC.getId(), rentree);
        cycleBail.entrerDansLesLieux(bD.getId(), rentree);
        cycleBail.entrerDansLesLieux(bE.getId(), LocalDate.of(2026, 8, 20));
        cycleBail.entrerDansLesLieux(bE.getId(), rentree);
        if (bH != null) cycleBail.annulerBail(bH.getId());
        if (bG != null) cycleBail.annulerBail(bG.getId());

        System.out.println();
        cycleBail.payerLoyer(bA.getId(), "2026-09", LocalDate.of(2026, 9, 3), bA.getLoyerChargesComprises());
        cycleBail.payerLoyer(bB.getId(), "2026-09", LocalDate.of(2026, 9, 12), bB.getLoyerChargesComprises());
        cycleBail.payerLoyer(bC.getId(), "2026-09", LocalDate.of(2026, 9, 5), bC.getLoyerChargesComprises());
        cycleBail.payerLoyer(bD.getId(), "2026-09", LocalDate.of(2026, 9, 9), bD.getLoyerChargesComprises());
        cycleBail.payerLoyer(bE.getId(), "2026-09", LocalDate.of(2026, 9, 2), 200);
        cycleBail.payerLoyer(bA.getId(), "2026-10", LocalDate.of(2026, 10, 15), 300);
        cycleBail.payerLoyer(bC.getId(), "2026-10", LocalDate.of(2026, 10, 1), 1000);

        System.out.println();
        System.out.println(quittances.formater(bD, "2026-09"));
        System.out.println(quittances.formater(bA, "2026-10"));

        System.out.println();
        relances.relancerImpayes();

        System.out.println();
        cycleBail.donnerPreavis(bB.getId(), LocalDate.of(2026, 10, 20));
        cycleBail.donnerPreavis(bD.getId(), LocalDate.of(2026, 10, 20));
        cycleBail.donnerPreavis(bE.getId(), LocalDate.of(2026, 10, 20));
        cycleBail.sortir(bD.getId(), LocalDate.of(2026, 11, 5), 0);
        cycleBail.sortir(bB.getId(), LocalDate.of(2026, 11, 5), 40);
        cycleBail.sortir(bD.getId(), LocalDate.of(2026, 11, 20), 120);
        cycleBail.sortir(bE.getId(), LocalDate.of(2026, 11, 20), 30);

        rapport.afficherRapport();
    }
}
