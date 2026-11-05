package fr.efrei.residence;

/**
 * Une couche qui s'applique sur le montant de la couche qu'elle enveloppe.
 */
public abstract class DecorateurLoyer implements ComposanteLoyer {

    protected final ComposanteLoyer composante;

    protected DecorateurLoyer(ComposanteLoyer composante) {
        this.composante = composante;
    }
}
