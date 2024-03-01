package src.kcl.ac.uk.ppacoursework3.lifeForms;

import javafx.scene.paint.Color;
import src.kcl.ac.uk.ppacoursework3.GUI.SimulatorView;
import src.kcl.ac.uk.ppacoursework3.concurrent.GenerationTracker;
import src.kcl.ac.uk.ppacoursework3.simulation.Field;
import src.kcl.ac.uk.ppacoursework3.simulation.Location;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Predicate;

/**
 * A class representing a cell that can change its behaviour based on the number of generations it has been alive for.
 * It has two behaviours, one for the first 10 generations, and another for the rest.
 * We keep track of the generation using a GenerationTracker object. This object is created with the current generation
 * and the number of generations to keep track of. It returns a Future object that is used to check if 10 generations
 * have passed. If so, we switch to the second behaviour.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902)
 * @version 2024.03.01
 */
public class Metamorph extends Cell {

    private final List<Predicate<Cell>> rulset;
    private final Future<?> future;

    /**
     * Create a new cell at location in field.
     * <p>
     * Initialise the behaviours with two rules, one for the first 10 generations and another for the rest.
     * The first one is B8/S45 and the second is B1/S1, as can be seen in the code. The rules are implemented as
     * Predicate<Cell> objects, and we store them as a list. In the act() method, we check if 10 generations
     * have passed and choose the correct rules to apply. The act() method only tests the Predicates for the current
     * cell to determine the next state.
     *
     * @param field    The field currently occupied.
     * @param location The location within the field.
     * @param col      The colour of the cell.
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

    /**
     * Create a new cell at location in field with a default colour (Color.AQUA).
     *
     * @param field    The field currently occupied.
     * @param location The location within the field.
     */
    public Metamorph(Field field, Location location) {
        this(field, location, Color.AQUA);
    }

    /**
     * Check if 10 generations have passed. If so, use the second ruleset, if not use the first.
     */
    @Override
    public void act() {
        if (future.isDone()) {
            setNextState(rulset.get(1).test(this));
            return;
        }
        setNextState(rulset.get(0).test(this));
    }
}
