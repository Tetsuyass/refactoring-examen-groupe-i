package fr.efrei.residence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecorateursLoyerTest {

    private static final double DELTA = 1e-9;

    private final ComposanteLoyer cent = () -> 100.0;

    @Test
    void meubleMultiplieParUnVirguleQuinze() {
        assertEquals(115.0, new DecorateurMeuble(cent).montant(), DELTA);
    }

    @Test
    void rezDeChausseeMultiplieParZeroVirguleQuatreVingtQuinze() {
        assertEquals(95.0, new DecorateurRezDeChaussee(cent).montant(), DELTA);
    }

    @Test
    void menageMultiplieParUnVirguleZeroHuit() {
        assertEquals(108.0, new DecorateurMenage(cent, new TarifT1()).montant(), DELTA);
    }

    @Test
    void menageMultiplieParUnVirguleZeroQuatreEnColocation() {
        assertEquals(104.0, new DecorateurMenage(cent, new TarifColocation()).montant(), DELTA);
    }

    @Test
    void parkingAjouteQuaranteCinqEuros() {
        assertEquals(145.0, new DecorateurParking(cent).montant(), DELTA);
    }

    @Test
    void laverieAjouteDouzeEuros() {
        assertEquals(112.0, new DecorateurLaverie(cent).montant(), DELTA);
    }

    @Test
    void fibreAjouteVingtEuros() {
        assertEquals(120.0, new DecorateurFibre(cent, new TarifT1()).montant(), DELTA);
    }

    @Test
    void fibreEstIncluseEnColocation() {
        assertEquals(100.0, new DecorateurFibre(cent, new TarifColocation()).montant(), DELTA);
    }

    @Test
    void assuranceAjouteNeufEuros() {
        assertEquals(109.0, new DecorateurAssurance(cent).montant(), DELTA);
    }

    @Test
    void remiseStandardNeChangeRien() {
        assertEquals(100.0, new DecorateurRemise(cent, new ProfilStandard(), 0).montant(), DELTA);
    }

    @Test
    void remiseAlternantDeDixPourCent() {
        assertEquals(90.0, new DecorateurRemise(cent, new ProfilAlternant(), 0).montant(), DELTA);
    }

    @Test
    void remiseBoursierTransmetLEchelon() {
        assertEquals(85.0, new DecorateurRemise(cent, new ProfilBoursier(), 3).montant(), DELTA);
    }

    @Test
    void remiseBoursierPlafonneeATrentePourCent() {
        assertEquals(70.0, new DecorateurRemise(cent, new ProfilBoursier(), 7).montant(), DELTA);
    }

    @Test
    void lesDecorateursSEmpilent() {
        ComposanteLoyer chaine = new DecorateurParking(new DecorateurMeuble(cent));

        assertEquals(160.0, chaine.montant(), DELTA);
    }
}
