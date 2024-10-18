package com.example.sudokuproject.controller;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;

public class WelcomeSudokuController {

    @FXML
    private GridPane sudokuContainer;

    @FXML
    private Button playButton;

    private final WelcomeSudokuModel model;

    public WelcomeSudokuController() {
        this.model = new WelcomeSudokuModel();
    }

    @FXML
    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            this.sudokuAnimation();
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        this.startPlayButtonAnimation();
    }


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


    @FXML
    public void onPlayPressed(Event event) throws IOException {
        Node source = (Node) event.getSource();
        Stage actualStage = (Stage) source.getScene().getWindow();
        actualStage.close();

        SudokuGameView.getInstance();
    }
}
