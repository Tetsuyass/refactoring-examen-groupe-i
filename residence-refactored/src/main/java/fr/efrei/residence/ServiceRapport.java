package fr.efrei.residence;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Produit le rapport d'activité de la résidence.
 */
public class ServiceRapport {

    private final LogementRepository logements;
    private final BailRepository baux;
    private final CompteurSms compteurSms;
    private final CompteurTimbres compteurTimbres;

    public ServiceRapport(LogementRepository logements, BailRepository baux, CompteurSms compteurSms,
                          CompteurTimbres compteurTimbres) {
        this.logements = logements;
        this.baux = baux;
        this.compteurSms = compteurSms;
        this.compteurTimbres = compteurTimbres;
    }

    public void afficherRapport() {
        System.out.println();
        System.out.println(genererRapport());
    }

    public String genererRapport() {
        String r = "";
        r += "=============== RAPPORT " + Residence.NOM + " ===============" + "\n";
        // places et occupants par type (1 place par logement, sauf en colocation)
        Map<TypeLogement, Integer> places = new EnumMap<>(TypeLogement.class);
        Map<TypeLogement, Integer> occupants = new EnumMap<>(TypeLogement.class);
        for (TypeLogement type : TypeLogement.values()) {
            places.put(type, 0);
            occupants.put(type, 0);
        }
        double surfaceBalcons = 0;
        for (Logement l : logements.tous()) {
            places.merge(l.getType(), l.getNombrePlaces(), Integer::sum);
            occupants.merge(l.getType(), l.getNombreOccupants(), Integer::sum);
            try {
                surfaceBalcons += l.getSurfaceBalcon();
            } catch (UnsupportedOperationException ex) {
            }
        }
        r += "Studios      : " + occupants.get(TypeLogement.STUDIO) + "/" + places.get(TypeLogement.STUDIO) + "\n";
        r += "T1           : " + occupants.get(TypeLogement.T1) + "/" + places.get(TypeLogement.T1) + "\n";
        r += "T2           : " + occupants.get(TypeLogement.T2) + "/" + places.get(TypeLogement.T2) + "\n";
        r += "Colocation   : " + occupants.get(TypeLogement.COLOCATION) + "/" + places.get(TypeLogement.COLOCATION) + " places" + "\n";
        int total = places.values().stream().mapToInt(Integer::intValue).sum();
        int occupes = occupants.values().stream().mapToInt(Integer::intValue).sum();
        r += "Occupation   : " + Math.round(occupes * 100.0 / total) + " %" + "\n";
        r += "Balcons      : " + surfaceBalcons + " m²" + "\n";

        double loyersMensuels = 0;
        double impayes = 0;
        double cautions = 0;
        List<Bail> tousLesBaux = baux.tous();
        for (Bail b : tousLesBaux) {
            if (b.getStatut().equals(Statut.ACTIF) || b.getStatut().equals(Statut.PREAVIS)) {
                loyersMensuels += b.getLoyerChargesComprises();
                cautions += b.getCaution();
            }
            if (b.soldeDu > 0) impayes += b.soldeDu;
        }
        r += "Loyers CC    : " + Utils.euros(Utils.arrondir(loyersMensuels)) + " / mois" + "\n";
        r += "Cautions     : " + Utils.euros(Utils.arrondir(cautions)) + "\n";
        r += "Impayés      : " + Utils.euros(Utils.arrondir(impayes)) + "\n";
        r += "SMS envoyés  : " + compteurSms.nombreSmsEnvoyes() + "\n";
        r += "Timbres      : " + Utils.euros(Utils.arrondir(compteurTimbres.coutTimbres())) + "\n";
        r += "============================================================";
        return r;
    }
}
