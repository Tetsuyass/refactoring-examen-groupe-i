package fr.efrei.residence;

import fr.efrei.residence.enums.Option;

import java.util.Set;

public class CalculateurLoyer {

    /**
     * Loyer mensuel hors charges, options comprises, remise déduite.
     */
    public double calculerLoyer(Logement l, Profil profil, int echelon, Set<Option> options) {
        double loyer = AssembleurLoyer.assembler(l, profil, echelon, options).montant();
        return Math.round(loyer * 100.0) / 100.0;
    }

    public double calculerCharges(Logement l) {
        return FabriqueTarifLogement.pour(l.getType()).charges();
    }
}
