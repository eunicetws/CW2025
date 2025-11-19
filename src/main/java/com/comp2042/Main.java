package com.comp2042;

import com.comp2042.Controllers.HomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        URL location = getClass().getClassLoader().getResource("homeLayout.fxml");
        Font.loadFont(getClass().getClassLoader().getResource("fonts/Gluten-Bold.ttf").toExternalForm(), 40);
        ResourceBundle resources = null;
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
        Parent root = fxmlLoader.load();


        primaryStage.setTitle("TetrisJFX");
        Scene scene = new Scene(root, 540, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        new HomeController();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
