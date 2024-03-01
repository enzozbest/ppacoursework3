package src.kcl.ac.uk.ppacoursework3.simulation;

import src.kcl.ac.uk.ppacoursework3.lifeForms.Cell;
import src.kcl.ac.uk.ppacoursework3.lifeForms.CellFactory;
import src.kcl.ac.uk.ppacoursework3.lifeForms.LifeForms;
import src.kcl.ac.uk.ppacoursework3.maths.AliasSampler;
import src.kcl.ac.uk.ppacoursework3.utils.Randomizer;

import java.util.ArrayList;
import java.util.List;


/**
 * A Life (Game of Life) simulator, first described by British mathematician
 * John Horton Conway in 1970.
 *
 * @author David J. Barnes, Michael Kölling, Jeffery Raphael, Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902)
 * @version 2024.03.01
 */
public class Simulator {
    private final List<Cell> cells;
    private final Field field;
    private int generation;
    public static final int GRID_WIDTH = 100;
    public static final int GRID_HEIGHT = 80;
    private final CellFactory factory;
    private List<Cell> toAdd;
    private List<Cell> toRemove;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator() {
        this(GRID_HEIGHT, GRID_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     *
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width) {
        cells = new ArrayList<>();
        field = new Field(depth, width);
        factory = CellFactory.getInstance();
        reset();
    }

    /**
     * Run the simulation from its current state for a single generation.
     * Iterate over the whole field updating the state of each life form.
     */
    public void simOneGeneration() {
        toAdd = new ArrayList<>();
        toRemove = new ArrayList<>();
        generation++;

        for (Cell cell : cells) {
            cell.act();
        }

        for (Cell cell : cells) {
            cell.updateState();
        }

        cells.addAll(toAdd);
        cells.removeAll(toRemove);

        toAdd.clear();
        toRemove.clear();
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        generation = 0;
        Randomizer.reset();
        cells.clear();
        populate();
    }

    /**
     * Randomly populate the field with live/dead life forms
     */
    private void populate() {
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Location location = new Location(row, col);
                AliasSampler sampler = new AliasSampler();
                Cell cell = factory.createCell(LifeForms.getByID(sampler.sample()), location, field);
                cells.add(cell);
            }
        }
    }


    /**
     * Delay introduced between the showing of one generation and the next on the GUI.
     *
     * @param millisec The time to pause for, in milliseconds
     */
    public void delay(int millisec) {
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException exception) {
            //wake up
        }
    }

    /**
     * @return the field where a Cell object is located.
     */
    public Field getField() {
        return field;
    }

    /**
     * @return the current generation.
     */
    public int getGeneration() {
        return generation;
    }

    /**
     * @return the list of new cells created to add to the list of current cells.
     */
    public List<Cell> getToAdd() {
        return toAdd;
    }

    /**
     * @return the list of cells to remove from the list of current cells.
     */
    public List<Cell> getToRemove() {
        return toRemove;
    }
}
