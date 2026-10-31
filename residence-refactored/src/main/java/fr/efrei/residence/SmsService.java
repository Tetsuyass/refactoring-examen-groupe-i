package fr.efrei.residence;

import java.util.ArrayList;
import java.util.List;

public class SmsService {

    //format
    public static int minimumChiffreNumeroTel = 10;
    public static int minimumCharSMS = 160;
    public static int troncatureSMSNombre = 157;

    public static int compteurSms = 0;
    public static List<String[]> journal = new ArrayList<String[]>();

    public void envoyerSms(String numero, String message) {
        if (numero == null || numero.length() < minimumChiffreNumeroTel) {
            System.out.println("[SMS] numéro invalide : " + numero);
            return;
        }
        if (message.length() > minimumCharSMS) {
            message = message.substring(0, troncatureSMSNombre) + "...";
        }
        compteurSms++;
        journal.add(new String[]{numero, message});
        System.out.println("[SMS] -> " + numero + " : " + message);
    }
}
