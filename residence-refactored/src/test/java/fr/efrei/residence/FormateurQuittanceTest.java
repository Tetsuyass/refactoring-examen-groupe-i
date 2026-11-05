package fr.efrei.residence;

import fr.efrei.residence.enums.Option;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FormateurQuittanceTest {

    private final FormateurQuittance formateur = new FormateurQuittance();

    private static Bail bail() {
        Etudiant e = new Etudiant("ETU-1", "Martin", "Alice", "a@mail.fr", null, null, Profil.STANDARD, 0, 1500, false, Canal.EMAIL);
        Logement l = FabriqueLogement.creer(TypeLogement.T1, "A104", 28, 1, true, 0);
        Bail b = new Bail("BAIL-001", e, l, LocalDate.of(2026, 8, 25), Set.of(Option.PARKING));
        b.setLoyerHorsCharges(500);
        b.setCharges(35);
        return b;
    }

    @Test
    void moisSansPaiement() {
        assertEquals("Aucun paiement pour 2026-09", formateur.formater(bail(), "2026-09"));
    }

    @Test
    void moisPayeEnTotalite() {
        Bail b = bail();
        b.paiements.add(new Paiement("2026-09", LocalDate.of(2026, 9, 3), 535, 0, 535));

        assertEquals("""
                ==========================================
                   RÉSIDENCE CAMPUS VERT - QUITTANCE
                ==========================================
                Bail      : BAIL-001
                Locataire : Alice MARTIN (STANDARD)
                Logement  : A104 - T1 28.0 m² meublé
                Période   : 2026-09
                Options   : parking
                ------------------------------------------
                Loyer HC  : 500,00 €
                Charges   : 35,00 €
                Total dû  : 535,00 €
                Payé le   : 03/09/2026 : 535,00 €
                ==========================================""", formateur.formater(b, "2026-09"));
    }

    @Test
    void moisPayeEnRetardEtPartiellement() {
        Bail b = bail();
        b.paiements.add(new Paiement("2026-09", LocalDate.of(2026, 9, 12), 300, 26.75, 561.75));

        assertEquals("""
                ==========================================
                   RÉSIDENCE CAMPUS VERT - QUITTANCE
                ==========================================
                Bail      : BAIL-001
                Locataire : Alice MARTIN (STANDARD)
                Logement  : A104 - T1 28.0 m² meublé
                Période   : 2026-09
                Options   : parking
                ------------------------------------------
                Loyer HC  : 500,00 €
                Charges   : 35,00 €
                Pénalité  : 26,75 €
                Total dû  : 561,75 €
                Payé le   : 12/09/2026 : 300,00 €
                RESTE DÛ  : 261,75 €
                ==========================================""", formateur.formater(b, "2026-09"));
    }
}
