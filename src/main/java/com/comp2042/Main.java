package com.comp2042;

import com.comp2042.controllers.HomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This class initialises and starts the game.
 */
public class Main extends Application {

    /**
     * Starts the game application.
     *
     * <p>This method loads the {@code homeLayout.fxml} file and applies the
     * custom font before setting up the primary stage and showing
     * the home screen.</p>
     *
     * @param primaryStage the main stage
     * @throws Exception if the FXML file or font cannot be loaded
     */

    @Override
    public void start(Stage primaryStage) throws Exception {

        URL location = getClass().getClassLoader().getResource("homeLayout.fxml");
        Font font = Font.loadFont(
                Objects.requireNonNull(getClass().getResourceAsStream("/fonts/Gluten-Bold.ttf")),
                40
        );
        System.out.println(font.getName());
        ResourceBundle resources = null;
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
        Parent root = fxmlLoader.load();


        primaryStage.setTitle("TetrisJFX");
        Scene scene = new Scene(root, 540, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        new HomeController();
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
