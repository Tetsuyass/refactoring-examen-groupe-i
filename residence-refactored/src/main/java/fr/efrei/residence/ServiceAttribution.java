package fr.efrei.residence;

import fr.efrei.residence.enums.Option;

import java.time.LocalDate;
import java.util.Set;

/**
 * Attribue à un étudiant le premier logement qui lui convient.
 */
public class ServiceAttribution {

    private final LogementRepository logements;
    private final BailRepository baux;
    private final CalculateurLoyer calculateur;
    private final GenerateurIdentifiants identifiants;
    private final Notificateur notificateur;

    public ServiceAttribution(LogementRepository logements, BailRepository baux, CalculateurLoyer calculateur,
                              GenerateurIdentifiants identifiants, Notificateur notificateur) {
        this.logements = logements;
        this.baux = baux;
        this.calculateur = calculateur;
        this.identifiants = identifiants;
        this.notificateur = notificateur;
    }

    /**
     * @return le bail signé, ou {@code null} si aucun logement ne convient
     */
    public Bail attribuerLogement(Etudiant e, TypeLogement typeSouhaite, Set<Option> options, LocalDate dateSignature) {
        System.out.println("--- Attribution d'un " + typeSouhaite + " à " + e.getNomComplet());
        StrategieProfil strategie = FabriqueStrategieProfil.pour(e.getProfil());
        Logement choisi = null;
        double loyer = 0;
        double charges = 0;
        for (Logement l : logements.tous()) {
            if (!l.getType().equals(typeSouhaite)) continue;
            if (!l.estLibre()) continue;
            if (e.isPmr() && !l.estAccessiblePMR()) continue;

            double loyerCandidat = calculateur.calculerLoyer(l, e.getProfil(), e.getEchelonBourse(), options);
            double chargesCandidat = calculateur.calculerCharges(l);
            double total = loyerCandidat + chargesCandidat;

            if (!strategie.estSolvable(e.getRessourcesMensuelles(), total)) {
                System.out.println("  " + l.getNumero() + " : ressources insuffisantes (" + Utils.euros(total) + "/mois)");
                continue;
            }
            choisi = l;
            loyer = loyerCandidat;
            charges = chargesCandidat;
            break;
        }
        if (choisi == null) {
            System.out.println("  Aucun logement disponible pour " + e.getNomComplet());
            return null;
        }

        if (choisi instanceof Colocation) {
            choisi.ajouterColocataire(e);
        } else {
            choisi.loger(e);
        }

        String id = identifiants.prochainIdBail();
        Bail b = new Bail(id, e, choisi, dateSignature, options);
        b.setLoyerHorsCharges(loyer);
        b.setCharges(charges);

        b.setCaution(Utils.arrondir(strategie.calculerCaution(loyer)));
        b.setFraisDossier(strategie.getFraisDossier());

        baux.sauvegarder(b);
        System.out.println("  Bail " + id + " signé : " + choisi.getNumero() + ", " + Utils.euros(b.getLoyerChargesComprises())
                + " CC/mois, caution " + Utils.euros(b.getCaution()) + ", frais de dossier " + Utils.euros(b.getFraisDossier()));
        notificateur.notifier(e, "Votre bail " + id,
                "Bienvenue à la " + Residence.NOM + " ! Logement " + choisi.getNumero() + ", loyer "
                        + Utils.euros(b.getLoyerChargesComprises()) + " charges comprises.");
        return b;
    }
}
