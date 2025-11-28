package com.comp2042.controllers;

import com.comp2042.data.Timer;
import com.comp2042.enums.EventSource;
import com.comp2042.enums.EventType;
import com.comp2042.data.SaveData;
import com.comp2042.enums.KeyEventType;
import com.comp2042.interfaces.InputEventListener;
import com.comp2042.logic.DownData;
import com.comp2042.logic.MoveEvent;
import com.comp2042.media.Sfx;
import com.comp2042.view.NotificationPanel;
import com.comp2042.view.ViewData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;

// FXML
    @FXML private StackPane rootPane;

    //Playing
    @FXML private GridPane gamePanel, brickPanel, ghostPiecePanel, nextBrickDisplay, holdBrickDisplay;
    @FXML private Group groupNotification;
    @FXML private ImageView pauseImage;
    @FXML private Labeled scoreLabel, totalClearedLinesLabel, levelLabel;
    @FXML private Label TimerDisplay, LeftKeyLabel, RightKeyLabel, RotateKeyLabel, DownKeyLabel,
            HoldKeyLabel, PauseKeyLabel, RestartKeyLabel, HardDropKeyLabel;
    @FXML private VBox TimerPanel, NextPanel, HoldPanel, KeyboardKeys;

    //Pause Menu
    @FXML private StackPane PauseMenu, Resume;
    @FXML private Label Pause_Restart, Pause_Home, Pause_Settings;

    // Game Over
    @FXML private StackPane GameOverMenu;
    @FXML private Label GameOver_Restart, GameOver_Home, GameOver_Settings;

// Playing
    private Rectangle[][] displayMatrix, rectangles, rectanglesNextBrick, rectanglesHoldBrick, rectanglesGhostPiece;
    private InputEventListener eventListener;
    private Timeline timeLine;
    private boolean isHoldOn;
    private final BooleanProperty isPause = new SimpleBooleanProperty();
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    Parent settingsPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set Pause Button in playing
        Image normal = new Image("/images/buttons/Buttons-pause.png");
        Image hover = new Image("/images/buttons/Buttons-pause-pressed.png");
        pauseImage.setImage(normal);
        pauseImage.setOnMouseEntered(_ -> pauseImage.setImage(hover));
        pauseImage.setOnMouseExited(_ -> pauseImage.setImage(normal));

        // Playing Labels
        Timer.setDisplayLabel(TimerDisplay, TimerPanel);
        updateKeyLabels();

        // Set Pause Menu
        PauseMenu.setVisible(false);

        // Set Game Over Menu
        isGameOver.setValue(Boolean.FALSE);
        GameOverMenu.setVisible(false);

        //Check toggle Settings
        checkToggles();

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        /* Keyboard Events */
        gamePanel.setOnKeyPressed(keyEvent -> {
            // if settings is on, shortcut keys aren't triggered
            if (rootPane.getChildren().contains(settingsPane))
                return;

            // Pause
            if (keyEvent.getCode() == getKeyCode(SaveData.getKeyEvent(KeyEventType.PAUSE))) {
                pauseGame();
                keyEvent.consume();
            }

            // New Game / Restart
            if (keyEvent.getCode() == getKeyCode(SaveData.getKeyEvent(KeyEventType.RESTART))) {
                newGame();
                keyEvent.consume();
            }

            // If game is not paused and not over
            if (!isPause.getValue() && !isGameOver.getValue()) {

                // Move Left
                if (keyEvent.getCode() == getKeyCode(SaveData.getKeyEvent(KeyEventType.LEFT))) {
                    refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                    keyEvent.consume();
                }
                // Move Right
                else if (keyEvent.getCode() == getKeyCode(SaveData.getKeyEvent(KeyEventType.RIGHT))) {
                    refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                    keyEvent.consume();
                }
                // Rotate
                else if (keyEvent.getCode() == getKeyCode(SaveData.getKeyEvent(KeyEventType.ROTATE))) {
                    refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                    keyEvent.consume();
                }
                // Move Down
                else if (keyEvent.getCode() == getKeyCode(SaveData.getKeyEvent(KeyEventType.DOWN))) {
                    moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                    keyEvent.consume();
                }
                // Hold
                else if (isHoldOn && (keyEvent.getCode() == getKeyCode(SaveData.getKeyEvent(KeyEventType.HOLD)))) {
                    refreshHoldBrick(eventListener.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER)));
                    keyEvent.consume();
                }
                else if (keyEvent.getCode() == getKeyCode(SaveData.getKeyEvent(KeyEventType.HARDDROP))) {
                    HardDrop(new MoveEvent(EventType.DOWN, EventSource.USER));
                    keyEvent.consume();
                }
            }
        });

        /* Playing Mouse Events */
        pauseImage.setOnMouseClicked(_ -> {
            Sfx.play(KeyEventType.BUTTONS);
            pauseGame();
        });

        /* Pause Mouse Events */
        Resume.setOnMouseClicked(_ -> {
            Sfx.play(KeyEventType.BUTTONS);
            pauseGame();
        });

        Pause_Restart.setOnMouseClicked(_ -> {
            Sfx.play(KeyEventType.BUTTONS);
            pauseGame();
            newGame();
        });

        Pause_Home.setOnMouseClicked(_ -> {
            Sfx.play(KeyEventType.BUTTONS);
            returnHome();
        });

        Pause_Settings.setOnMouseClicked(_ -> {
            Sfx.play(KeyEventType.BUTTONS);
            settingsPane = SettingsController.openSettings(rootPane);
        });

        /* Game Over Mouse Events*/
        GameOver_Restart.setOnMouseClicked(_ -> {
            Sfx.play(KeyEventType.BUTTONS);
            newGame();
        });

        GameOver_Home.setOnMouseClicked(_ -> {
            Sfx.play(KeyEventType.BUTTONS);
            returnHome();
        });

        GameOver_Settings.setOnMouseClicked(_ -> {
            Sfx.play(KeyEventType.BUTTONS);
            settingsPane = SettingsController.openSettings(rootPane);
        });

        Timer.start();
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        // display grid
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.web("#6C84C1"));
                rectangle.setStrokeType(StrokeType.INSIDE);
                rectangle.setStrokeWidth(0.5);
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        // display blocks that are snapped to grid
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        // Get Current Falling Brick
        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangle.setStroke(getBorderColour(brick.getBrickData()[i][j]));
                rectangle.setStrokeType(StrokeType.INSIDE);
                rectangle.setStrokeWidth(1);
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(-40 + gamePanel.getLayoutY() + (brick.getyPosition()) * BRICK_SIZE);

        rectanglesGhostPiece = new Rectangle[rectangles.length][rectangles[0].length];
        for (int i = 0; i < rectangles.length; i++) {
            for (int j = 0; j < rectangles[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangle.setStroke(getBorderColour(brick.getBrickData()[i][j]));
                rectangle.setStrokeType(StrokeType.INSIDE);
                rectangle.setStrokeWidth(1);
                rectangle.setOpacity(0.5);
                rectanglesGhostPiece[i][j] = rectangle;
                ghostPiecePanel.add(rectangle, j, i);
            }
        }
        refreshGhostPiece(brick);

        //Get Next Brick
        rectanglesNextBrick = new Rectangle[brick.getNextBrickData().length][brick.getNextBrickData()[0].length];
        for (int i = 0; i < brick.getNextBrickData().length; i++) {
            for (int j = 0; j < brick.getNextBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectanglesNextBrick[i][j] = rectangle;
                nextBrickDisplay.add(rectangle, j, i);
            }
        }
        refreshNextBrick(brick);

        // set up display for hold brick
        rectanglesHoldBrick = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                rectanglesHoldBrick[i][j] = rectangle;
                holdBrickDisplay.add(rectangle, j, i);
            }
        }

    }

    private Paint getFillColor(int i) {
        return switch (i) {
            case 0 -> Color.TRANSPARENT;
            case 1 -> Color.web("#caf0f8"); //light blue
            case 2 -> Color.web("#ccdbfd"); // blue
            case 3 -> Color.web("#d8f3dc"); // green
            case 4 -> Color.web("#f8dda4"); // orange
            case 5 -> Color.web("#ffc4d6"); // pink
            case 6 -> Color.web("#e7c6ff"); // purple
            case 7 -> Color.web("#e6ccb2"); //brown
            default -> Color.WHITE;
        };
    }

    private Paint getBorderColour(int i) {
        return switch (i) {
            case 0 -> Color.TRANSPARENT;
            case 1 -> Color.web("#90e0ef"); //light blue
            case 2 -> Color.web("#abc4ff"); // blue
            case 3 -> Color.web("#95d5b2"); // green
            case 4 -> Color.web("#f9c784"); // orange
            case 5 -> Color.web("#ffa6c1"); // pink
            case 6 -> Color.web("#c8b6ff"); // purple
            case 7 -> Color.web("#ddb892"); //brown
            default -> Color.WHITE;
        };
    }

    // get current brick display
    private void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            refreshGhostPiece(brick);
            // brick x and y position
            brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * BRICK_SIZE);
            brickPanel.setLayoutY(-40 + gamePanel.getLayoutY() + brick.getyPosition() * BRICK_SIZE);

            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }
            refreshGhostPiece(brick);
        }
    }

    // get current ghost display
    private void refreshGhostPiece(ViewData brick) {
        ghostPiecePanel.setLayoutX(gamePanel.getLayoutX() + brick.getGhostPieceXPosition() * BRICK_SIZE);
        ghostPiecePanel.setLayoutY(-40 + gamePanel.getLayoutY() + brick.getGhostPieceYPosition() * BRICK_SIZE);

        if (isPause.getValue() == Boolean.FALSE) {
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    Rectangle rectangle = rectanglesGhostPiece[i][j];
                    int value = brick.getBrickData()[i][j];

                    rectangle.setFill(getFillColor(value));
                    rectangle.setStroke(getBorderColour(value));
                    rectangle.setOpacity(0.5);
                }
            }
        }
    }

    // get next brick to display
    public void refreshNextBrick(ViewData brick){
        if (isPause.getValue() == Boolean.FALSE) {
            for (int i = 0; i < brick.getNextBrickData().length; i++) {
                for (int j = 0; j < brick.getNextBrickData()[i].length; j++) {
                    setRectangleData(brick.getNextBrickData()[i][j], rectanglesNextBrick[i][j]);
                }
            }
        }
    }

    // get hold brick display
    public void refreshHoldBrick(ViewData brick){
        if (brick == null || isPause.getValue() == Boolean.TRUE) {
            return;
        }
        if (brick.getHoldBrickData() != null) {
            for (int i = 0; i < brick.getHoldBrickData().length; i++) {
                for (int j = 0; j < brick.getHoldBrickData()[i].length; j++) {
                    setRectangleData(brick.getHoldBrickData()[i][j], rectanglesHoldBrick[i][j]);
                }
            }
            refreshNextBrick(brick);
        }
    }

    // get reset brick display
    public void resetHoldBrickDisplay() {
        if (rectanglesHoldBrick == null) return;

        for (Rectangle[] value : rectanglesHoldBrick) {
            for (Rectangle rectangle : value) {
                setRectangleData(0, rectangle); // 0 = transparent cell
            }
        }
    }

    // get display of the bricks snapped to the background
    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    // display the tetris blocks' shape, color, and rounded corner
    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setStroke(getBorderColour(color));
        rectangle.setStrokeType(StrokeType.INSIDE);
        rectangle.setStrokeWidth(1);
    }

    // move block down
    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event, false);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(downData.getViewData());
            refreshGhostPiece(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    // display score when a row is cleared
    private void HardDrop(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event, true);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    // get the keyboard shortcut from save file
    private KeyCode getKeyCode(int saveDataLine){
        try {
            return SaveData.ReadKeyCode(saveDataLine);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // check if display is on
    private void checkToggles (){
        try {
            isHoldOn = SaveData.ReadBoolean(SaveData.getKeyEvent(KeyEventType.TOGGLE_HOLD));
            HoldPanel.setVisible(isHoldOn);
            HoldPanel.setManaged(isHoldOn);

            if (!SaveData.ReadBoolean(SaveData.getKeyEvent(KeyEventType.TOGGLE_NEXT))) {
                NextPanel.setVisible(false);
                NextPanel.setManaged(false);
            }
            else {
                NextPanel.setVisible(true);
                NextPanel.setManaged(true);
            }

            if (!SaveData.ReadBoolean(SaveData.getKeyEvent(KeyEventType.TOGGLE_GHOST))) {
                ghostPiecePanel.setVisible(false);
                ghostPiecePanel.setManaged(false);
            }else {
                ghostPiecePanel.setVisible(true);
                ghostPiecePanel.setManaged(true);
            }

            if (!SaveData.ReadBoolean(SaveData.getKeyEvent(KeyEventType.TOGGLE_CONTROLS))) {
                KeyboardKeys.setVisible(false);
            }else {
                KeyboardKeys.setVisible(true);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

// Event
    // pause the game
    private void pauseGame() {
        gamePanel.requestFocus();
        if (isPause.getValue() == true){
            isPause.setValue(Boolean.FALSE);
        } else if (isPause.getValue() == false){
            isPause.setValue(Boolean.TRUE);
        }
        Timer.pause(isPause.getValue());
        PauseMenu.setVisible(isPause.getValue());
        pauseImage.setVisible(!isPause.getValue());
        checkToggles();
        updateKeyLabels();
    }

    // create new game
    private void newGame() {
        timeLine.stop();
        GameOverMenu.setVisible(false);
        PauseMenu.setVisible(false);
        pauseImage.setVisible(true);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
        resetHoldBrickDisplay();
        Timer.reset();
    }

    // show game over
    public void gameOver() {
        timeLine.stop();
        GameOverMenu.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
        Timer.stop();
    }

    // go back to home menu
    private void returnHome(){
        try {
            Timer.stop();

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/homeLayout.fxml")
            );
            Parent home = loader.load();

            Stage stage = (Stage) Pause_Home.getScene().getWindow();
            stage.setScene(new Scene(home));

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // Labels
    public void bindScore(IntegerProperty integerProperty) {
        scoreLabel.textProperty().bind(integerProperty.asString("Score: %d"));
    }

    public void bindTotalClearedLines(IntegerProperty integerProperty) {
        totalClearedLinesLabel.textProperty().bind(integerProperty.asString("Lines: %d"));
    }

    public void bindLevel(IntegerProperty integerProperty) {
        levelLabel.textProperty().bind(integerProperty.asString("Level: %d"));
    }

    public void updateKeyLabels() {
        try {
            LeftKeyLabel.setText("Left : " +SaveData.ReadKeyCode(SaveData.getKeyEvent(KeyEventType.LEFT)).getName());
            RightKeyLabel.setText("Right : " +SaveData.ReadKeyCode(SaveData.getKeyEvent(KeyEventType.RIGHT)).getName());
            RotateKeyLabel.setText("Rotate : " +SaveData.ReadKeyCode(SaveData.getKeyEvent(KeyEventType.ROTATE)).getName());
            DownKeyLabel.setText("Down : " +SaveData.ReadKeyCode(SaveData.getKeyEvent(KeyEventType.DOWN)).getName());
            HoldKeyLabel.setText("Hold : " +SaveData.ReadKeyCode(SaveData.getKeyEvent(KeyEventType.HOLD)).getName());
            PauseKeyLabel.setText("Pause : " +SaveData.ReadKeyCode(SaveData.getKeyEvent(KeyEventType.PAUSE)).getName());
            RestartKeyLabel.setText("Restart : " +SaveData.ReadKeyCode(SaveData.getKeyEvent(KeyEventType.RESTART)).getName());
            HardDropKeyLabel.setText("HardDrop : " +SaveData.ReadKeyCode(SaveData.getKeyEvent(KeyEventType.HARDDROP)).getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//

    //setters
    public void setSpeed(int speed) {

        // Reset the timeline to fit the new speed
        if (timeLine != null) {
            timeLine.stop();
        }

        // Create a new timeline with updated speed
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(speed),
                _ -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }
//
}
