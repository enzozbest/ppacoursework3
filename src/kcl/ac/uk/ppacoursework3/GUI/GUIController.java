package src.kcl.ac.uk.ppacoursework3.GUI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import src.kcl.ac.uk.ppacoursework3.lifeForms.Cell;
import src.kcl.ac.uk.ppacoursework3.lifeForms.*;

import java.util.HashMap;


public class GUIController {

    public static final int WIN_WIDTH = 800;
    public static final int WIN_HEIGHT = 800;

    private final SimulatorView view;

    @FXML
    private Button startButton, stepButton, resetButton;

    private final FieldStats stats;

    private int mycoCount, lycoCount, conCount, disCount, phaCount, metaCount;

    @FXML
    private Label mycoLabel, lycoLabel, conLabel, disLabel, phaLabel, metaLabel;

    @FXML
    private Label genLabel;

    @FXML
    private final int gen;

    public static boolean shouldRun;

    public GUIController(SimulatorView view) {
        this.view = view;
        shouldRun = false;
        stats = new FieldStats();
        gen = 10;
        mycoCount = 0;
        lycoCount = 0;
        conCount = 0;
        disCount = 0;
        phaCount = 0;
        metaCount = 0;

    }

    @FXML
    private void clickStart(ActionEvent actionEvent) {
        shouldRun = !shouldRun;
        if (shouldRun) {

            TextInputDialog dialog = new TextInputDialog("Game of Life Simulation");
            DialogPane dialogPane = new DialogPane();
            dialogPane.setPadding(new Insets(10, 10, 10, 10));
            dialogPane.setPrefWidth(300);
            dialogPane.setPrefHeight(100);

            TextField numGen = new TextField();
            numGen.setText("Enter the number of generations you wish to simulate");
            numGen.autosize();

            TextField delay = new TextField();
            delay.setText("Enter the delay between generations in seconds");
            delay.autosize();


//            VBox vBox = new VBox();
//            vBox.setAlignment(Pos.BOTTOM_LEFT);
//            vBox.setSpacing(10);
//            vBox.getChildren().add(numGen);
//            vBox.getChildren().add(delay);
//
//            dialogPane.getChildren().add(vBox);

            dialogPane.autosize();

            dialogPane.setHeaderText("Choose Simulation Parameters");
            dialog.setTitle("Game of Life Simulation");
            dialog.setDialogPane(dialogPane);
            // dialog.show();
            view.simulate(gen);
            shouldRun = false;
        }
    }

    public void updateGenerationLabel() {
        genLabel.setText("Generation: " + SimulatorView.simulator.getGeneration());//Updates the GUI
    }

    private void calculateStats() {
        HashMap<Class<? extends Cell>, Integer> population = stats.getPopulationDetails(SimulatorView.simulator.getField());
        mycoCount = population.get(Mycoplasma.class);
        lycoCount = population.get(Lycoperdon.class);
        conCount = population.get(Conus.class);
        disCount = population.get(DiseasedCell.class);
        phaCount = population.get(Phage.class);
        metaCount = population.get(Metamorph.class);
    }


    @FXML
    private void clickStep(ActionEvent actionEvent) {
        if (shouldRun) {
            return;
        }
        SimulatorView.simulator.simOneGeneration();
        updateCanvas();
    }

    private void updatePopulationLabels() {
        calculateStats();
        mycoLabel.setText("Mycoplasma: " + mycoCount);
        lycoLabel.setText("Lycoperdon: " + lycoCount);
        conLabel.setText("Conus: " + conCount);
        disLabel.setText("Diseased Cell: " + disCount);
        phaLabel.setText("Phage: " + phaCount);
        metaLabel.setText("Metamorph: " + metaCount);
    }

    private void updateCanvas() {
        Platform.runLater(() -> {
                    view.updateCanvas(SimulatorView.simulator.getField());
                    updateGenerationLabel();
                    updatePopulationLabels();
                }
        );
        SimulatorView.simulator.delay(10); //ensures the GUI has time to update before simulating the next generation.
    }

    @FXML
    private void clickReset(ActionEvent actionEvent) {
        if (shouldRun) {
            return;
        }
        view.reset();
        startButton.setText("Start");
        updateCanvas();
    }
}
