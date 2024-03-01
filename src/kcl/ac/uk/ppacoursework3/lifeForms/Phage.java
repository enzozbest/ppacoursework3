package src.kcl.ac.uk.ppacoursework3.lifeForms;

import javafx.scene.paint.Color;
import src.kcl.ac.uk.ppacoursework3.simulation.Field;
import src.kcl.ac.uk.ppacoursework3.simulation.Location;

/**
 * A class representing a Phage, a type of intra-cellular obligatory parasite.
 * In real life, phages are a highly adapted type of virus that can only infect bacterial cells. In our project we model
 * this behaviour by allowing a Phage to only infect Mycoplasma cells, which are the only bacterial cell type in our simulation.
 * <p>
 * A Phage's behaviour is basic: it exists only to infect its neighbours, and if it has no neighbours to infect, it dies.
 * This is because in real life viruses cannot live without a host cell.
 * <p>
 * If a Phage is alive, it will try to infect its neighbours. If it is dead, it will come alive as soon as it has
 * neighbours to potentially infect.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902)
 * @version 2024.03.01
 */
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
    private Phage(Field field, Location location, Color col) {
        super(field, location, col);
        isBasic = false;
    }

    /**
     * Check if the cell is alive and if it has any neighbours to infect. If so, try to infect one of them.
     * If it has no neighbours to infect, die.
     * If the cell was originally dead, check if it has any neighbours to infect. If so, come alive and try to infect them
     * in the next generation (provided both still are alive).
     */
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


