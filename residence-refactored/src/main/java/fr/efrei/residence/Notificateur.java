package fr.efrei.residence;

import java.util.Map;

/**
 * Achemine les messages vers le canal préféré de l'étudiant.
 */
public class Notificateur {

    private final Map<Canal, CanalNotification> canaux;

    public Notificateur(Map<Canal, CanalNotification> canaux) {
        this.canaux = canaux;
    }

    public void notifier(Etudiant e, String sujet, String message) {
        Canal canal = e.getCanalPrefere();
        canaux.get(canal).notifier(e, sujet, message);
        // les internationaux reçoivent toujours un email en plus (demande de l'administration)
        if (FabriqueStrategieProfil.pour(e.getProfil()).recoitCopieEmailEnAnglais() && !canal.equals(Canal.EMAIL)) {
            canaux.get(Canal.EMAIL).notifier(e, "[EN] " + sujet, message);
        }
    }

    /**
     * Une relance part uniquement sur le canal préféré : elle n'a pas de copie en anglais.
     */
    public void relancer(Etudiant e, Bail b) {
        canaux.get(e.getCanalPrefere()).relancer(e, b);
    }
}
