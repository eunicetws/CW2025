package com.comp2042.controllers;

import com.comp2042.enums.KeyEventType;
import com.comp2042.data.SaveData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML
    private StackPane rootPane;

    @FXML
    private ImageView closeImage;

    @FXML
    private Label Left;

    @FXML
    private Label Right;

    @FXML
    private Label Down;

    @FXML
    private Label Rotate;

    @FXML
    private Label Pause;

    @FXML
    private Label Hold;

    @FXML
    private Label Restart;

    @FXML
    private StackPane SettingsRoot;

    private Label selectedLabel = null;

    private Parent settingsPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // create close settings buttons
        Image closeNormal = new Image("/images/buttons/C_X.png");
        Image closeHover = new Image("/images/buttons/C_X_P.png");
        closeImage.setImage(closeNormal);
        closeImage.setOnMouseEntered(e -> closeImage.setImage(closeHover));
        closeImage.setOnMouseExited(e -> closeImage.setImage(closeNormal));

        // load keys
        getKeyCode(Left, SaveData.getKeyEvent(KeyEventType.LEFT));
        getKeyCode(Right, SaveData.getKeyEvent(KeyEventType.RIGHT));
        getKeyCode(Down, SaveData.getKeyEvent(KeyEventType.DOWN));
        getKeyCode(Rotate, SaveData.getKeyEvent(KeyEventType.ROTATE));
        getKeyCode(Pause, SaveData.getKeyEvent(KeyEventType.PAUSE));
        getKeyCode(Hold, SaveData.getKeyEvent(KeyEventType.HOLD));
        getKeyCode(Restart, SaveData.getKeyEvent(KeyEventType.RESTART));

        // Setup shortcut labels
        setupShortcutLabel(Left, SaveData.getKeyEvent(KeyEventType.LEFT));
        setupShortcutLabel(Right, SaveData.getKeyEvent(KeyEventType.RIGHT));
        setupShortcutLabel(Down, SaveData.getKeyEvent(KeyEventType.DOWN));
        setupShortcutLabel(Rotate, SaveData.getKeyEvent(KeyEventType.ROTATE));
        setupShortcutLabel(Pause, SaveData.getKeyEvent(KeyEventType.PAUSE));
        setupShortcutLabel(Hold, SaveData.getKeyEvent(KeyEventType.HOLD));
        setupShortcutLabel(Restart, SaveData.getKeyEvent(KeyEventType.RESTART));

        //mouse event
        closeImage.setOnMouseClicked(e -> closeSettings());
    }

    // highlight the label and allow keyboard shortcut to change
    private void setupShortcutLabel(Label label, int saveDataLine) {
        //
        label.setOnMouseClicked(event -> {

            //if a prev label is selected, deselect it and select the new label instead
            if (selectedLabel != null) {
                selectedLabel.getStyleClass().remove("ShortcutKeySelect");
                selectedLabel.getScene().setOnKeyPressed(null);
            }
            selectedLabel = label;

            // when clicked, change colour to show it is selected
            label.getStyleClass().add("ShortcutKeySelect");

            // start listening for key input
            label.getScene().setOnKeyPressed(keyEvent -> {
                String key = keyEvent.getCode().toString();

                //write new shortcut to dave file
                try {
                    SaveData.overWriteFile(key, saveDataLine);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                getKeyCode(selectedLabel, saveDataLine);

                // remove highlight after getKeyCode
                selectedLabel.getStyleClass().remove("ShortcutKeySelect");
                selectedLabel = null;

                label.getScene().setOnKeyPressed(null); //stop listening for keys
            });
        });
    }

    private void getKeyCode(Label label, int saveDataLine){
        try {
            KeyCode keyCode = SaveData.ReadKeyCode(saveDataLine);
            label.setText(keyCode.getName());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // from another gui controller, open the settings itself
    public static Parent openSettings(StackPane rootPane) {
        FXMLLoader loader;
        Parent settingsPane = null;
        try {
            loader = new FXMLLoader(SettingsController.class.getResource("/settingsLayout.fxml"));
            settingsPane = loader.load();

            rootPane.getChildren().add(settingsPane);

            SettingsController settingsController = loader.getController();
            settingsController.setRootPane(rootPane);
            settingsController.setSettingsPane(settingsPane);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return settingsPane;
    }

    // close it when done
    public void closeSettings() {
        rootPane.getChildren().remove(settingsPane);
    }

    public void setRootPane(StackPane rootPane){
        this.rootPane = rootPane;
    }

    public void setSettingsPane(Parent settingsPane){
        this.settingsPane = settingsPane;
    }

}



