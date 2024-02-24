package src.kcl.ac.uk.ppacoursework3.lifeForms;

import javafx.scene.paint.Color;
import src.kcl.ac.uk.ppacoursework3.simulation.Field;
import src.kcl.ac.uk.ppacoursework3.simulation.Location;


public class Phage extends AbstractParasite {

    /**
     * Create a new Phage with a default colour.
     * The default spawning colour of Phage is Color.BLUE
     *
     * @param field    The field currently occupied.
     * @param location The location within the field.
     */
    public Phage(Field field, Location location) {
        this(field, location, Color.BLUE);
    }

    /**
     * Create a new Phage with a specified colour by calling the superclass constructor
     * Initialise fields and set this cell as a non-basic life form.
     *
     * @param field
     * @param location
     * @param col
     */
    public Phage(Field field, Location location, Color col) {
        super(field, location, col);
        isBasic = false;
    }

    @Override
    public void act() {
        if (!isAlive()) {
            if (!getField().getLivingNeighbours(getLocation()).isEmpty()) {
                setNextState(true); //New cells to try to infect, so it "comes alive".
                return;
            }
        }
        if (getField().getLivingNeighbours(getLocation()).isEmpty()) {
            setNextState(false); //There is nothing to infect, so phage "dies".
            return;
        }
        infectNeighbour(); //Infect neighbour if possible.
    }
}


