package src.kcl.ac.uk.ppacoursework3.simulation;

import javafx.scene.paint.Color;
import src.kcl.ac.uk.ppacoursework3.GUI.Field;
import src.kcl.ac.uk.ppacoursework3.GUI.SimulatorView;
import src.kcl.ac.uk.ppacoursework3.lifeForms.Fungus;
import src.kcl.ac.uk.ppacoursework3.lifeForms.LifeForms;
import src.kcl.ac.uk.ppacoursework3.lifeForms.Mycoplasma;

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

    public static final double GRID_SPAWN = 0.30;

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
            if (cell.isBasic) {
                it.remove();
                continue;
            }
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
                double probability = rand.nextDouble();
                Cell cell = generateLife(getType(probability), location);
                cells.add(cell);
            }
        }
    }

    private Cell generateLife(LifeForms type, Location location) {
        double spawn = (new Random().nextDouble());
        switch (type) {
            case MYCOPLASMA -> {
                Mycoplasma myco = new Mycoplasma(field, location, Color.ORANGE);
                if(spawn > GRID_SPAWN)  myco.setDead();
                return myco;
            }
            case FUNGUS -> {
                Fungus fung = new Fungus(field, location, Color.PURPLE);
                if(spawn > GRID_SPAWN) fung.setDead();
                return fung;
            }
            default -> {
                Cell ret = new Cell(field, location, Color.GREEN) {
                    @Override
                    public void act() {
                        generateLife(getType((new Random()).nextDouble()), getLocation());
                    }
                };
                ret.setDead();
                return ret;
            }
        }
    }

    private LifeForms getType(double probability) {
        if (probability <= LifeForms.FUNGUS.SPAWN_PROB) {
            return LifeForms.FUNGUS;
        }
        if (probability <= LifeForms.MYCOPLASMA.SPAWN_PROB + LifeForms.FUNGUS.SPAWN_PROB) {
            return LifeForms.MYCOPLASMA;
        }

        return LifeForms.DEFAULT;
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
