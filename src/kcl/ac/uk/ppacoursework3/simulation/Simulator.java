package src.kcl.ac.uk.ppacoursework3.simulation;

import javafx.scene.paint.Color;
import src.kcl.ac.uk.ppacoursework3.GUI.Field;
import src.kcl.ac.uk.ppacoursework3.GUI.SimulatorView;
import src.kcl.ac.uk.ppacoursework3.lifeForms.Fungus;
import src.kcl.ac.uk.ppacoursework3.lifeForms.LifeForms;
import src.kcl.ac.uk.ppacoursework3.lifeForms.Mycoplasma;

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

    public static final double GRID_SPAWN = 0.80;

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
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Location location = new Location(row, col);
                Cell cell = generateLife(getType(new int[]{40, 40, 20}), location);
                cells.add(cell);
            }
        }
    }

    /**
     * Given a type and a location, replace the cell in that location with an organism of the given type.
     * @param type meaning which organism it will become
     * @param location position in grid where to put this new organism.
     * @return an organism of the specified type
     **/
    private Cell generateLife(LifeForms type, Location location) {
        double spawn = Randomizer.getRandom().nextDouble();
        switch (type) {
            case MYCOPLASMA -> {
                Mycoplasma myco = new Mycoplasma(field, location, Color.ORANGE);
                if (spawn > GRID_SPAWN) myco.setDead();
                return myco;
            }
            case FUNGUS -> {
                Fungus fung = new Fungus(field, location, Color.PURPLE);
                if (spawn > GRID_SPAWN) fung.setDead();
                return fung;
            }
            default -> {
                Cell ret = new Cell(field, location, Color.GREEN) {
                    @Override
                    public void act() {
                        generateLife(mutate(Counter.neighbourTypeCount(this), this), getLocation());
                    }
                };
                ret.setDead();
                return ret;
            }
        }
    }


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

    /**
     * Given an initial set of ratios, choose with a bias of those ratios a type for an organism.
     * @param ratio a set of numbers representing the ratio of each type
     * @return a type of organism
     * */
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
