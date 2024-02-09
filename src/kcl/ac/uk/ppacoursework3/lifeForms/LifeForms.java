package src.kcl.ac.uk.ppacoursework3.lifeForms;


public enum LifeForms {
    MYCOPLASMA(1), FUNGUS(0), DEFAULT(-1);

    int ID;

    LifeForms(int ID) {
        this.ID = ID;
    }

    public static LifeForms getByID(int id) {
        for (LifeForms type : LifeForms.values()) {
            if (type.ID == id) return type;
        }
        return DEFAULT;
    }


}
