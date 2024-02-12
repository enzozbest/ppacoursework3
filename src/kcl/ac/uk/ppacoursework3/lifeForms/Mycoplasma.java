package src.kcl.ac.uk.ppacoursework3.lifeForms;

import javafx.scene.paint.Color;
import src.kcl.ac.uk.ppacoursework3.GUI.Field;
import src.kcl.ac.uk.ppacoursework3.simulation.Cell;
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
 * @version 2024.02.12
 */

public class Mycoplasma extends Cell {

    /**
     * Create a new Mycoplasma. The default spawning colour of Mycoplasma is Color.ORANGE
     *
     * @param field    The field currently occupied.
     * @param location The location within the field.
     */
    public Mycoplasma(Field field, Location location) {
        this(field,location, Color.ORANGE);
    }

    private Mycoplasma(Field field, Location location, Color col) {
        super(field, location, col);
        isBasic = false;
    }

    /**
     * Rule set for this Life form:
     * 1. If Mycoplasma is alive and it has both more than 1 and fewer than 3 neighbours,
     *    it remains alive in the next generation.
     * 2. If Mycoplasma is dead and it has exactly 3 neighbours, it comes alive in the next generation.
     */
    public void act() {
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
