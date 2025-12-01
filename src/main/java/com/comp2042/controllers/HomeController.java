package com.comp2042.controllers;

import com.comp2042.data.SaveData;
import com.comp2042.data.Timer;
import com.comp2042.enums.SaveDataType;
import com.comp2042.media.Bgm;
import com.comp2042.media.Sfx;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
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
    @FXML private Label HighScoreDisplay, Setting, Play, Exit, TimerAdd, TimerMinus, TimerDisplay;
    int option = 1;

    public HomeController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int currentHighScore;
        // if no save file, create one
        SaveData.createSaveFile();

        TimerDisplay.setText("None");
        try {
            HighScoreDisplay.setText(SaveData.ReadFileString(SaveData.getKeyEvent(SaveDataType.HIGHSCORE)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // play bgm
        Bgm.init();
        Bgm.play();

        //set sfx
        Sfx.loadMap(SaveDataType.BUTTONS);
        Sfx.loadMap(SaveDataType.CLEARLINES);

        // when settings button pressed
        Setting.setOnMouseClicked(_ -> {
            Sfx.play(SaveDataType.BUTTONS);
            SettingsController.openSettings(rootPane);
        });

        TimerAdd.setOnMouseClicked(_ -> getCurrentTimer(true));
        TimerMinus.setOnMouseClicked(_ -> getCurrentTimer(false));
        // when play button pressed
        Play.setOnMouseClicked(e -> {
            try {
                Sfx.play(SaveDataType.BUTTONS);
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
            Sfx.play(SaveDataType.BUTTONS);
            Platform.exit();
            System.exit(0);
        });
    }

    public void getCurrentTimer(boolean add) {
        if (add) {
            option++;
            if (option > 5) option = 1;   // wrap
        } else {
            option--;
            if (option < 1) option = 5;   // wrap
        }
        try {
            switch (option) {
                case 1 -> {
                    Timer.setTimer(0);
                    TimerDisplay.setText("None");
                    HighScoreDisplay.setText(SaveData.ReadFileString(SaveData.getKeyEvent(SaveDataType.HIGHSCORE)));
                }
                case 2 -> {
                    Timer.setTimer(5);
                    TimerDisplay.setText("5 minutes");
                    HighScoreDisplay.setText(SaveData.ReadFileString(SaveData.getKeyEvent(SaveDataType.HIGHSCORE_5)));
                }
                case 3 -> {
                    Timer.setTimer(10);
                    TimerDisplay.setText("10 minutes");
                    HighScoreDisplay.setText(SaveData.ReadFileString(SaveData.getKeyEvent(SaveDataType.HIGHSCORE_10)));
                }
                case 4 -> {
                    Timer.setTimer(15);
                    TimerDisplay.setText("15 minutes");
                    HighScoreDisplay.setText(SaveData.ReadFileString(SaveData.getKeyEvent(SaveDataType.HIGHSCORE_15)));
                }
                case 5 -> {
                    Timer.setTimer(20);
                    TimerDisplay.setText("20 minutes");
                    HighScoreDisplay.setText(SaveData.ReadFileString(SaveData.getKeyEvent(SaveDataType.HIGHSCORE_20)));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
