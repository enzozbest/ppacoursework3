package src.kcl.ac.uk.ppacoursework3.lifeForms;

import javafx.scene.paint.Color;
import src.kcl.ac.uk.ppacoursework3.maths.AliasSampler;
import src.kcl.ac.uk.ppacoursework3.simulation.Field;
import src.kcl.ac.uk.ppacoursework3.simulation.Location;

import java.util.List;
import java.util.function.Predicate;

/**
 * This class represents a non-deterministic cell in the simulation, a life form inspired by the
 * Conus genus of sea snails, which have a non-deterministic pattern on their shells.
 * This class inherits from NonDeterministicCell and as such has a set of rules that can be applied to it at any given point in time.
 * The rules for this life form are represented as Predicate objects and are stored in a list.
 * <p>
 * When a Conus object is created, its rule set is initialised with two rules:
 * 1. If Conus is alive, and it has exactly 4 neighbours, it remains alive in the next generation.
 * 2. If Conus is dead, and it has 2 or more neighbours, it comes alive in the next generation.
 * <p>
 * These are the Predicate objects used by the act() method of the superclass to determine whether this
 * cell is alive or dead in the next generation.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902)
 * @version 2024.02.16
 */
public class Conus extends NonDeterministicCell {

    /**
     * Create a new Conus by calling the private constructor with a default colour.
     * The default spawning colour of Conus is Color.RED
     *
     * @param field The field currently occupied.
     * @param loc   The location within the field.
     */
    public Conus(Field field, Location loc) {
        this(field, loc, Color.RED);
    }

    /**
     * Create a new Conus by calling the superclass constructor.
     *
     * @param field the field of cells
     * @param loc   the location of the cell in the field
     * @param col   the colour of the cell
     */
    private Conus(Field field, Location loc, Color col) {
        super(field, loc, col);
        sampler = new AliasSampler(new double[]{0.5, 0.5}); //Set probabilities for each rule in the set, in order.
        rules = createRuleSet();
        currentRule = rules.get(sampler.sample());
    }

    /**
     * Create the rule set for this life form.
     *
     * @return The rule set as list of Predicate objects that are used to determine whether this cell is alive or dead
     * in the next generation.
     */
    private List<Predicate<Cell>> createRuleSet() {
        return List.of(
                cell -> {
                    boolean nextAlive = false;
                    List<Cell> neighbours = cell.getSameType();
                    if (isAlive() && (neighbours.size() == 2 || neighbours.size() == 3)) {
                        nextAlive = true;
                    }
                    if (!isAlive() && neighbours.size() == 0) {
                        nextAlive = true;
                    }

                    return nextAlive;
                }, // Rule 1 B0/S23
                cell -> {
                    boolean nextAlive = false;
                    List<Cell> neighbours = cell.getSameType();
                    if (isAlive() && neighbours.size() == 7) {
                        nextAlive = true;
                    }
                    if (!isAlive() && neighbours.size() == 1) {
                        nextAlive = true;
                    }
                    return nextAlive;
                } // Rule 2 B1/S7
        );
    }
}
