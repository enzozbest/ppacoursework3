package src.kcl.ac.uk.ppacoursework3.lifeForms;

import javafx.scene.paint.Color;
import src.kcl.ac.uk.ppacoursework3.GUI.SimulatorView;
import src.kcl.ac.uk.ppacoursework3.simulation.Field;
import src.kcl.ac.uk.ppacoursework3.simulation.Location;

import java.util.List;

/**
 * A class representing a parasitic cell.
 * Parasitic cells are cells that have the ability to infect other cells.
 * They are able to infect other cells by substituting themselves with a default cell and then
 * placing a diseased cell in their neighbour's location. This simulates the process of a parasite entering a cell.
 * <p></p>
 * Parasitic cells are able to infect only one cell at a time, and in this implementation only if that cell is of
 * the type Mycoplasma. This models real-life parasites that are highly adapted to infect one specific type of cell only.
 * <p></p>
 * Parasitic cells are able to infect only living cells, and only if they neighbour the cell they are trying to infect.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902)
 * @version 2024.02.24
 */
public abstract class AbstractParasite extends Cell {

    protected CellFactory factory;

    /**
     * Create a parasitic cell at a location in field.
     *
     * @param field    The field currently occupied.
     * @param location The location within the field.
     * @param col
     */
    protected AbstractParasite(Field field, Location location, Color col) {
        super(field, location, col);
        factory = new CellFactory();
    }

    /**
     * Try to infect a neighbour cell.
     * This method is called when a parasitic cell is trying to infect a neighbour cell.
     * It checks if the neighbour is a living cell and if it is of the type that the parasite can infect (Mycoplasma).
     * If the neighbour is a living cell and of the type that the parasite can infect, the parasite will infect it.
     */
    protected void infectNeighbour() {
        List<Cell> liveNeighbours = getField().getLivingNeighbours(getLocation());
        for (Cell neighbour : liveNeighbours) {
            if (neighbour instanceof Mycoplasma) { //Check for each neighbour of a Phage if it is a Mycoplasma it can infect.

                //Substitute Phage with a default cell, as if the virus has entered its neighbour.
                Cell basicCell = factory.createCell(LifeForms.PROKARYOTE, getField(), getLocation());
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
