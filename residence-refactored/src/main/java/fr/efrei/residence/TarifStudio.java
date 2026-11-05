package fr.efrei.residence;

public class TarifStudio implements TarifLogement {

    public static final int LOYER_M2 = 14;
    public static final int LOYER_MIN = 350;

    @Override
    public double loyerDeBase(Logement l) {
        double loyer = l.getSurface() * LOYER_M2;
        if (loyer < LOYER_MIN) loyer = LOYER_MIN;
        return loyer;
    }
}
