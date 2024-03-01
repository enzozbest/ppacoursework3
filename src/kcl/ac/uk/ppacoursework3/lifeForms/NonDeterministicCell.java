package src.kcl.ac.uk.ppacoursework3.lifeForms;

import javafx.scene.paint.Color;
import src.kcl.ac.uk.ppacoursework3.simulation.Field;
import src.kcl.ac.uk.ppacoursework3.simulation.Location;

import java.util.List;
import java.util.function.Predicate;

/**
 * This class represents a non-deterministic cell in the simulation. It is a subclass of Cell and is used to represent
 * cells that have a non-deterministic behaviour, meaning that they have multiple behaviours that can be performed at
 * any given point in time.
 * <p>
 * Rule-sets are represented as Predicate objects and are stored in a list. Each subtype of NonDeterministicCell
 * has its own behaviours to choose from.
 * For all cells inheriting from this class, their act() method will choose a rule using a biased probability distribution,
 * modelled by the AliasSampler class.
 * The chosen rule Predicate is then tested through the test() method and the result applied to the cell, which
 * determines whether the cell is alive or dead in the next generation.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902)
 * @version 2024.03.01
 */
public abstract class NonDeterministicCell extends Cell {

    protected List<Predicate<Cell>> rules;
    protected Predicate<Cell> currentRule;

    /**
     * Create a new non-deterministic cell at location in field.
     *
     * @param field    The field currently occupied.
     * @param location The location within the field.
     * @param col      The colour of the cell.
     */
    protected NonDeterministicCell(Field field, Location location, Color col) {
        super(field, location, col);
    }

    /**
     * Choose a rule using a biased probability distribution and apply it to the cell.
     * The chosen rule Predicate is tested and the result used as the cell's next state.
     */
    @Override
    public void act() {
        setNextState(currentRule.test(this));
    }
}
