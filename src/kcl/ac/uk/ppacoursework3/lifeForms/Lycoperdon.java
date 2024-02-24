package src.kcl.ac.uk.ppacoursework3.lifeForms;

import javafx.scene.paint.Color;
import src.kcl.ac.uk.ppacoursework3.GUI.SimulatorView;
import src.kcl.ac.uk.ppacoursework3.concurrent.GenerationTracker;
import src.kcl.ac.uk.ppacoursework3.simulation.Field;
import src.kcl.ac.uk.ppacoursework3.simulation.Location;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Lycoperdon is the genus of a puffball fungus, Lycoperdum perlatum. This type of fungus bursts on impact,
 * releasing spores into the air.
 * This class models this behaviour:
 * Once this Life form has more than 3 neighbours it "bursts", which is modelled as a change of colour.
 * After the Lycoperdon has burst, it takes 3 generations for it to refill. Once it is refilled, it can burst again.
 * <p></p>
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz(K23000902)
 * @version 2024.02.13
 */
public class Lycoperdon extends Cell {

    private int colisionCount;

    private GenerationTracker genTracker;

    private Future future;

    /**
     * Create a new Lycoperdon cell with default colour.
     * The spawning colour of the Lycoperdon is Color.PURPLE
     *
     * @param field the field where this cell will spawn
     * @param loc   the location in the grid where the cell will spawn
     */

    public Lycoperdon(Field field, Location loc) {
        this(field, loc, Color.PURPLE);
    }

    /**
     * Calls the superclass constructor to create a Lycoperdon object.
     * Initialise fields and set this cell as non-basic.
     */
    private Lycoperdon(Field field, Location loc, Color color) {
        super(field, loc, color);
        colisionCount = 0;
        isBasic = false;
    }

    /**
     * Rule set for this Life form:
     * 1. If it has more than 3 living neighbours of any type it changes colour to Color.GREEN
     * 2. If Lycoperdon is alive, and it has both more than 1 and fewer than 3 neighbours,
     * it remains alive in the next generation.
     * 3. If Lycoperdon is dead, and it has exactly 3 neighbours, it comes alive in the next generation.
     */
    @Override
    public void act() {
        List<Cell> neighbours = getSameType();
        colisionCount = getField().getLivingNeighbours(getLocation()).size(); //Get the number of "things" touching the Lycoperdon
        setNextState(false); //Set the next state to false by default

        if (!getColor().equals(Color.GREEN)) {
            if (colisionCount >= 3) {
                setColor(Color.GREEN);
                genTracker = new GenerationTracker(SimulatorView.simulator.getGeneration(), 3);
                future = genTracker.run(); //Start the tracking of 3 generations to "refill" the Lycoperdon.
            }
        } else {
            if (future != null && future.isDone()) {
                setColor(Color.PURPLE); //3 generations have passed, the Lycoperdon is refilled.
                future = null;
            }
        }

        if (isAlive()) {
            if (neighbours.size() == 2 || neighbours.size() == 3) {
                setNextState(true);
            }
        } else if (neighbours.size() == 3 || neighbours.size() == 6) {
            setNextState(true);
        } //B36/S23
    }
}
