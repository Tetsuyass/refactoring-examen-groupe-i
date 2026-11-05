package fr.efrei.residence;

import fr.efrei.residence.enums.Option;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * Seul endroit qui assemble les couches du loyer, et donc qui en fixe l'ordre.
 */
public class AssembleurLoyer {

    private record CoucheOption(Option option, BiFunction<ComposanteLoyer, TarifLogement, ComposanteLoyer> decorer) {
    }

    /**
     * Couches d'options dans leur ordre d'application : le ménage est un pourcentage, il passe avant les prix fixes.
     * Ajouter une option = créer son décorateur et ajouter une ligne ici.
     */
    private static final List<CoucheOption> COUCHES_OPTIONS = List.of(
            new CoucheOption(Option.MENAGE, DecorateurMenage::new),
            new CoucheOption(Option.PARKING, (loyer, tarif) -> new DecorateurParking(loyer)),
            new CoucheOption(Option.LAVERIE, (loyer, tarif) -> new DecorateurLaverie(loyer)),
            new CoucheOption(Option.FIBRE, DecorateurFibre::new),
            new CoucheOption(Option.ASSURANCE, (loyer, tarif) -> new DecorateurAssurance(loyer))
    );

    /**
     * @return la chaîne complète, dont le montant n'est pas arrondi
     */
    public static ComposanteLoyer assembler(Logement l, Profil profil, int echelon, Set<Option> options) {
        TarifLogement tarif = FabriqueTarifLogement.pour(l.getType());

        ComposanteLoyer loyer = new LoyerDeBase(tarif, l);
        if (l.isMeuble()) loyer = new DecorateurMeuble(loyer);
        if (l.getEtage() == 0) loyer = new DecorateurRezDeChaussee(loyer);

        for (CoucheOption couche : COUCHES_OPTIONS) {
            if (options.contains(couche.option())) loyer = couche.decorer().apply(loyer, tarif);
        }

        return new DecorateurRemise(loyer, FabriqueStrategieProfil.pour(profil), echelon);
    }
}
