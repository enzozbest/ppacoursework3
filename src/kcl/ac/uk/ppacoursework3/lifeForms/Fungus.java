package src.kcl.ac.uk.ppacoursework3.lifeForms;

import javafx.scene.paint.Color;
import src.kcl.ac.uk.ppacoursework3.GUI.Field;
import src.kcl.ac.uk.ppacoursework3.simulation.Cell;
import src.kcl.ac.uk.ppacoursework3.simulation.Location;

import java.util.List;

public class Fungus extends Cell {

    public Fungus(Field field, Location loc, Color color) {
        super(field, loc, color);
        isBasic = false;
    }

    @Override
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
