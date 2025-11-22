package com.comp2042.test;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsGuiController implements Initializable {


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
    private Label NewGame;

    private Label selectedLabel = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // close settings
        Image closeNormal = new Image("/images/buttons/C_X.png");
        Image closeHover = new Image("/images/buttons/C_X_P.png");
        closeImage.setImage(closeNormal);
        closeImage.setOnMouseEntered(e -> closeImage.setImage(closeHover));
        closeImage.setOnMouseExited(e -> closeImage.setImage(closeNormal));

        // change shortcuts
        setupShortcutLabel(Left);
        setupShortcutLabel(Right);
        setupShortcutLabel(Down);
        setupShortcutLabel(Rotate);
        setupShortcutLabel(Pause);
        setupShortcutLabel(Hold);
        setupShortcutLabel(NewGame);
    }

    private void setupShortcutLabel(Label label) {
        label.setOnMouseClicked(event -> {
            if (selectedLabel == null) {

                selectedLabel = label;
                label.getStyleClass().add("ShortcutKeySelect"); // when clicked, change colour to show it is selected

                // start listening for key input
                label.getScene().setOnKeyPressed(keyEvent -> {
                    String key = keyEvent.getCode().toString();
                    selectedLabel.setText(key);

                    // remove highlight
                    selectedLabel.getStyleClass().remove("ShortcutKeySelect"); // get text is done, so remove it
                    selectedLabel = null;

                    label.getScene().setOnKeyPressed(null); //stop listening for keys
                });
            }
        });
    }
}


