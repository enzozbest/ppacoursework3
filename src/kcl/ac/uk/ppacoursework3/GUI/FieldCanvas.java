package src.kcl.ac.uk.ppacoursework3.GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.IOException;

/**
 * Provide a graphical view of the field. This is a custom node for the user interface.
 *
 * @author Jeffery Raphael, Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902)
 * @version 2024.03.01
 */
public class FieldCanvas extends Canvas {

    private static final int GRID_VIEW_SCALING_FACTOR = 6;
    private final int width;
    private final int height;
    private int xScale, yScale;
    GraphicsContext gc;

    /**
     * Create a new FieldView component.
     */
    public FieldCanvas(int height, int width) {
        super(height, width);
        gc = getGraphicsContext2D();
        this.height = height;
        this.width = width;

        //Load custom FXML type for the FieldCanvas class.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("canvas-controller.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
        }
    }


    /**
     * Sets the scale of the grid.
     *
     * @param gridHeight the height of the grid
     * @param gridWidth  the width of the grid
     */
    public void setScale(int gridHeight, int gridWidth) {
        xScale = width / gridWidth;
        yScale = height / gridHeight;

        if (xScale < 1) xScale = GRID_VIEW_SCALING_FACTOR;

        if (yScale < 1) yScale = GRID_VIEW_SCALING_FACTOR;
    }

    /**
     * Paint a rectangle of the given color on the canvas
     */
    public void drawMark(int x, int y, Color color) {
        gc.setFill(color);
        gc.fillRect(x * xScale, y * yScale, xScale - 1, yScale - 1);
    }
}
