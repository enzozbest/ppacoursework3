package src.kcl.ac.uk.ppacoursework3.lifeForms;

import javafx.scene.paint.Color;
import src.kcl.ac.uk.ppacoursework3.GUI.SimulatorView;
import src.kcl.ac.uk.ppacoursework3.simulation.Field;
import src.kcl.ac.uk.ppacoursework3.simulation.Location;

import java.util.List;

public abstract class AbstractParasite extends Cell {

    /**
     * Create a parasitic cell at a location in field.
     *
     * @param field    The field currently occupied.
     * @param location The location within the field.
     * @param col
     */
    protected AbstractParasite(Field field, Location location, Color col) {
        super(field, location, col);
    }

    protected void infectNeighbour() {
        List<Cell> liveNeighbours = getField().getLivingNeighbours(getLocation());
        for (Cell neighbour : liveNeighbours) {
            if (neighbour instanceof Mycoplasma) { //Check for each neighbour of a Phage if it is a Mycoplasma it can infect.

                //Substitute Phage with a default cell, as if the virus has entered its neighbour.
                Cell basicCell = SimulatorView.simulator.createDefaultCell(getLocation());
                basicCell.setDead();
                getField().place(basicCell, getLocation());
                SimulatorView.simulator.getToAdd().add(basicCell);
                SimulatorView.simulator.getToRemove().add(this);

                //Infect neighbour
                DiseasedCell diseasedCell = new DiseasedCell(getField(), neighbour.getLocation());
                getField().place(diseasedCell, neighbour.getLocation());
                SimulatorView.simulator.getToAdd().add(diseasedCell);

                return;
            }
        }
    }
}
