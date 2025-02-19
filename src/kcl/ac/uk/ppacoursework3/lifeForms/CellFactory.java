package src.kcl.ac.uk.ppacoursework3.lifeForms;

import src.kcl.ac.uk.ppacoursework3.simulation.Field;
import src.kcl.ac.uk.ppacoursework3.simulation.Location;
import src.kcl.ac.uk.ppacoursework3.utils.Randomizer;

import java.lang.reflect.Constructor;

/**
 * This class is responsible for creating new cells. It uses reflection to create new cells based on the LifeForms enum.
 * It also contains a method to generate an array of biases to be used with the AliasSampler, which is used to determine
 * which type of cell a default cell should change into.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902)
 * @version 2024.03.01
 */
@SuppressWarnings("unchecked")
public class CellFactory {
    private static final double GRID_SPAWN = 0.60;
    private static CellFactory factory;

    /**
     * Create a new cell at location in field.
     *
     * @param type  the LifeForms constant representing which subtype of Cell to create.
     * @param loc   the location within the field.
     * @param field the field being simulated.
     * @return a new Cell object of the specified type.
     */
    public Cell createCell(LifeForms type, Location loc, Field field) {
        Cell cell = null;
        String typeName = type.toString().substring(0, 1).toUpperCase() +
                type.toString().substring(1).toLowerCase(); //Convert LifeForms constant into a Class name
        try {
            if (Randomizer.getRandom().nextDouble() > GRID_SPAWN) {
                Cell defaultCell = new Prokaryote(field, loc);
                defaultCell.setDead();
                return defaultCell;
            }

            Constructor<? extends Cell> clazz = (Constructor<? extends Cell>)
                    Class.forName("src.kcl.ac.uk.ppacoursework3.lifeForms." + typeName)
                            .getConstructor(Field.class, Location.class);

            cell = clazz.newInstance(field, loc);

            if (Randomizer.getRandom().nextDouble() > GRID_SPAWN) cell.setDead();

        } catch (Exception e) {
        }
        return cell;
    }

    /**
     * Implementation of the Singleton pattern for the CellFactory with lazy initialisation.
     *
     * @return the singleton instance of the CellFactory.
     */
    public static CellFactory getInstance() {
        if (factory == null) {
            factory = new CellFactory();
        }
        return new CellFactory();
    }
}
