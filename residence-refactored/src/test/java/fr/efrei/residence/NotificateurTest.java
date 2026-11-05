package fr.efrei.residence;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotificateurTest {

    /** Canal bouchon qui retient les sujets reçus. */
    private static class CanalEspion implements CanalNotification {

        private final List<String> sujets = new ArrayList<>();
        private int relances = 0;

        @Override
        public void notifier(Etudiant e, String sujet, String message) {
            sujets.add(sujet);
        }

        @Override
        public void relancer(Etudiant e, Bail b) {
            relances++;
        }
    }

    private final CanalEspion email = new CanalEspion();
    private final CanalEspion sms = new CanalEspion();
    private final CanalEspion courrier = new CanalEspion();
    private final Notificateur notificateur =
            new Notificateur(Map.of(Canal.EMAIL, email, Canal.SMS, sms, Canal.COURRIER, courrier));

    private static Etudiant etudiant(Profil profil, Canal canal) {
        return new Etudiant("ETU-1", "Martin", "Alice", "a@mail.fr", "+33601020304", "Rennes", profil, 0, 1500, false, canal);
    }

    @Test
    void messageEnvoyeSurLeCanalPrefere() {
        notificateur.notifier(etudiant(Profil.STANDARD, Canal.SMS), "Sujet", "Message");

        assertEquals(List.of("Sujet"), sms.sujets);
        assertEquals(List.of(), email.sujets);
        assertEquals(List.of(), courrier.sujets);
    }

    @Test
    void internationalHorsEmailRecoitUneCopieEnAnglais() {
        notificateur.notifier(etudiant(Profil.INTERNATIONAL, Canal.COURRIER), "Sujet", "Message");

        assertEquals(List.of("Sujet"), courrier.sujets);
        assertEquals(List.of("[EN] Sujet"), email.sujets);
    }

    @Test
    void internationalParEmailNeRecoitPasDeCopie() {
        notificateur.notifier(etudiant(Profil.INTERNATIONAL, Canal.EMAIL), "Sujet", "Message");

        assertEquals(List.of("Sujet"), email.sujets);
    }

    @Test
    void relanceSurLeCanalPrefereSansCopieEnAnglais() {
        notificateur.relancer(etudiant(Profil.INTERNATIONAL, Canal.SMS), null);

        assertEquals(1, sms.relances);
        assertEquals(0, email.relances);
        assertEquals(List.of(), email.sujets);
    }
}
