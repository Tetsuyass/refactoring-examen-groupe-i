package fr.efrei.residence;

public class TarifT1 implements TarifLogement {

    public static final int LOYER_M2 = 13;
    public static final int MAJORATION = 50;

    @Override
    public double loyerDeBase(Logement l) {
        return l.getSurface() * LOYER_M2 + MAJORATION;
    }
}
