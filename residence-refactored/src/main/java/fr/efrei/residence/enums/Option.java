package fr.efrei.residence.enums;

/**
 * Options mensuelles d'un bail. L'ordre de déclaration est l'ordre d'affichage.
 */
public enum Option {
    MENAGE("ménage"),
    PARKING("parking"),
    LAVERIE("laverie"),
    FIBRE("fibre"),
    ASSURANCE("assurance");

    private final String libelle;

    Option(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
