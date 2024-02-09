package src.kcl.ac.uk.ppacoursework3.lifeForms;


public enum LifeForms {
    MYCOPLASMA(0), FUNGUS(1), DEFAULT(-1);

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
