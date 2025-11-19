package com.comp2042.Controllers;

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

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Label HighScoreDisplay;
    @FXML
    private Label Setting;
    @FXML
    private Label Play;
    @FXML
    private Label Exit;

    public HomeController() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IntegerProperty highScore = new SimpleIntegerProperty(14560);
        bindHighScore(highScore);

        Setting.setOnMouseClicked(e -> {});

        Play.setOnMouseClicked(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getClassLoader().getResource("gameLayout.fxml")
                );
                StackPane gamePane = loader.load();

                GuiController guiController = loader.getController();
                GameController gameController = new GameController(guiController);

                Stage stage = (Stage) Play.getScene().getWindow();
                stage.setScene(new Scene(gamePane));

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Exit.setOnMouseClicked(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public void bindHighScore(IntegerProperty integerProperty) {
        HighScoreDisplay.textProperty().bind(integerProperty.asString());
    }
}
