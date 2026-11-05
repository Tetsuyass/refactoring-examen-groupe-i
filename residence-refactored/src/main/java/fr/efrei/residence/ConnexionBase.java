package fr.efrei.residence;

/**
 * Connexion à la base de données (simulée sur la console).
 */
public class ConnexionBase {

    public ConnexionBase() {
        System.out.println("[DB] Connexion à jdbc:postgresql://db.campusvert.local:5432/residence");
    }

    public void executer(String sql) {
        System.out.println("[SQL] " + sql);
    }
}
