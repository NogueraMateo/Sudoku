package com.example.sudokuproject.controller.alerts;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class WinnerAlert extends Alert {

    public WinnerAlert(String message) {
        super(AlertType.INFORMATION);
        this.setTitle("We have a winner!");
        this.setHeaderText(null);
        this.setContentText(message);

        Image trophyImage = new Image(getClass().getResourceAsStream("/com/example/sudokuproject/images/trofeo.png"));
        ImageView trophy = new ImageView(trophyImage);

        trophy.setFitHeight(80);
        trophy.setFitWidth(80);

        this.setGraphic(trophy);
    }

}
