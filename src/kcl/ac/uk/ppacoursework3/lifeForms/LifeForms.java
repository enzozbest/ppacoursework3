package src.kcl.ac.uk.ppacoursework3.lifeForms;

/**
 * This enum lists all the possible life forms that any cell can take.
 * Each life form is associated with an integer ID.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz(K23000902)
 * @version 2024.02.12
 */
public enum LifeForms {
    
    MYCOPLASMA(1), FUNGUS(0), DEFAULT(-1);

    int ID;

    /**
     * Associate each enum constant with an integer ID.
     *
     * @param ID
     */
    LifeForms(int ID) {
        this.ID = ID;
    }

    /**
     * Return a LifeForms constant based on the provided ID.
     * If there is a match, return that life form.
     * Otherwise, return the DEFAULT life form.
     *
     * @param id the integer identification number you wish to match with a life form
     * @return the Life form associated with the given ID
     */
    public static LifeForms getByID(int id) {
        for (LifeForms type : LifeForms.values()) {
            if (type.ID == id) return type;
        }
        return DEFAULT;
    }
}
