package com.comp2042.controllers;

import com.comp2042.data.SaveData;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML private StackPane rootPane;
    @FXML private Label HighScoreDisplay, Setting, Play, Exit;

    public HomeController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int currentHighScore;
        // if no save file, create one
        SaveData.createSaveFile();

        // get previous high score
        try {
            currentHighScore = Integer.parseInt(SaveData.ReadFileString(0));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        IntegerProperty highScore = new SimpleIntegerProperty(currentHighScore);
        bindHighScore(highScore);

        // when settings button pressed
        Setting.setOnMouseClicked(e -> {
            SettingsController.openSettings(rootPane);
        });

        // when play button pressed
        Play.setOnMouseClicked(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getClassLoader().getResource("gameLayout.fxml")
                );
                StackPane gamePane = loader.load();

                GuiController guiController = loader.getController();
                GameController gameController = new GameController(guiController);

                Stage stage = (Stage) Play.getScene().getWindow();
                stage.setScene(new Scene(gamePane)); //create the game

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        // when exit button pressed, close everything
        Exit.setOnMouseClicked(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public void bindHighScore(IntegerProperty integerProperty) {
        HighScoreDisplay.textProperty().bind(integerProperty.asString());
    }
}
