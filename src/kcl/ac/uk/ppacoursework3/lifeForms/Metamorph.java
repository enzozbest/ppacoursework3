package src.kcl.ac.uk.ppacoursework3.lifeForms;

import javafx.scene.paint.Color;
import src.kcl.ac.uk.ppacoursework3.GUI.SimulatorView;
import src.kcl.ac.uk.ppacoursework3.concurrent.GenerationTracker;
import src.kcl.ac.uk.ppacoursework3.simulation.Field;
import src.kcl.ac.uk.ppacoursework3.simulation.Location;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Predicate;

public class Metamorph extends Cell {

    private final List<Predicate<Cell>> rulset;
    private final GenerationTracker tracker;
    private final Future<?> future;
    private int initialGeneration;


    /**
     * Create a new cell at location in field.
     *
     * @param field    The field currently occupied.
     * @param location The location within the field.
     * @param col
     */
    private Metamorph(Field field, Location location, Color col) {
        super(field, location, col);
        isBasic = false;
        rulset = List.of(cell -> {
                    boolean nextAlive = false;
                    List<Cell> neighbours = cell.getSameType();
                    if (isAlive() && (neighbours.size() == 0)) {
                        nextAlive = true;
                    }
                    if (!isAlive() && neighbours.size() == 8 || neighbours.size() == 3) {
                        nextAlive = true;
                    }

                    return nextAlive;
                }, // B8/S45
                cell -> {
                    boolean nextAlive = false;
                    List<Cell> neighbours = cell.getSameType();
                    if (isAlive() && neighbours.size() != 1) {
                        nextAlive = true;
                    }
                    if (!isAlive() && neighbours.size() == 1) {
                        nextAlive = true;
                    }
                    return nextAlive;
                }); // Rule 2 B1/S1);

        if (SimulatorView.simulator == null) {
            tracker = new GenerationTracker(0, 10);
        } else {
            tracker = new GenerationTracker(SimulatorView.simulator.getGeneration(), 10);
        }
        future = tracker.run();
    }

    public Metamorph(Field field, Location location) {
        this(field, location, Color.AQUA);
    }

    @Override
    public void act() {
        if (future.isDone()) {
            setNextState(rulset.get(1).test(this));
            return;
        }
        setNextState(rulset.get(0).test(this));
    }
}
