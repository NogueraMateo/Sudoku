package com.example.sudokuproject.controller.alerts;

import javafx.scene.control.Alert;

public class ConfirmationAlert extends Alert {
    public ConfirmationAlert(String message) {
        super(AlertType.CONFIRMATION);
        this.setTitle(message);
        this.setHeaderText("NEW GAME");
        this.setContentText("Are you sure you want to start a new game?");
    }
}
