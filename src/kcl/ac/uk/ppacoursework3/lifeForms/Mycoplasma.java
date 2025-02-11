package src.kcl.ac.uk.ppacoursework3.lifeForms;

import javafx.scene.paint.Color;
import src.kcl.ac.uk.ppacoursework3.simulation.Field;
import src.kcl.ac.uk.ppacoursework3.simulation.Location;

import java.util.List;

/**
 * Simplest form of life.
 * Fun Fact: Mycoplasma are one of the simplest forms of life.  A type of
 * bacteria, they only have 500-1000 genes! For comparison, fruit flies have
 * about 14,000 genes.
 *
 * @author David J. Barnes, Michael Kölling & Jeffery Raphael, Enzo Bestetti (K23011872),
 * Krystian Augustynowicz (K23000902)
 * @version 2024.03.01
 */
public class Mycoplasma extends Cell {

    /**
     * Create a new Mycoplasma with a default colour.
     * The default spawning colour of Mycoplasma is Color.ORANGE
     *
     * @param field    The field currently occupied.
     * @param location The location within the field.
     */
    public Mycoplasma(Field field, Location location) {
        this(field, location, Color.ORANGE);
    }

    /**
     * Create a new Mycoplasma with a specified colour by calling the superclass constructor.
     * Initialise fields and set this cell as a non-basic life form.
     *
     * @param field    the field of cells this Mycoplasma is in.
     * @param location the location of this Mycoplasma.
     * @param col      the colour of this Mycoplasma.
     */
    private Mycoplasma(Field field, Location location, Color col) {
        super(field, location, col);
        isBasic = false;
    }

    /**
     * Check if the Mycoplasma has enough diseased neighbours to become ill.
     *
     * @return true if more than 3 of its neighbours are diseased, false otherwise.
     */
    private boolean fallIll() {
        int count = 0;
        for (Cell cell : getField().getLivingNeighbours(getLocation())) {
            if (cell instanceof DiseasedCell) {
                count++;
            }
        }
        return count >= 2;
    }

    /**
     * Implement the rules for Mycoplasma's life and death.
     * If the Mycoplasma has more than 2 diseased neighbours, it becomes diseased.
     * The behaviour defined in this method only applies to non-diseased Mycoplasma cells.
     */
    public void act() {
        if (fallIll()) {
            //Cell becomes diseased
            getField().place(new DiseasedCell(getField(), getLocation()), getLocation());
            return;
        }

        List<Cell> neighbours = getSameType();
        setNextState(false);

        if (isAlive()) {
            if (neighbours.size() > 1 && neighbours.size() <= 3) {
                setNextState(true);
            }
        } else if (neighbours.size() == 3) {
            setNextState(true);
        }
    }
}
