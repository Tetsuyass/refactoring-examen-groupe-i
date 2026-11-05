package fr.efrei.residence;

/**
 * Fabrique : seul endroit qui choisit le service de notification selon le canal.
 */
public class FabriqueNotification {

    public static CanalNotification creer(Canal canal) {
        return switch (canal) {
            case EMAIL -> new EmailService();
            case SMS -> new SmsService();
            case COURRIER -> new CourrierService();
        };
    }
}
