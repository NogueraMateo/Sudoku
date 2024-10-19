package com.example.sudokuproject.controller.alerts;

import javafx.scene.control.Alert;

/**
 * ConfirmationAlert is a custom alert that extends {@link Alert}.
 * It displays a confirmation message to the user when they attempt to start a new game.
 *
 * The alert uses the {@link Alert.AlertType#CONFIRMATION} type and prompts the user with a confirmation dialog.
 *
 * @author Mateo Noguera Pinto
 */
public class ConfirmationAlert extends Alert {

    /**
     * Constructor that creates a new {@link Alert} of type {@link Alert.AlertType#CONFIRMATION}.
     * It sets the title, header text, and content text for the confirmation alert.
     *
     * @param message The title of the confirmation alert.
     */
    public ConfirmationAlert(String message) {
        super(AlertType.CONFIRMATION);
        this.setTitle(message);
        this.setHeaderText("NEW GAME");
        this.setContentText("Are you sure you want to start a new game?");
    }
}
