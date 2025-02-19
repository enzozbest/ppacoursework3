package src.kcl.ac.uk.ppacoursework3.lifeForms;

import javafx.scene.paint.Color;
import src.kcl.ac.uk.ppacoursework3.concurrent.GenerationTracker;
import src.kcl.ac.uk.ppacoursework3.maths.AliasSampler;
import src.kcl.ac.uk.ppacoursework3.simulation.Field;
import src.kcl.ac.uk.ppacoursework3.simulation.Location;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A class representing the shared characteristics of all forms of life.
 *
 * @author David J. Barnes, Michael Kölling & Jeffery Raphael, Enzo Bestetti(K23011872), Krystian Augustynowicz(K23000902)
 * @version 2024.03.01
 */
public abstract class Cell {
    protected boolean isBasic;
    protected GenerationTracker tracker;
    protected AliasSampler sampler;
    private boolean alive;
    private boolean nextAlive; // The state of the cell in the next iteration
    private final Field field;
    private Location location;
    private Color color = Color.WHITE;

    /**
     * Create a new cell at location in field.
     *
     * @param field    The field currently occupied.
     * @param location The location within the field.
     */
    protected Cell(Field field, Location location, Color col) {
        isBasic = true;
        alive = true;
        nextAlive = false;
        this.field = field;
        setLocation(location);
        setColor(col);
    }

    /**
     * Make this cell act - that is: the cell decides it's status in the
     * next generation and/or performs a given function.
     */
    public abstract void act();

    /**
     * Returns the cell's color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Return the cell's location.
     *
     * @return The cell's location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Filter out cells of the same subtype from the list of living neighbours
     *
     * @return a list of cells of the same subtype.
     */
    protected List<Cell> getSameType() {
        Class<? extends Cell> type = this.getClass();
        return field.getLivingNeighbours(location).stream().filter(c -> type.isAssignableFrom(c.getClass())).map(type::cast).distinct().collect(Collectors.toList());
    }

    /**
     * Return the cell's field.
     *
     * @return The cell's field.
     */
    public Field getField() {
        return field;
    }

    /**
     * @return true if the cell is a basic cell, false otherwise.
     */
    public boolean isBasic() {
        return isBasic;
    }

    /**
     * Check whether the cell is alive or not.
     *
     * @return true if the cell is still alive.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Indicate that the cell is no longer alive.
     */
    public void setDead() {
        alive = false;
    }

    /**
     * Indicate that the cell will be alive or dead in the next generation.
     */
    protected void setNextState(boolean value) {
        nextAlive = value;
    }

    /**
     * Changes the color of the cell
     */
    protected void setColor(Color col) {
        color = col;
    }

    /**
     * Place the cell at the new location in the given field.
     *
     * @param location The cell's location.
     */
    protected void setLocation(Location location) {
        this.location = location;
        field.place(this, location);
    }

    /**
     * Changes the state of the cell
     */
    public void updateState() {
        alive = nextAlive;
    }
}
