package com.example.sudokuproject.controller;

import com.example.sudokuproject.controller.alerts.ConfirmationAlert;
import com.example.sudokuproject.model.WelcomeSudokuModel;
import com.example.sudokuproject.view.SudokuGameView;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Controller for the Welcome Sudoku view.
 * This controller handles the animations for the play button and the numbers on the Sudoku grid.
 * It also manages the start of a new Sudoku game.
 *
 * @author Mateo Noguera Pinto
 */

public class WelcomeSudokuController {

    /**
     * The Sudoku grid container where the numbers are displayed.
     */
    @FXML
    private GridPane sudokuContainer;

    /**
     * The button to start the game.
     */
    @FXML
    private Button playButton;

    /**
     * The model used by the controller to generate random cells and numbers.
     */
    private final WelcomeSudokuModel model;

    /**
     * Default constructor for the controller. Initializes the model.
     */
    public WelcomeSudokuController() {
        this.model = new WelcomeSudokuModel();
    }

    /**
     * Method automatically called by JavaFX on initialization.
     * It starts the animation for the Sudoku grid and the play button.
     */
    @FXML
    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            this.sudokuAnimation();
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        this.startPlayButtonAnimation();
    }

    /**
     * Generates an animation for three random cells on the Sudoku grid.
     * Each cell is assigned a random number between 1 and 6 without repetition.
     * The selected cell displays the number and applies a fade transition.
     */
    public void sudokuAnimation() {
        ArrayList<Integer> currentNumbers = new ArrayList<>();
        Pair<Integer, Integer> randomCell;
        int randomNumber, col, row;

        for (int i = 0; i < 3; i++) {

            randomCell = this.model.getRandomCell();

            // Looping over all the children of the GridPane
            for (Node node : this.sudokuContainer.getChildren()) {

                // The key represents the column and the value the row
                col = randomCell.getKey();
                row = randomCell.getValue();

                // If the indexes of the current node match with the random indexes a random number is set in the text field
                if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {

                    TextField textField = (TextField) node;

                    do {
                        randomNumber = this.model.generateNumber(1, 6);
                    } while (currentNumbers.contains(randomNumber));

                    textField.setText(String.valueOf(randomNumber));
                    currentNumbers.add(randomNumber);
                    this.startFadeTransition(textField, 1, 1.0, 0.5, 1);
                }
            }

            if (i == 2) {
                currentNumbers.clear();
            }
        }
    };


    /**
     * Applies a fade transition to a given text field.
     *
     * @param textField The text field where the fade transition is applied.
     * @param duration  The duration of the transition in seconds.
     * @param from      The starting opacity value.
     * @param to        The ending opacity value.
     * @param delay     The delay before starting the transition in seconds.
     */
    public void startFadeTransition(
            TextField textField,
            double duration,
            double from,
            double to,
            double delay) {

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(duration), textField);
        fadeTransition.setFromValue(from);
        fadeTransition.setToValue(to);
        fadeTransition.setDelay(Duration.seconds(delay));
        fadeTransition.play();

        fadeTransition.setOnFinished(e -> {
            textField.setText("");
            textField.setOpacity(1.0);
        });
    }

    /**
     * Starts a scale animation for the play button.
     * The button increases in size slightly, and the animation repeats indefinitely.
     */
    public void startPlayButtonAnimation() {
        ScaleTransition scaleTransitionGrow = new ScaleTransition(Duration.seconds(1), this.playButton);
        scaleTransitionGrow.setFromX(1);
        scaleTransitionGrow.setFromY(1);
        scaleTransitionGrow.setToX(1.05);
        scaleTransitionGrow.setToY(1.05);

        scaleTransitionGrow.setCycleCount(ScaleTransition.INDEFINITE);
        scaleTransitionGrow.setAutoReverse(true);

        scaleTransitionGrow.play();
    }

    /**
     * Handles the event when the play button is pressed.
     * A confirmation alert is displayed, and if confirmed, the current window is closed, and a new Sudoku game is started.
     *
     * @param event The event triggered by pressing the play button.
     * @throws IOException If an error occurs while loading the Sudoku game view.
     */
    @FXML
    public void onPlayPressed(Event event) throws IOException {
        ConfirmationAlert alert = new ConfirmationAlert("Confirm the start of a new game");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Node source = (Node) event.getSource();
            Stage actualStage = (Stage) source.getScene().getWindow();
            actualStage.close();

            SudokuGameView.getInstance();
        } else {
            alert.close();
        }

    }
}
