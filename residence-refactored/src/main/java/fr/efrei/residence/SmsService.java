package fr.efrei.residence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SmsService implements CanalNotification, CompteurSms {

    //format
    public static final int MINIMUM_CHIFFRES_NUMERO_TEL = 10;
    public static final int LONGUEUR_MAX_SMS = 160;
    public static final int LONGUEUR_TRONCATURE_SMS = 157;

    private int compteurSms = 0;
    private final List<String[]> journal = new ArrayList<>();

    public void envoyerSms(String numero, String message) {
        if (numero == null || numero.length() < MINIMUM_CHIFFRES_NUMERO_TEL) {
            System.out.println("[SMS] numéro invalide : " + numero);
            return;
        }
        if (message.length() > LONGUEUR_MAX_SMS) {
            message = message.substring(0, LONGUEUR_TRONCATURE_SMS) + "...";
        }
        compteurSms++;
        journal.add(new String[]{numero, message});
        System.out.println("[SMS] -> " + numero + " : " + message);
    }

    public List<String[]> getJournal() {
        return Collections.unmodifiableList(journal);
    }

    @Override
    public int nombreSmsEnvoyes() {
        return compteurSms;
    }

    @Override
    public void notifier(Etudiant e, String sujet, String message) {
        envoyerSms(e.getTelephone(), sujet + " - " + message);
    }

    @Override
    public void relancer(Etudiant e, Bail b) {
        envoyerSms(e.getTelephone(), "Campus Vert : " + Utils.euros(b.soldeDu) + " impayés sur " + b.getId());
    }
}
