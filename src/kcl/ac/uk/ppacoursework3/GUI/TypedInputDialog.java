package src.kcl.ac.uk.ppacoursework3.GUI;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.List;

/**
 * This class represents a custom dialog box that prompts the user to enter the number of generations and the delay
 * between generations. It is used to set the parameters for the simulation.
 * <p>
 * The user is prompted to enter the number of generations and the delay between generations. Validation is performed to
 * ensure that the user enters a positive integer for the number of generations and a decimal value for the delay.
 * <p>
 * If the user enters invalid inputs, an error message is displayed as an alert and the user is prompted to enter
 * the values again.
 *
 * @author Enzo Bestetti (K23011872), Krystian Augustynowicz (K23000902)
 * @version 2024.03.01
 */
public class TypedInputDialog extends Dialog<List<Number>> {

    private TextField numGen;
    private TextField delay;

    /**
     * Constructor for the TypedInputDialog class.
     * It sets the size of the dialog box and the content of the dialog box.
     * The content of the dialog box is a GridPane that contains two labels and two text fields. The labels prompt the
     * user to enter the number of generations and the delay between generations. The text fields allow the user to enter
     * the values.
     * <p>
     * The dialog box also contains a "Done" button that the user can click to submit the values. The button is added to
     * the dialog box and the dialog's result converter is set to convert the button data to a list of 2 numbers.
     * <p>
     * The first element in the returned list is the number of generations and the second element is the delay between
     * generations.
     */
    public TypedInputDialog() {
        DialogPane pane = getDialogPane();
        pane.setPrefWidth(350);
        pane.setPrefHeight(200);
        pane.setContent(getGridPane());

        ButtonType okButton = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);

        pane.getButtonTypes().addAll(okButton);
        setResultConverter(this::convertButtonData);
    }


    /**
     * This method creates a GridPane that contains two labels and two text fields. The labels prompt the user to enter
     * the number of generations and the delay between generations. The text fields allow the user to enter the values.
     *
     * @return the complete GridPane
     */
    private GridPane getGridPane() {
        numGen = new TextField();
        delay = new TextField();

        Label numGenLabel = new Label("Number of Generations:");
        Label delayLabel = new Label("Delay between Generations (sec):");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPrefSize(300, 100);
        gridPane.setHgap(180);
        gridPane.setVgap(20);

        gridPane.add(numGenLabel, 0, 0, 2, 1);
        gridPane.add(numGen, 1, 0, 2, 1);
        gridPane.add(delayLabel, 0, 1, 2, 1);
        gridPane.add(delay, 1, 1, 2, 1);

        return gridPane;
    }

    /**
     * This method is called when the user clicks the "Done" button. It converts the button data to a list of 2 numbers.
     * The first element in the returned list is the number of generations and the second element is the delay between
     * generations.
     * <p>
     * Validation is performed to ensure that the user enters a positive integer for the number of generations and a
     * decimal value for the delay. If the user enters invalid inputs, an error message is displayed as an alert and
     * the user is prompted to enter the values again.
     *
     * @param buttonType the type of button that was clicked
     * @return a list of 2 numbers
     */
    private List<Number> convertButtonData(ButtonType buttonType) {
        List<Number> ret;

        try {
            int gen = Integer.parseInt(numGen.getText());
            double delayValue = Double.parseDouble(delay.getText());

            if (gen <= 0 || delayValue <= 0) {
                throw new NumberFormatException();
            }

            ret = List.of(gen, delayValue);
            return ret;
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Invalid Inputs");
            alert.setContentText("Enter a positive integer for the number of generations \n and a decimal value for the delay.");
            alert.showAndWait();
        }
       /* Use recursion to prompt the user to enter the values again until valid inputs are entered and pass the results
        along a "chain" of recursive calls*/
        TypedInputDialog alternate = new TypedInputDialog();
        alternate.showAndWait();
        return alternate.getResult();
    }
}

