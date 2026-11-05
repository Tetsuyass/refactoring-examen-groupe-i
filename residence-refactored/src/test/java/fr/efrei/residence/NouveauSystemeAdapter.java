package fr.efrei.residence;

import fr.efrei.residence.contrat.Canal;
import fr.efrei.residence.contrat.DemandeAttribution;
import fr.efrei.residence.contrat.DemandeDevis;
import fr.efrei.residence.contrat.DemandeInscription;
import fr.efrei.residence.contrat.DemandeLogement;
import fr.efrei.residence.contrat.EtatBail;
import fr.efrei.residence.contrat.EtatLogement;
import fr.efrei.residence.contrat.Notification;
import fr.efrei.residence.contrat.Option;
import fr.efrei.residence.contrat.StatutBail;
import fr.efrei.residence.contrat.SystemeResidence;
import fr.efrei.residence.contrat.TypeLogement;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;

/**
 * Branche le nouveau code sur les tests de contrat : construction du graphe d'objets et traduction des types neutres.
 */
public class NouveauSystemeAdapter implements SystemeResidence {

    private EtudiantRepository etudiants;
    private BailRepository baux;
    private LogementRepository logements;
    private EmailService email;
    private SmsService sms;
    private CourrierService courrier;
    private CalculateurLoyer calculateur;
    private ServiceParc parc;
    private ServiceInscription inscription;
    private ServiceAttribution attribution;
    private ServiceCycleBail cycleBail;
    private ServiceRelances relances;
    private ServiceRapport serviceRapport;
    private FormateurQuittance quittances;

    @Override
    public void reinitialiser() {
        ConnexionBase connexion = new ConnexionBase();
        etudiants = new EtudiantRepositoryEnMemoire(connexion);
        baux = new BailRepositoryEnMemoire(connexion);
        logements = new LogementRepositoryEnMemoire();
        Residence.chargerCatalogue(logements);

        email = new EmailService();
        sms = new SmsService();
        courrier = new CourrierService();
        Notificateur notificateur = new Notificateur(Map.of(
                fr.efrei.residence.Canal.EMAIL, email,
                fr.efrei.residence.Canal.SMS, sms,
                fr.efrei.residence.Canal.COURRIER, courrier));

        GenerateurIdentifiants identifiants = new GenerateurIdentifiants();
        calculateur = new CalculateurLoyer(new AssembleurLoyer());

        parc = new ServiceParc(logements);
        inscription = new ServiceInscription(new ValidateurInscription(etudiants), etudiants, identifiants);
        attribution = new ServiceAttribution(logements, baux, calculateur, identifiants, notificateur);
        cycleBail = new ServiceCycleBail(baux, notificateur);
        relances = new ServiceRelances(baux, notificateur);
        serviceRapport = new ServiceRapport(logements, baux, sms, courrier);
        quittances = new FormateurQuittance();
    }

    @Override
    public Optional<String> inscrire(DemandeInscription d) {
        Etudiant e = inscription.inscrireEtudiant(d.nom(), d.prenom(), d.email(), d.telephone(), d.adresse(),
                fr.efrei.residence.Profil.valueOf(d.profil().name()), d.echelonBourse(), d.ressourcesMensuelles(), d.pmr(), fr.efrei.residence.Canal.valueOf(d.canalPrefere().name()));
        return Optional.ofNullable(e).map(Etudiant::getId);
    }

    @Override
    public boolean ajouterLogement(DemandeLogement d) {
        int param = d.type() == TypeLogement.COLOCATION ? d.places() : d.surfaceBalcon();
        return parc.ajouterLogement(fr.efrei.residence.TypeLogement.valueOf(d.type().name()), d.numero(), d.surface(), d.etage(), d.meuble(), param) != null;
    }

    @Override
    public Optional<EtatLogement> logement(String numero) {
        Logement l = logements.trouver(numero);
        if (l == null) {
            return Optional.empty();
        }
        int occupants;
        if (l instanceof Colocation) {
            occupants = l.getColocataires().size();
        } else {
            occupants = l.estLibre() ? 0 : 1;
        }
        return Optional.of(new EtatLogement(l.getNumero(), fr.efrei.residence.contrat.TypeLogement.valueOf(l.getType().name()), l.getNombrePlaces(),
                occupants, l.estAccessiblePMR()));
    }

    @Override
    public OptionalDouble devisLoyer(DemandeDevis d) {
        Logement l = logements.trouver(d.numeroLogement());
        if (l == null) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of(calculateur.calculerLoyer(l, fr.efrei.residence.Profil.valueOf(d.profil().name()), d.echelonBourse(),
                options(d.options())));
    }

    @Override
    public Optional<String> attribuer(DemandeAttribution d) {
        Etudiant e = etudiants.trouverParId(d.refEtudiant());
        if (e == null) {
            return Optional.empty();
        }
        Bail b = attribution.attribuerLogement(e, fr.efrei.residence.TypeLogement.valueOf(d.type().name()), options(d.options()), d.dateSignature());
        return Optional.ofNullable(b).map(Bail::getId);
    }

    @Override
    public Optional<EtatBail> consulter(String idBail) {
        Bail b = baux.trouver(idBail);
        if (b == null) {
            return Optional.empty();
        }
        return Optional.of(new EtatBail(b.getId(), b.getLogement().getNumero(), StatutBail.valueOf(b.getStatut().name()),
                b.getLoyerHorsCharges(), b.getCharges(), b.getLoyerChargesComprises(), b.getCaution(),
                b.getFraisDossier(), b.getDateFin(), b.soldeDu, b.cautionRestituee));
    }

    @Override
    public boolean entrer(String idBail, LocalDate date) {
        return cycleBail.entrerDansLesLieux(idBail, date);
    }

    @Override
    public boolean payer(String idBail, YearMonth mois, LocalDate date, double montant) {
        return cycleBail.payerLoyer(idBail, mois.toString(), date, montant);
    }

    @Override
    public boolean donnerPreavis(String idBail, LocalDate date) {
        return cycleBail.donnerPreavis(idBail, date);
    }

    @Override
    public boolean sortir(String idBail, LocalDate date, double retenuesDegats) {
        return cycleBail.sortir(idBail, date, retenuesDegats);
    }

    @Override
    public boolean annuler(String idBail) {
        return cycleBail.annulerBail(idBail);
    }

    @Override
    public Optional<String> quittance(String idBail, YearMonth mois) {
        return Optional.ofNullable(baux.trouver(idBail)).map(b -> quittances.formater(b, mois.toString()));
    }

    @Override
    public void relancerImpayes() {
        relances.relancerImpayes();
    }

    @Override
    public String rapport() {
        return serviceRapport.genererRapport();
    }

    @Override
    public List<Notification> notifications() {
        List<Notification> notifications = new ArrayList<>();
        for (String[] e : email.getJournal()) {
            notifications.add(new Notification(Canal.EMAIL, e[0], e[1]));
        }
        for (String[] s : sms.getJournal()) {
            notifications.add(new Notification(Canal.SMS, s[0], s[1]));
        }
        for (String[] c : courrier.getJournal()) {
            notifications.add(new Notification(Canal.COURRIER, c[0], c[1]));
        }
        return notifications;
    }

    private static Set<fr.efrei.residence.enums.Option> options(Set<Option> options) {
        Set<fr.efrei.residence.enums.Option> traduites = EnumSet.noneOf(fr.efrei.residence.enums.Option.class);
        for (Option option : options) {
            traduites.add(fr.efrei.residence.enums.Option.valueOf(option.name()));
        }
        return traduites;
    }
}
