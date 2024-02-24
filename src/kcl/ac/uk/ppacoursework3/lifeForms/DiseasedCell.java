package src.kcl.ac.uk.ppacoursework3.lifeForms;

import javafx.scene.paint.Color;
import src.kcl.ac.uk.ppacoursework3.GUI.SimulatorView;
import src.kcl.ac.uk.ppacoursework3.concurrent.GenerationTracker;
import src.kcl.ac.uk.ppacoursework3.simulation.Field;
import src.kcl.ac.uk.ppacoursework3.simulation.Location;
import src.kcl.ac.uk.ppacoursework3.utils.Randomizer;

import java.util.concurrent.Future;

public class DiseasedCell extends AbstractParasite {

    private GenerationTracker tracker;
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
        int diseasedFor = getDiseaseLength();
        tracker = new GenerationTracker(SimulatorView.simulator.getGeneration(), diseasedFor);

    }

    private int getDiseaseLength() {
        return Randomizer.getRandom().nextInt(5);
    }

    @Override
    public void act() {
        setNextState(true);
        if (future == null) { //Check if this cell is already "recovering" before starting the tracker.
            future = tracker.run();
        }
        if (future.isDone()) {
            if (Randomizer.getRandom().nextDouble() < 0.5) {//Cell "recovers"
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
        //While cell is alive, it may infect its Mycoplasma neighbours.
        infectNeighbour();
    }
}