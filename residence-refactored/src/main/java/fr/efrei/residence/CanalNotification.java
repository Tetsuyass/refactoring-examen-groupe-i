package fr.efrei.residence;

/**
 * Un canal par lequel on peut joindre un étudiant.
 */
public interface CanalNotification {

    void notifier(Etudiant e, String sujet, String message);

    void relancer(Etudiant e, Bail b);
}
