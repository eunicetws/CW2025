package com.comp2042.test;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeGUI implements Initializable {

    @FXML
    private Label HighScoreDisplay;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IntegerProperty highScore = new SimpleIntegerProperty(14560);
        bindHighScore(highScore);
    }


    public void bindHighScore(IntegerProperty integerProperty) {
        HighScoreDisplay.textProperty().bind(integerProperty.asString());
    }
}
