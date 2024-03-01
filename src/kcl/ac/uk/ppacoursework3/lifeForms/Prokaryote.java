package src.kcl.ac.uk.ppacoursework3.lifeForms;

import javafx.scene.paint.Color;
import src.kcl.ac.uk.ppacoursework3.GUI.SimulatorView;
import src.kcl.ac.uk.ppacoursework3.maths.AliasSampler;
import src.kcl.ac.uk.ppacoursework3.simulation.Field;
import src.kcl.ac.uk.ppacoursework3.simulation.Location;
import src.kcl.ac.uk.ppacoursework3.utils.Counter;

import java.util.HashMap;
import java.util.Objects;

/**
 * A simple model of a prokaryote.
 * Prokaryotes are the simplest form of life, they are single celled organisms that do not have a nucleus.
 * They are the most common form of life on earth and are found in a variety of environments.
 * In this simulation, they are the simplest form of life and their only action is to try to become something else.
 * All Prokaryotes are dead by default when they spawn, and they will not ever come alive.
 * At each step, they will try to become something else, and if they succeed, they will be removed from the field
 * while the new cell will be added to replace them. That new cell will spawn alive and behave like that life form.
 * This new type selection, however, is based on the number of neighbours of each type the Prokaryote has. The more
 * neighbours of a certain type, the more likely it is that the Prokaryote will become that type, but it is not guaranteed.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902)
 * @version 2024.02.24
 */
public class Prokaryote extends Cell {

    private final CellFactory factory;

    /**
     * Create a new cell at location in field.
     *
     * @param field    The field currently occupied.
     * @param location The location within the field.
     * @param col
     */
    private Prokaryote(Field field, Location location, Color col) {
        super(field, location, col);
        factory = CellFactory.getInstance();
        super.isBasic = true;
        setDead();
    }

    /**
     * Create a new cell at location in field with default colour.
     *
     * @param field    The field currently occupied.
     * @param location The location within the field.
     */
    public Prokaryote(Field field, Location location) {
        this(field, location, Color.GREEN);
    }

    /**
     * Create a new cell based on the probabilities returned by the factory. The probabilities are based on the
     * number of neighbours of each type the Prokaryote has. The more neighbours of a certain type, the more likely
     * it is that the Prokaryote will become that type, but it is not guaranteed.
     * If the new cell is a Prokaryote, this cell will be replaced by that new Prokaryote, but in our simulation this is
     * equivalent to doing nothing.
     */
    @Override
    public void act() {
        sampler = new AliasSampler(getProbabilities(Objects.requireNonNull(Counter.neighbourTypeCount(this)), this));

        Cell cell = factory.createCell(LifeForms.getByID(sampler.sample()), getLocation(), getField());
        SimulatorView.simulator.getToAdd().add(cell);
        SimulatorView.simulator.getToRemove().add(this);
        getField().place(cell, getLocation());
    }


    /**
     * Generate an array of biases to be used with the AliasSampler.
     * The probabilities are generated based on how many living neighbours of each type are counted and how many
     * living neighbours a cell could have.
     *
     * @param neighbourCount a HashMap that uses the subtype of Cell as a key to its respective count.
     * @param cell           the cell that is being evaluated to potentially change into a different LifeForm.
     * @return an array of biases for the AliasSampler.
     */
    private double[] getProbabilities(HashMap<Class<? extends Cell>, Integer> neighbourCount, Cell cell) {
        int totalNeighbours = cell.getField().adjacentLocations(cell.getLocation()).size();
        double[] probs = new double[neighbourCount.values().size()];

        for (int i = 0; i < neighbourCount.values().size(); i++) {
            int temp = (int) neighbourCount.values().toArray()[i];
            probs[i] = (double) temp / totalNeighbours;
        }
        return probs;
    }
}
