package src.kcl.ac.uk.ppacoursework3.GUI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import src.kcl.ac.uk.ppacoursework3.concurrent.GenerationTracker;
import src.kcl.ac.uk.ppacoursework3.lifeForms.*;
import src.kcl.ac.uk.ppacoursework3.simulation.Simulator;
import src.kcl.ac.uk.ppacoursework3.utils.Counter;

import java.util.HashMap;
import java.util.List;

/**
 * This class is the controller for the GUI. It is responsible for handling the user's input and updating the GUI
 * with the current state of the simulation.
 * <p>
 * This class is set as the controller for the GUI in the main FXML file, and as such declares some fields and methods
 * with the annotation @FXML to allow the JavaBean adapter of JavaFX to access these members.
 * <p>
 * The logic behind the GUI is handled in this class, that is, what buttons do when clicked, updating labels, and
 * initiating a custom dialog to get user input for the simulation. These methods pass the necessary information to the
 * SimulatorView class to actually perform the simulation tasks.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902)
 * @version 2024.03.01
 */
public class GUIController {

    public static final int WIN_WIDTH = 800;
    public static final int WIN_HEIGHT = 800;
    private final SimulatorView view;
    private final Simulator simulator;
    @FXML
    private Button startButton, stepButton, resetButton;
    @FXML
    private Label genLabel, mycoLabel, lycoLabel, conLabel, disLabel, phaLabel, metaLabel;
    private final HashMap<Class, Counter> population;
    private int mycoCount, lycoCount, conCount, disCount, phaCount, metaCount;

    /**
     * Create the GUI controller with a reference to the SimulatorView object that is responsible for the simulation.
     *
     * @param view the SimulatorView object that is responsible for the simulation logic.
     */
    public GUIController(SimulatorView view) {
        this.view = view;
        simulator = SimulatorView.simulator;
        population = view.getStats().getPopulationDetails(simulator.getField());
    }

    /**
     * This method is called when the user clicks the "Start" button. It will start the simulation with the parameters
     * provided by the user (number of generations and delay between generations) in a dialog box.
     * It also disables all GUI buttons to prevent the user from clicking them while the simulation is running, and
     * re-enables them once the simulation is complete.
     *
     * @param actionEvent the button click
     */
    @FXML
    private void clickStart(ActionEvent actionEvent) {
        TypedInputDialog dialog = createInputDialog();
        dialog.showAndWait(); //Wait for user input before starting the simulation.

        disableButtons();

        List<Number> dialogResults = dialog.getResult(); //Retrieve user input from the dialog box
        int gen = dialogResults.get(0).intValue(); //Use the number of generations as an int
        double delay = dialogResults.get(1).doubleValue(); //Use the delay as a double
        int millisecDelay = (int) (delay * 1000); //Convert the delay to milliseconds

        view.simulate(gen, millisecDelay); //Start the simulation with the user input

        //Create a new thread to check if the simulation is complete, and re-enable the buttons when it is.
        GenerationTracker.EXECUTOR.submit(() -> {
            while (!view.getSimulationComplete().isDone()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {

                }
            }
            Platform.runLater(this::enableButtons);
        });
    }

    /**
     * This method is called when the user clicks the "Step" button. It will simulate one generation of the simulation.
     * It also invokes the updateCanvas() method to update the GUI with the state of the simulation after the next
     * generation is simulated.
     *
     * @param actionEvent the button click
     */
    @FXML
    private void clickStep(ActionEvent actionEvent) {
        simulator.simOneGeneration();
        this.updateCanvas();
    }

    /**
     * This method is called when the user clicks the "Reset" button. It will reset the simulation
     * This means that the field will be cleared and the generation counter will be reset to 0.
     * The grid is populated again and the GUI is updated to reflect the new state of the simulation.
     *
     * @param actionEvent the button click
     */
    @FXML
    private void clickReset(ActionEvent actionEvent) {
        view.reset();
        startButton.setText("Start");
        this.updateCanvas();
    }

    /**
     * Set the buttons to be disabled, to prevent the user from clicking them while the simulation is running.
     */
    private void disableButtons() {
        resetButton.setDisable(true);
        stepButton.setDisable(true);
        startButton.setDisable(true);
    }

    /**
     * Set the buttons to be enabled, to allow the user to interact with the GUI again.
     */
    private void enableButtons() {
        resetButton.setDisable(false);
        stepButton.setDisable(false);
        startButton.setDisable(false);
    }

    /**
     * Update the generation label to display the current generation number.
     */
    public void updateGenerationLabel() {
        genLabel.setText("Generation: " + simulator.getGeneration()); //Updates the GUI
    }

    /**
     * Update the population labels to display the current population of each life form.
     */
    public void updatePopulationLabels() {
        retrieveStats();
        mycoLabel.setText("Mycoplasma: " + mycoCount);
        lycoLabel.setText("Lycoperdon: " + lycoCount);
        conLabel.setText("Conus: " + conCount);
        disLabel.setText("Diseased Cell: " + disCount);
        phaLabel.setText("Phage: " + phaCount);
        metaLabel.setText("Metamorph: " + metaCount);
    }

    /**
     * Create a custom dialog box to get user input for the number of generations and the delay between each generation.
     *
     * @return the dialog box
     */
    private TypedInputDialog createInputDialog() {
        TypedInputDialog dialog = new TypedInputDialog();
        dialog.setTitle("Game of Life Simulation");
        return dialog;
    }

    /**
     * Retrieve the current population of each life form  and store the counts in the appropriate fields.
     * This method is called when the GUI is first created and when the population labels are updated.
     */
    private void retrieveStats() {
        mycoCount = population.get(Mycoplasma.class).getCount();
        lycoCount = population.get(Lycoperdon.class).getCount();
        conCount = population.get(Conus.class).getCount();
        phaCount = population.get(Phage.class).getCount();
        metaCount = population.get(Metamorph.class).getCount();
        if (population.get(DiseasedCell.class) == null) {
            disCount = 0;
            return;
        }
        disCount = population.get(DiseasedCell.class).getCount();
    }

    /**
     * Update the canvas to display the current state of the simulation.
     */
    private void updateCanvas() {
        Platform.runLater(() -> view.updateCanvas(simulator.getField()));
        simulator.delay(10); //ensures the GUI has time to update before simulating the next generation.
    }
}
