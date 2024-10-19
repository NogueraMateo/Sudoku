package com.example.sudokuproject.controller.alerts;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * WinnerAlert is a custom alert that extends {@link Alert}.
 * It displays a message and a trophy image when there is a winner in the game.
 * The alert uses the {@link Alert.AlertType#INFORMATION} type to inform the user of the victory.
 *
 * The alert shows a custom message along with an image of a trophy.
 *
 * @author Mateo Noguera Pinto
 */
public class WinnerAlert extends Alert {

    /**
     * Constructor that creates a new {@link Alert} of type {@link Alert.AlertType#INFORMATION}.
     * It sets the title of the alert to "We have a winner!", displays a custom message,
     * and includes an image of a trophy in the alert's graphic.
     *
     * @param message The message to display in the content of the alert.
     */
    public WinnerAlert(String message) {
        super(AlertType.INFORMATION);
        this.setTitle("We have a winner!");
        this.setHeaderText(null);
        this.setContentText(message);

        // Load the trophy image and set it as the graphic for the alert
        Image trophyImage = new Image(getClass().getResourceAsStream("/com/example/sudokuproject/images/trofeo.png"));
        ImageView trophy = new ImageView(trophyImage);

        // Set the size of the trophy image
        trophy.setFitHeight(80);
        trophy.setFitWidth(80);

        // Set the trophy image as the graphic for the alert
        this.setGraphic(trophy);
    }

}
