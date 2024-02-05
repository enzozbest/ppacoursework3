package src.kcl.ac.uk.ppacoursework3.simulation;

public enum LifeForms {

    MYCOPLASMA(0.25), FUNGUS(0.70), DEFAULT(0);

    double SPAWN_PROB;

    LifeForms(double SPAWN_PROB) {
        this.SPAWN_PROB = SPAWN_PROB;
    }


}
