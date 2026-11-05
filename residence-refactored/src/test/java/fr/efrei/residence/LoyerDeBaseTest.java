package fr.efrei.residence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoyerDeBaseTest {

    private static final double DELTA = 1e-9;

    private static double loyerDeBase(TarifLogement tarif, TypeLogement type, double surface, int param) {
        Logement l = FabriqueLogement.creer(type, "X001", surface, 1, false, param);
        return new LoyerDeBase(tarif, l).montant();
    }

    @Test
    void studioAQuatorzeEurosLeMetreCarre() {
        assertEquals(420.0, loyerDeBase(new TarifStudio(), TypeLogement.STUDIO, 30, 0), DELTA);
    }

    @Test
    void studioAuMinimumDeTroisCentCinquanteEuros() {
        assertEquals(350.0, loyerDeBase(new TarifStudio(), TypeLogement.STUDIO, 20, 0), DELTA);
    }

    @Test
    void t1ATreizeEurosLeMetreCarrePlusCinquante() {
        assertEquals(440.0, loyerDeBase(new TarifT1(), TypeLogement.T1, 30, 0), DELTA);
    }

    @Test
    void t2ADouzeEurosLeMetreCarrePlusCent() {
        assertEquals(640.0, loyerDeBase(new TarifT2(), TypeLogement.T2, 45, 0), DELTA);
    }

    @Test
    void colocationDiviseeParLeNombreDePlaces() {
        assertEquals(220.0, loyerDeBase(new TarifColocation(), TypeLogement.COLOCATION, 80, 4), DELTA);
    }
}
