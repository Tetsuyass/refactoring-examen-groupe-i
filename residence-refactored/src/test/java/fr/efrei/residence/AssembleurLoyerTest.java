package fr.efrei.residence;

import fr.efrei.residence.enums.Option;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssembleurLoyerTest {

    private static final double DELTA = 1e-9;

    /** Base : 13 × 30 + 50 = 440. */
    private final Logement t1 = FabriqueLogement.creer(TypeLogement.T1, "B105", 30, 1, false, 0);

    /** Base : 13 × 20 + 50 = 310, meublé, au rez-de-chaussée. */
    private final Logement t1MeubleRdc = FabriqueLogement.creer(TypeLogement.T1, "A010", 20, 0, true, 0);

    /** Base : 11 × 80 / 4 = 220. */
    private final Logement colocation = FabriqueLogement.creer(TypeLogement.COLOCATION, "B301", 80, 3, false, 4);

    private final AssembleurLoyer assembleur = new AssembleurLoyer();

    private double montant(Logement l, Profil profil, int echelon, Set<Option> options) {
        return assembleur.assembler(l, profil, echelon, options).montant();
    }

    @Test
    void sansOptionNiRemiseLeLoyerEstLaBase() {
        assertEquals(440.0, montant(t1, Profil.STANDARD, 0, Set.of()), DELTA);
    }

    @Test
    void meubleEtRezDeChausseeSontAppliquesSelonLeLogement() {
        // 310 × 1,15 × 0,95
        assertEquals(338.675, montant(t1MeubleRdc, Profil.STANDARD, 0, Set.of()), DELTA);
    }

    @Test
    void menageAppliqueAvantLesPrixFixes() {
        // 440 × 1,08 + 45 = 520,20 (et non (440 + 45) × 1,08 = 523,80)
        assertEquals(520.2, montant(t1, Profil.STANDARD, 0, Set.of(Option.MENAGE, Option.PARKING)), DELTA);
    }

    @Test
    void remiseAppliqueeSurLeTotal() {
        // (440 + 45) × 0,90 = 436,50 (et non 440 × 0,90 + 45 = 441)
        assertEquals(436.5, montant(t1, Profil.ALTERNANT, 0, Set.of(Option.PARKING)), DELTA);
    }

    @Test
    void toutesLesCouchesDansLOrdre() {
        // (310 × 1,15 × 0,95 × 1,08 + 45 + 12 + 20 + 9) × 0,90
        assertEquals(406.5921, montant(t1MeubleRdc, Profil.ALTERNANT, 0, EnumSet.allOf(Option.class)), DELTA);
    }

    @Test
    void colocationMenageReduitFibreIncluseRemiseBoursierPlafonnee() {
        // (220 × 1,04 + 45 + 0) × 0,70
        Set<Option> options = Set.of(Option.MENAGE, Option.PARKING, Option.FIBRE);

        assertEquals(191.66, montant(colocation, Profil.BOURSIER, 7, options), DELTA);
    }

    @Test
    void lOrdreDuSetNeChangeRien() {
        Set<Option> desordre = new LinkedHashSet<>(
                List.of(Option.ASSURANCE, Option.FIBRE, Option.LAVERIE, Option.PARKING, Option.MENAGE));

        assertEquals(montant(t1, Profil.STANDARD, 0, EnumSet.allOf(Option.class)),
                montant(t1, Profil.STANDARD, 0, desordre));
    }

    @Test
    void laChaineNArronditPasEtLeCalculateurArrondit() {
        Set<Option> options = EnumSet.allOf(Option.class);

        assertEquals(406.5921, montant(t1MeubleRdc, Profil.ALTERNANT, 0, options), DELTA);
        assertEquals(406.59, new CalculateurLoyer(assembleur).calculerLoyer(t1MeubleRdc, Profil.ALTERNANT, 0, options));
    }
}
