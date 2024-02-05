package src.kcl.ac.uk.ppacoursework3.simulation;

import javafx.scene.paint.Color;
import src.kcl.ac.uk.ppacoursework3.GUI.Field;
import src.kcl.ac.uk.ppacoursework3.GUI.SimulatorView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


/**
 * A Life (Game of Life) simulator, first described by British mathematician
 * John Horton Conway in 1970.
 *
 * @author David J. Barnes, Michael Kölling & Jeffery Raphael
 * @version 2024.02.03
 */

public class Simulator {

    private final List<Cell> cells;
    private final Field field;
    private int generation;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator() {
        this(SimulatorView.GRID_HEIGHT, SimulatorView.GRID_WIDTH);
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
        reset();
    }

    /**
     * Run the simulation from its current state for a single generation.
     * Iterate over the whole field updating the state of each life form.
     */
    public void simOneGeneration() {
        generation++;
        for (Iterator<Cell> it = cells.iterator(); it.hasNext(); ) {
            Cell cell = it.next();
            cell.act();
        }

        for (Cell cell : cells) {
            cell.updateState();
        }
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        generation = 0;
        cells.clear();
        populate();
    }

    /**
     * Randomly populate the field with live/dead life forms
     */
    private void populate() {
        Random rand = Randomizer.getRandom();
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Location location = new Location(row, col);
                Cell cell = null;
                double probability = rand.nextDouble();
                LifeForms type = getType(probability);
                switch (type) {
                    case MYCOPLASMA -> cell = new Mycoplasma(field, location, Color.ORANGE);
                    case FUNGUS -> cell = new Fungus(field, location, Color.PURPLE);
                    default -> cell = new Mycoplasma(field, location, Color.GREEN);
                }
                if (probability <= type.SPAWN_PROB) {
                    cells.add(cell);
                } else {
                    cell.setDead();
                    cells.add(cell);
                }

            }
        }
    }

    private LifeForms getType(double probability) {
        if (probability <= LifeForms.MYCOPLASMA.SPAWN_PROB) {
            return LifeForms.MYCOPLASMA;
        }
        return LifeForms.FUNGUS;
    }

    /**
     * Pause for a given time.
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

    public Field getField() {
        return field;
    }

    public int getGeneration() {
        return generation;
    }
}
