package fr.efrei.residence;

/**
 * Ajoute des logements au parc.
 */
public class ServiceParc {

    private final LogementRepository logements;

    public ServiceParc(LogementRepository logements) {
        this.logements = logements;
    }

    /**
     * @return le logement ajouté, ou {@code null} si le numéro existe déjà ou si le type n'est pas géré
     */
    public Logement ajouterLogement(TypeLogement type, String numero, double surface, int etage, boolean meuble, int param) {
        if (logements.trouver(numero) != null) {
            System.out.println("Logement " + numero + " existe déjà");
            return null;
        }
        Logement l = FabriqueLogement.creer(type, numero, surface, etage, meuble, param);
        if (l == null) {
            System.out.println("Type de logement inconnu : " + type);
            return null;
        }
        logements.ajouter(l);
        System.out.println("Logement ajouté : " + numero + " (" + type + ")");
        return l;
    }
}
