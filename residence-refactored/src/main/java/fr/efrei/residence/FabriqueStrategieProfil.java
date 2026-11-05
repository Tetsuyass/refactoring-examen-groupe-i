package fr.efrei.residence;

/**
 * Seul endroit qui choisit la stratégie selon le profil de l'étudiant.
 */
public class FabriqueStrategieProfil {

    public static StrategieProfil pour(Profil profil) {
        return switch (profil) {
            case STANDARD -> new ProfilStandard();
            case BOURSIER -> new ProfilBoursier();
            case ALTERNANT -> new ProfilAlternant();
            case INTERNATIONAL -> new ProfilInternational();
        };
    }
}
