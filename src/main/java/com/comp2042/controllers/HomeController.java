package com.comp2042.controllers;

import com.comp2042.data.SaveData;
import com.comp2042.logic.Timer;
import com.comp2042.enums.SaveDataType;
import com.comp2042.media.Bgm;
import com.comp2042.media.Sfx;
import javafx.application.Platform;
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

/**
 * This is the controller for the Home screen of the application.
 *
 * <p>This class handles the following in the Home Menu:</p>
 * <ul>
 *     <li>Initializing UI elements (high score, timer text, audio)</li>
 *     <li>Binding button interactions for settings, play, timer adjustment, and exit</li>
 * </ul>
 *
 * <p>
 *  This controller is automatically instantiated by JavaFX when the
 *  {@code homeLayout.fxml} file is loaded.
 * </p>
 */

public class HomeController implements Initializable {

    @FXML private StackPane rootPane;
    @FXML private Label HighScoreDisplay, Setting, Play, Exit, TimerAdd, TimerMinus, TimerDisplay;
    int option = 1;

    /**
     * Default constructor required by JavaFX.
     */
    public HomeController() {

    }

    /**
     * Initializes the Home screen.
     *
     * <p>This method will:
     * <ul>
     *     <li>Ensures a save file exists using {@link SaveData#createSaveFile()}</li>
     *     <li>Loads and displays the current high score</li>
     *     <li>Plays the background music (BGM)</li>
     *     <li>Loads sound effects when a button is pressed</li>
     *     <li>Assigns mouse event handlers to the Settings, Play, Timer, and Exit buttons</li>
     *     <li>Handles scene switching when the Play button is clicked</li>
     * </ul>
     *
     * @param location  unused but required by {@link javafx.fxml.Initializable}
     * @param resources unused but required by {@link javafx.fxml.Initializable}
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    /**
     * Allow users to choose the timer option
     *
     * <p> This method updates: </p>
     *
     * <ul>
     *     <li>The displayed timer chosen</li>
     *     <li>Update the game timer duration using {@link Timer}</li>
     *     <li>The displayed high score belonging to the selected timer</li>
     * </ul>
     *
     * <p>Timer modes wrap around (1–5).
     * Example: moving forward from option 5 returns to option 1.
     *
     * @param add {@code true} to increment the timer option, {@code false} to decrement
     */
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
