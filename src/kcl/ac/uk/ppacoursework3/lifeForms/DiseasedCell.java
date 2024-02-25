package src.kcl.ac.uk.ppacoursework3.lifeForms;

import javafx.scene.paint.Color;
import src.kcl.ac.uk.ppacoursework3.GUI.SimulatorView;
import src.kcl.ac.uk.ppacoursework3.concurrent.GenerationTracker;
import src.kcl.ac.uk.ppacoursework3.simulation.Field;
import src.kcl.ac.uk.ppacoursework3.simulation.Location;
import src.kcl.ac.uk.ppacoursework3.utils.Randomizer;

import java.util.concurrent.Future;

/**
 * A DiseasedCell is a cell that has been infected by a Phage or by an infected neighbour.
 * It has a chance to recover from the disease or die after a certain amount of generations.
 * If it recovers, it will spawn a Mycoplasma in its place.
 * If it dies, it will spawn a Phage in its place.
 * This behaviour is non-deterministic and meant to model the way infections happen in the real world: if a cell
 * dies, it releases viral particles that can infect other cells. However, if a cell recovers, it goes back to normal.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902)
 * @version 2024.02.24
 */
public class DiseasedCell extends AbstractParasite {

    private Future<?> future;

    /**
     * Create a new non-deterministic cell at location in field.
     *
     * @param field    The field currently occupied.
     * @param location The location within the field.
     * @param col      The colour of the cell.
     */
    private DiseasedCell(Field field, Location location, Color col) {
        super(field, location, col);
        isBasic = false;
        int diseasedFor = getDiseaseLength();
        tracker = new GenerationTracker(SimulatorView.simulator.getGeneration(), diseasedFor);
    }

    /**
     * Create a new DiseasedCell with a default colour.
     * The default spawning colour of DiseasedCell is Color.YELLOW
     *
     * @param field    The field currently occupied.
     * @param location The location within the field.
     */
    public DiseasedCell(Field field, Location location) {
        this(field, location, Color.YELLOW);
    }

    /**
     * @return a random number between 1 and 5 (inclusive), representing the amount of generations the cell will be diseased for.
     */
    private int getDiseaseLength() {
        return Randomizer.getRandom().nextInt(5) + 1;
    }

    /**
     * This method first ensures the correct amount of time has passed before the cell can recover or die.
     * Once the correct amount of time has indeed passed, the cell will have a 70% chance of recovering. If it recovers,
     * it will spawn a Mycoplasma in its place. If it doesn't, it will spawn a Phage in its place, which can
     * infect other cells.
     * <p>
     * While the cell is diseased, meaning it has not yet recovered or died, it will attempt to infect its neighbours.
     * If the neighbour is a Mycoplasma, it will become a DiseasedCell. Hence, the "ruleset" of this LifeForm is simply to
     * exist for the correct amount of time and then either recover or die. There is no other behaviour carried out by
     * this LifeForm.
     */
    @Override
    public void act() {
        setNextState(true);
        if (future == null) { //Check if this cell is already "recovering" before starting the tracker.
            future = tracker.run();
        }
        if (future.isDone()) {
            if (Randomizer.getRandom().nextDouble() < 0.70) {//Cell "recovers"
                Mycoplasma myco = new Mycoplasma(getField(), getLocation());
                myco.setNextState(true);
                SimulatorView.simulator.getToAdd().add(myco);
                SimulatorView.simulator.getToRemove().add(this);
                getField().place(myco, getLocation());
                return;
            }

            Phage phage = new Phage(getField(), getLocation());
            phage.setNextState(true);
            SimulatorView.simulator.getToAdd().add(phage);
            SimulatorView.simulator.getToRemove().add(this); //Cell "dies"
            getField().place(phage, getLocation()); //Cell "releases" viral particles

        }
        //While cell is alive and diseased, it may infect its Mycoplasma neighbours.
        infectNeighbour();
    }
}