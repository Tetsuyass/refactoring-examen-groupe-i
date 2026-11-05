package fr.efrei.residence;

import fr.efrei.residence.enums.Option;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BailOptionsTest {

    private static Bail bail(Logement l, Set<Option> options) {
        return new Bail("BAIL-001", null, l, LocalDate.of(2026, 8, 25), options);
    }

    private static Bail bail(Set<Option> options) {
        return bail(null, options);
    }

    @Test
    void sansOptionAfficheAucune() {
        assertEquals("aucune", bail(Set.of()).listerOptions());
    }

    @Test
    void optionsAfficheesDansLOrdreDeDeclaration() {
        Set<Option> desordre = new LinkedHashSet<>(
                List.of(Option.ASSURANCE, Option.FIBRE, Option.LAVERIE, Option.PARKING, Option.MENAGE));

        assertEquals("ménage, parking, laverie, fibre, assurance", bail(desordre).listerOptions());
    }

    @Test
    void fibreListeeMemeEnColocation() {
        Logement colocation = FabriqueLogement.creer(TypeLogement.COLOCATION, "B301", 80, 3, true, 4);

        assertEquals("fibre", bail(colocation, Set.of(Option.FIBRE)).listerOptions());
    }

    @Test
    void lesOptionsDuBailNeSuiventPasLeSetFourni() {
        Set<Option> options = EnumSet.of(Option.PARKING);
        Bail b = bail(options);

        options.add(Option.FIBRE);

        assertEquals(Set.of(Option.PARKING), b.getOptions());
    }

    @Test
    void lesOptionsNeSontPasModifiablesDepuisLExterieur() {
        Set<Option> options = bail(Set.of(Option.PARKING)).getOptions();

        assertThrows(UnsupportedOperationException.class, () -> options.add(Option.FIBRE));
    }
}
