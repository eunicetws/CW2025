package com.comp2042.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        URL location = getClass().getClassLoader().getResource("settingsLayout.fxml");
        Font.loadFont(getClass().getClassLoader().getResource("fonts/Gluten-Bold.ttf").toExternalForm(), 40);
        ResourceBundle resources = null;
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
        Parent root = fxmlLoader.load();
        SettingsGuiController c = fxmlLoader.getController();

        primaryStage.setTitle("Settings Menu Test");
        Scene scene = new Scene(root, 540, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        new SettingsGuiController();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
