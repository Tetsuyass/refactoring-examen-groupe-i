package fr.efrei.residence;

public class TarifT2 implements TarifLogement {

    public static final int LOYER_M2 = 12;
    public static final int MAJORATION = 100;

    @Override
    public double loyerDeBase(Logement l) {
        return l.getSurface() * LOYER_M2 + MAJORATION;
    }
}
