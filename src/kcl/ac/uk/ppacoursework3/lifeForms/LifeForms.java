package src.kcl.ac.uk.ppacoursework3.lifeForms;

public enum LifeForms {

    MYCOPLASMA(0.25), FUNGUS(0.10), DEFAULT(0);

    public double SPAWN_PROB;

    LifeForms(double SPAWN_PROB) {
        this.SPAWN_PROB = SPAWN_PROB;
    }


}
