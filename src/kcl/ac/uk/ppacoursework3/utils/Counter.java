package src.kcl.ac.uk.ppacoursework3.utils;

import src.kcl.ac.uk.ppacoursework3.lifeForms.Cell;

import java.util.HashMap;
import java.util.List;

/**
 * Provide a counter for a participant in the simulation.
 * This includes an identifying string and a count of how
 * many participants of this type currently exist within
 * the simulation.
 *
 * @author David J. Barnes and Michael Kölling, Enzo Bestetti(K23011872), Krystian Augustynowicz(K23000902)
 * @version 2024.02.12
 */
public class Counter {
    private final String name;
    private int count;

    /**
     * Provide a name for one of the simulation types.
     *
     * @param name A class of life
     */
    public Counter(String name) {
        this.name = name;
        count = 0;
    }

    /**
     * Given a cell, count how many living neighbours of each type it has got.
     *
     * @param cell whose neighbours you wish to count.
     * @return a hashmap of cell types mapped to their count.
     **/
    public static HashMap<Class<? extends Cell>, Integer> neighbourTypeCount(Cell cell) {
        if (!cell.isBasic()) {
            return null;
        }
        HashMap<Class<? extends Cell>, Integer> typeCount = new HashMap<>();
        List<Cell> neighbours = cell.getField().getLivingNeighbours(cell.getLocation());

        for (Cell neighbour : neighbours) {
            if (!typeCount.containsKey(neighbour.getClass())) {
                typeCount.put(neighbour.getClass(), 1);
                continue;
            }
            Integer newCount = typeCount.get(neighbour.getClass()) + 1;
            typeCount.put(neighbour.getClass(), newCount);
        }
        return typeCount;
    }
    
    /**
     * @return The current count for this type.
     */
    public int getCount() {
        return count;
    }

    /**
     * Increment the current count by one.
     */
    public void increment() {
        count++;
    }

    /**
     * Reset the current count to zero.
     */
    public void reset() {
        count = 0;
    }
}
