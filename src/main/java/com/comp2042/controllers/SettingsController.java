package com.comp2042.controllers;

import com.comp2042.enums.SaveDataType;
import com.comp2042.data.SaveData;
import com.comp2042.media.Bgm;
import com.comp2042.media.Sfx;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the controller for the Settings Screen of the application.
 *
 * <p>This class handles the following in the Settings Menu:</p>
 * <ul>
 *     <li>Load the saved Settings</li>
 *     <li>Display the saved Settings</li>
 *     <li>Change the saved setting through {@link SaveData}</li>
 * </ul>
 *
 * <p>
 *  This controller is loaded when the Settings Menu is called
 * </p>
 */
public class SettingsController implements Initializable {

//FXML
    @FXML private StackPane rootPane;
    @FXML private VBox ControlSetting, MusicNVisual;
    @FXML private ImageView closeImage;
    @FXML private Slider MusicSlider, ButtonsSlider, ClearLinesSlider;
    @FXML private Label Left, Right, Down, Rotate, Pause, Hold, Restart, Harddrop;
    @FXML private Label ToggleGhostOn, ToggleGhostOff;
    @FXML private Label ToggleHoldOn, ToggleHoldOff;
    @FXML private Label ToggleNextOn, ToggleNextOff;
    @FXML private Label ToggleControlOn, ToggleControlOff;
    @FXML private Label SettingsLeft, SettingsRight, SettingsTitle;

    private Label selectedLabel = null;

    private Parent settingsPane;

    /**
     * Initializes the Setting screen.
     *
     * <p>This method will: </p>
     * <ul>
     *     <li>Loads saved volume settings</li>
     *     <li>Set up the buttons and slider</li>
     *     <li>Loads and displays keyboard shortcuts</li>
     *     <li>Saves changes made by the player</li>
     * </ul>
     *
     * @param url  unused but required by {@link javafx.fxml.Initializable}
     * @param resourceBundle unused but required by {@link javafx.fxml.Initializable}
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // create close settings buttons
        Image closeNormal = new Image("/images/buttons/C_X.png");
        Image closeHover = new Image("/images/buttons/C_X_P.png");
        closeImage.setImage(closeNormal);
        closeImage.setOnMouseEntered(_ -> closeImage.setImage(closeHover));
        closeImage.setOnMouseExited(_ -> closeImage.setImage(closeNormal));
        ControlSetting.setVisible(false);
        MusicNVisual.setVisible(true);
        SettingsLeft.setVisible(false);

        try {
            MusicSlider.setValue(SaveData.ReadFileInt(SaveData.getKeyEvent(SaveDataType.MUSIC))); // get saved volume (0-100)
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            String[] buttonsData = SaveData.ReadFileList(SaveData.getKeyEvent(SaveDataType.BUTTONS));
            double buttonsVolume = Double.parseDouble(buttonsData[1]);
            ButtonsSlider.setValue(buttonsVolume * 100);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            String[] clearLinesData = SaveData.ReadFileList(SaveData.getKeyEvent(SaveDataType.CLEARLINES));
            double clearLinesVolume = Double.parseDouble(clearLinesData[1]);
            ClearLinesSlider.setValue(clearLinesVolume * 100);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // load toggle
        showToggle(ToggleGhostOn, ToggleGhostOff, SaveData.getKeyEvent(SaveDataType.TOGGLE_GHOST));
        showToggle(ToggleHoldOn, ToggleHoldOff, SaveData.getKeyEvent(SaveDataType.TOGGLE_HOLD));
        showToggle(ToggleNextOn, ToggleNextOff, SaveData.getKeyEvent(SaveDataType.TOGGLE_NEXT));
        showToggle(ToggleControlOn, ToggleControlOff, SaveData.getKeyEvent(SaveDataType.TOGGLE_CONTROLS));

        // load keys
        getKeyCode(Left, SaveData.getKeyEvent(SaveDataType.LEFT));
        getKeyCode(Right, SaveData.getKeyEvent(SaveDataType.RIGHT));
        getKeyCode(Down, SaveData.getKeyEvent(SaveDataType.DOWN));
        getKeyCode(Rotate, SaveData.getKeyEvent(SaveDataType.ROTATE));
        getKeyCode(Pause, SaveData.getKeyEvent(SaveDataType.PAUSE));
        getKeyCode(Hold, SaveData.getKeyEvent(SaveDataType.HOLD));
        getKeyCode(Restart, SaveData.getKeyEvent(SaveDataType.RESTART));
        getKeyCode(Harddrop, SaveData.getKeyEvent(SaveDataType.HARDDROP));

        //setup toggle
        setupToggle(ToggleGhostOn, ToggleGhostOff, SaveData.getKeyEvent(SaveDataType.TOGGLE_GHOST));
        setupToggle(ToggleHoldOn, ToggleHoldOff, SaveData.getKeyEvent(SaveDataType.TOGGLE_HOLD));
        setupToggle(ToggleNextOn, ToggleNextOff, SaveData.getKeyEvent(SaveDataType.TOGGLE_NEXT));
        setupToggle(ToggleControlOn, ToggleControlOff, SaveData.getKeyEvent(SaveDataType.TOGGLE_CONTROLS));

        // Setup shortcut labels
        setupShortcutLabel(Left, SaveData.getKeyEvent(SaveDataType.LEFT));
        setupShortcutLabel(Right, SaveData.getKeyEvent(SaveDataType.RIGHT));
        setupShortcutLabel(Down, SaveData.getKeyEvent(SaveDataType.DOWN));
        setupShortcutLabel(Rotate, SaveData.getKeyEvent(SaveDataType.ROTATE));
        setupShortcutLabel(Pause, SaveData.getKeyEvent(SaveDataType.PAUSE));
        setupShortcutLabel(Hold, SaveData.getKeyEvent(SaveDataType.HOLD));
        setupShortcutLabel(Restart, SaveData.getKeyEvent(SaveDataType.RESTART));
        setupShortcutLabel(Harddrop, SaveData.getKeyEvent(SaveDataType.HARDDROP));

        //mouse event
        closeImage.setOnMouseClicked(_ -> {
            Sfx.play(SaveDataType.BUTTONS);
            closeSettings();
        });

        SettingsLeft.setOnMouseClicked(_ -> {
            Sfx.play(SaveDataType.BUTTONS);
            SettingsLeft.setVisible(false);
            SettingsRight.setVisible(true);
            MusicNVisual.setVisible(true);
            ControlSetting.setVisible(false);
            SettingsTitle.setText("Audio / Visuals");
        });

        SettingsRight.setOnMouseClicked(_ -> {
            Sfx.play(SaveDataType.BUTTONS);

            SettingsLeft.setVisible(true);
            SettingsRight.setVisible(false);
            MusicNVisual.setVisible(false);
            ControlSetting.setVisible(true);
            SettingsTitle.setText("Controls");
        });

        MusicSlider.valueProperty().addListener((_, _, newVal) -> {
            int volume = newVal.intValue();
            try {
                SaveData.overWriteFile(volume, SaveData.getKeyEvent(SaveDataType.MUSIC));
                Bgm.setVolume();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        ButtonsSlider.valueProperty().addListener((_, _, newVal) -> {
            double volume = newVal.doubleValue() / 100.0;
            try {
                SaveData.overWriteFile(volume,1, SaveData.getKeyEvent(SaveDataType.BUTTONS) );
                Sfx.reset(SaveDataType.BUTTONS);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        ClearLinesSlider.valueProperty().addListener((_, _, newVal) -> {
            double volume = newVal.doubleValue() / 100.0;
            try {
                SaveData.overWriteFile(volume,1, SaveData.getKeyEvent(SaveDataType.CLEARLINES) );
                Sfx.reset(SaveDataType.CLEARLINES);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // initiate if toggle is on or off

    /**
     * Set up the mouse event for toggle and display the current toggle choice
     *<p>
     * Initialises the mouse event and resets both labels to the unselected state and determines whether it is on or off
     * from the {@link SaveData}, before adding the Selected CSS to the selected option.
     *</p>
     * @param optionOn the UI of the "on" state of the toggle
     * @param optionOff the UI of the "off" state of the toggle
     * @param line the line number of the save file {@link SaveDataType}
     */

    private void setupToggle(Label optionOn, Label optionOff, int line) {

        EventHandler<MouseEvent> toggleHandler = event -> {
            Label clicked = (Label) event.getSource();

            optionOn.getStyleClass().remove("ToggleSelected");
            optionOff.getStyleClass().remove("ToggleSelected");

            boolean value = clicked.getText().equals("O");

            try {
                SaveData.overWriteFile(Boolean.toString(value), line);
                showToggle(optionOn, optionOff, line);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };

        optionOn.setOnMouseClicked(toggleHandler);
        optionOff.setOnMouseClicked(toggleHandler);
    }

    /**
     * Sets up the mouse and keyboard event to configure the keyboard shortcut for a specific action.
     *
     * <p>
     * When the label is clicked, it is highlighted and will begin listening
     * for the next key pressed by the user. The new key is saved
     * in the save file using {@link SaveData} and {@link SaveDataType}.
     * The label is then updated to display the new keyboard shortcut and the
     * highlight is removed.
     * </p>
     *
     * @param label the keyboard Label of the action
     * @param saveDataLine the line number in the save file
     */
    private void setupShortcutLabel(Label label, int saveDataLine) {
        //
        label.setOnMouseClicked(_ -> {

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

    /**
     /**
     * Highlights the toggle label based on the saved setting.
     *
     * <p>
     * Reads the boolean value from the save file using {@link SaveData} and {@link SaveDataType} to
     * determine which toggle state should apply the "ToggleSelected" CSS style.
     * </p>
     *
     * @param optionOn the UI of the "on" state of the toggle
     * @param optionOff the UI of the "off" state of the toggle
     * @param line the line number of the save file
     */
    private void showToggle(Label optionOn, Label optionOff, int line) {
        try {
            if(SaveData.ReadBoolean(line)){
                optionOn.getStyleClass().add("ToggleSelected");
            } else {
                optionOff.getStyleClass().add("ToggleSelected");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the saved keyboard shortcut from the save file and updates the label.
     *
     * <p>
     * Reads the saved key code stored using {@link SaveData} and {@link SaveDataType}
     * to set the text of the label.
     * </p>
     *
     * @param label display the active keyboard shortcut
     * @param saveDataLine the line number in the save file
     */
    private void getKeyCode(Label label, int saveDataLine){
        try {
            KeyCode keyCode = SaveData.ReadKeyCode(saveDataLine);
            label.setText(keyCode.getName());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Opens the Settings menu and adds it on top of the current UI.
     *
     * <p>
     * The method loads the FXML layout for the settings menu using {@code FXMLLoader}
     * and adds it to the provided {@code rootPane}. The new settings pane overlays
     * the existing UI components.
     * </p>
     *
     * @param rootPane the root container of the current scene to which the settings pane will be added
     * @return the loaded {@code Parent} node representing the settings pane
     * @throws RuntimeException if the FXML file cannot be loaded
     */

    // from another gui controller, open the settings itself
    public static Parent openSettings(StackPane rootPane) {
        FXMLLoader loader;
        Parent settingsPane;
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

    /**
     * Closes the Settings Menu when the user clicks on the close button
     */
    public void closeSettings() {
        rootPane.getChildren().remove(settingsPane);
    }

    /**
     * Sets up the Root Pane so the Settings Menu can stack on top of other FXML
     */
    public void setRootPane(StackPane rootPane){
        this.rootPane = rootPane;
    }

    /**
     * Sets up the settings Pane
     */
    public void setSettingsPane(Parent settingsPane){
        this.settingsPane = settingsPane;
    }

}



