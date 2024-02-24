package src.kcl.ac.uk.ppacoursework3.GUI;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Provide a graphical view of the field. This is a custom node for the user interface.
 *
 * @author Jeffery Raphael, Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902)
 * @version 2024.02.12
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

        if (xScale < 1)
            xScale = GRID_VIEW_SCALING_FACTOR;

        if (yScale < 1)
            yScale = GRID_VIEW_SCALING_FACTOR;
    }

    /**
     * Paint a rectangle of the given color on the canvas
     */
    public void drawMark(int x, int y, Color color) {
        gc.setFill(color);
        gc.fillRect(x * xScale, y * yScale, xScale - 1, yScale - 1);
    }
}
