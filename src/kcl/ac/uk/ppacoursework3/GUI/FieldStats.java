package src.kcl.ac.uk.ppacoursework3.GUI;

import src.kcl.ac.uk.ppacoursework3.lifeForms.Cell;
import src.kcl.ac.uk.ppacoursework3.simulation.Field;
import src.kcl.ac.uk.ppacoursework3.utils.Counter;

import java.util.HashMap;

/**
 * This class collects and provides some statistical data on the state
 * of a field. It is flexible: it will create and maintain a counter
 * for any class of object that is found within the field.
 *
 * @author David J. Barnes and Michael Kölling, Enzo Bestetti (K23011872), Krystian Augustynowicz(K23000902)
 * @version 2024.02.29
 */

public class FieldStats {

    private final HashMap<Class, Counter> counters;
    private boolean countsValid;

    /**
     * Construct a FieldStats object.  Set up a collection for counters for
     * each type of cell that we might find
     */
    public FieldStats() {
        counters = new HashMap<>();
        countsValid = true;
    }

    /**
     * Get details of what is in the field.
     *
     * @return A HashMap containing each life form contained in the field and their respective counter.
     */
    public HashMap<Class, Counter> getPopulationDetails(Field field) {
        if (!countsValid) generateCounts(field);

        return counters;
    }

    /**
     * Invalidate the current set of statistics; reset all
     * counts to zero.
     */
    public void reset() {
        countsValid = false;
        for (Class key : counters.keySet()) {
            Counter count = counters.get(key);
            count.reset();
        }
    }

    /**
     * Increment the count for one class of life
     *
     * @param cellClass The class of cell to increment.
     */
    public void incrementCount(Class cellClass) {
        //Use function computeIfAbsent to create a new counter if the cellClass is not present in the map.
        Counter count = counters.computeIfAbsent(cellClass, c -> new Counter(c.getName()));

        count.increment();
    }

    /**
     * Indicate that a cell count has been completed.
     */
    public void countFinished() {
        countsValid = true;
    }


    /**
     * Generate counts of the number of cells.
     * These are not kept up to date.
     *
     * @param field The field to generate the stats for.
     */
    private void generateCounts(Field field) {
        reset();

        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Cell cell = field.getObjectAt(row, col);
                incrementCount(cell.getClass());
            }
        }

        countsValid = true;
    }
}
