package src.kcl.ac.uk.ppacoursework3.simulation;

import javafx.scene.paint.Color;
import src.kcl.ac.uk.ppacoursework3.GUI.Field;
import src.kcl.ac.uk.ppacoursework3.lifeForms.Lycoperdon;
import src.kcl.ac.uk.ppacoursework3.lifeForms.LifeForms;
import src.kcl.ac.uk.ppacoursework3.lifeForms.Mycoplasma;
import src.kcl.ac.uk.ppacoursework3.maths.AliasSampler;

import java.util.*;


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

    public static final int GRID_WIDTH = 100;
    public static final int GRID_HEIGHT = 80;

    public static final double GRID_SPAWN = 0.50;

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
        reset();
    }

    /**
     * Run the simulation from its current state for a single generation.
     * Iterate over the whole field updating the state of each life form.
     */
    public void simOneGeneration() {
        generation++;
        for (Cell cell : cells) {
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
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Location location = new Location(row, col);
                AliasSampler sampler = new AliasSampler();
                Cell cell = generateLife((sampler.getType(sampler.sample())), location);
                cells.add(cell);
            }
        }
    }

    private Cell generateLife(LifeForms type, Location location) {
        double spawn = Randomizer.getRandom().nextDouble();
        switch (type) {
            case MYCOPLASMA -> {
                Mycoplasma myco = new Mycoplasma(field, location);
                if (spawn > GRID_SPAWN) myco.setDead();
                return myco;
            }
            case FUNGUS -> {
                Lycoperdon fung = new Lycoperdon(field, location);
                if (spawn > GRID_SPAWN) fung.setDead();
                return fung;
            }
            default -> {
                Cell ret = createDefaultCell(location);
                return ret;
            }
        }
    }

    private Cell createDefaultCell(Location location) {
        Cell ret = new Cell(field, location, Color.GREEN) {
            @Override
            public void act() {
                AliasSampler sampler = new AliasSampler(getProbabilities(Counter.neighbourTypeCount(this), this));
                generateLife((sampler.getType(sampler.sample())), getLocation());
            }
        };
        ret.setDead();
        return ret;
    }

    private double[] getProbabilities(HashMap<Class<? extends Cell>, Integer> neighbourCount, Cell cell) {
        int totalNeighbours = cell.getField().adjacentLocations(cell.getLocation()).size();
        double[] probs = new double[neighbourCount.values().size()];

        for (int i = 0; i < neighbourCount.values().size(); i++) {
            int temp = (int) neighbourCount.values().toArray()[i];
            probs[i] = (double) temp / totalNeighbours;
        }
        return probs;
    }

    @Deprecated
    public LifeForms mutate(HashMap<Class<? extends Cell>, Integer> typeCount, Cell cell) {
        Random rand = Randomizer.getRandom();
        int chosen = rand.nextInt(field.adjacentLocations(cell.getLocation()).size() + 1);

        if (typeCount.values().isEmpty()) {
            return LifeForms.DEFAULT;
        }

        int[] cumulants = new int[typeCount.keySet().size()];
        for (int i = 0; i < typeCount.values().size(); i++) {
            for (int j = 0; j <= i; j++) {
                cumulants[i] += (int) typeCount.values().toArray()[j];
            }
        }
        switch (typeCount.values().size()) {
            case 1 -> {
                if (chosen < cumulants[0]) return LifeForms.MYCOPLASMA;
            }
            case 2 -> {
                if (chosen < cumulants[0]) return LifeForms.MYCOPLASMA;
                if (chosen < cumulants[1]) return LifeForms.FUNGUS;
            }
        }
        return LifeForms.DEFAULT;
    }

    @Deprecated
    private LifeForms getType(int[] ratio) {
        Random rand = Randomizer.getRandom();
        int chosen = rand.nextInt(Arrays.stream(ratio).sum());

        int[] cumulants = new int[ratio.length];
        for (int i = 0; i < ratio.length; i++) {
            for (int j = 0; j <= i; j++) {
                cumulants[i] += ratio[j];
            }
        }
        if (chosen < cumulants[0]) return LifeForms.FUNGUS;
        if (chosen < cumulants[1]) return LifeForms.MYCOPLASMA;
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
