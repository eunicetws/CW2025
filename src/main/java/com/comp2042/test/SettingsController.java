package com.comp2042.test;

import com.comp2042.logic.KeyEventType;
import com.comp2042.logic.SaveData;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {


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

    private Label selectedLabel = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // close settings
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

    }

    private void setupShortcutLabel(Label label, int saveDataLine) {
        label.setOnMouseClicked(event -> {
            if (selectedLabel != null) {
                selectedLabel.getStyleClass().remove("ShortcutKeySelect");
                selectedLabel.getScene().setOnKeyPressed(null);
            }
            selectedLabel = label;
            label.getStyleClass().add("ShortcutKeySelect"); // when clicked, change colour to show it is selected

            // start listening for key input
            label.getScene().setOnKeyPressed(keyEvent -> {
                String key = keyEvent.getCode().toString();

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
}


