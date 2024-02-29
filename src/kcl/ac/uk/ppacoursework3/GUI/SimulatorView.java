package src.kcl.ac.uk.ppacoursework3.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import src.kcl.ac.uk.ppacoursework3.concurrent.GenerationTracker;
import src.kcl.ac.uk.ppacoursework3.lifeForms.Cell;
import src.kcl.ac.uk.ppacoursework3.simulation.Field;
import src.kcl.ac.uk.ppacoursework3.simulation.Simulator;

import java.io.IOException;
import java.util.concurrent.Future;

/**
 * A graphical view of the simulation grid. The view displays a rectangle for
 * each location. Each rectangle is colored according to the state of the
 * cell at a given location.
 *
 * @author David J. Barnes, Michael Kölling & Jeffery Raphael, Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902)
 * @version 2024.02.12
 */

public class SimulatorView extends Application {


    private FieldCanvas fieldCanvas;
    private FieldStats stats;
    public static Simulator simulator;
    private static final Color EMPTY_COLOR = Color.WHITE;
    private Future<?> simulationComplete;
    private GUIController controller;

    /**
     * Create a view of the given width and height.
     */

    @Override
    public void start(Stage stage) {
        simulator = new Simulator();
        stats = new FieldStats();
        controller = new GUIController(this);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("simulator-view.fxml"));
        loader.setController(controller);

        try {
            Group root = loader.load();

            BorderPane pane = (BorderPane) root.getChildren().get(0);

            fieldCanvas = new FieldCanvas(600, 600);
            fieldCanvas.setScale(Simulator.GRID_HEIGHT, Simulator.GRID_WIDTH);
            updateCanvas(simulator.getField());

            pane.setCenter(fieldCanvas);
            pane.autosize();


            Scene scene = new Scene(root, GUIController.WIN_WIDTH, GUIController.WIN_HEIGHT);
            stage.setScene(scene);
            stage.setTitle("Game of Life Simulation");

            stage.show();

            //simulate(1000);
        } catch (IOException e) {
            System.out.println("Error loading FXML file" + e.getMessage() + e.getCause() + e.getStackTrace());
        }
    }

    /**
     * Show the current status of the field.
     *
     * @param field The field whose status is to be displayed.
     */
    public void updateCanvas(Field field) {
        stats.reset();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Cell cell = field.getObjectAt(row, col);
                if (cell.isAlive()) {
                    stats.incrementCount(cell.getClass());
                    fieldCanvas.drawMark(col, row, cell.getColor());
                } else {
                    fieldCanvas.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }
        stats.countFinished();
        controller.updateGenerationLabel();
    }


    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        simulator.reset();
    }

    /**
     * Run the simulation from its current state for the given number of
     * generations.  Stop before the given number of generations if the
     * simulation ceases to be viable.
     *
     * @param numGenerations The number of generations to run for.
     */
    @FXML
    public void simulate(int numGenerations) {
        Task<Void> simulation = new Task<>() {
            @Override
            protected Void call() {
                for (int g = 1; g <= numGenerations; g++) {
                    simulator.simOneGeneration();
                    simulator.delay(1000);
                    Platform.runLater(() -> updateCanvas(simulator.getField())); //Updates the GUI
                    simulator.delay(10); //ensures the GUI has time to update before simulating the next generation.
                }
                return null;
            }
        };
        simulationComplete = GenerationTracker.executor.submit(simulation);
    }

    public Future<?> getSimulationComplete() {
        return simulationComplete;
    }

    /**
     * Begin JavaFX application.
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }


}
