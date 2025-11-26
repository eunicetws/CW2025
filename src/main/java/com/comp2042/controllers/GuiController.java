package com.comp2042.controllers;

import com.comp2042.enums.EventSource;
import com.comp2042.enums.EventType;
import com.comp2042.data.SaveData;
import com.comp2042.enums.KeyEventType;
import com.comp2042.interfaces.InputEventListener;
import com.comp2042.logic.DownData;
import com.comp2042.logic.MoveEvent;
import com.comp2042.view.NotificationPanel;
import com.comp2042.view.ViewData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
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

    //Pause Menu
    @FXML private StackPane PauseMenu, Resume;
    @FXML private Label Pause_Restart, Pause_Home, Pause_Settings;

    // Game Over
    @FXML private StackPane GameOverMenu;
    @FXML private Label GameOver_Restart, GameOver_Home, GameOver_Settings;

    //Settings

    private Rectangle[][] displayMatrix, rectangles, rectanglesNextBrick, rectanglesHoldBrick, rectanglesGhostPiece;

    private InputEventListener eventListener;

    private Timeline timeLine;

    private int currentScore;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    Parent settingsPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set Pause Button in playing
        Image normal = new Image("/images/buttons/Buttons-pause.png");
        Image hover = new Image("/images/buttons/Buttons-pause-pressed.png");
        pauseImage.setImage(normal);
        pauseImage.setOnMouseEntered(e -> pauseImage.setImage(hover));
        pauseImage.setOnMouseExited(e -> pauseImage.setImage(normal));

        // Set Pause Menu
        PauseMenu.setVisible(false);

        // Set Game Over Menu
        isGameOver.setValue(Boolean.FALSE);
        GameOverMenu.setVisible(false);

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        /* Keyboard Events */
        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
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
                    else if (keyEvent.getCode() == getKeyCode(SaveData.getKeyEvent(KeyEventType.HOLD))) {
                        refreshHoldBrick(eventListener.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER)));
                        keyEvent.consume();
                    }
                    else if (keyEvent.getCode() == getKeyCode(SaveData.getKeyEvent(KeyEventType.HARDDROP))) {
                        HardDrop(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                }
            }
        });

        /* Playing Mouse Events */
        pauseImage.setOnMouseClicked(e -> pauseGame());

        /* Pause Mouse Events */
        Resume.setOnMouseClicked(e -> pauseGame());

        Pause_Restart.setOnMouseClicked(e -> {
            pauseGame();
            newGame();
        });

        Pause_Home.setOnMouseClicked(e -> returnHome());

        Pause_Settings.setOnMouseClicked(e -> settingsPane = SettingsController.openSettings(rootPane));

        /* Game Over Mouse Events*/
        GameOver_Restart.setOnMouseClicked(e -> newGame());

        GameOver_Home.setOnMouseClicked(e -> returnHome());

        GameOver_Settings.setOnMouseClicked(e -> settingsPane = SettingsController.openSettings(rootPane));

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

    public void resetHoldBrickDisplay() {
        if (rectanglesHoldBrick == null) return;

        for (int i = 0; i < rectanglesHoldBrick.length; i++) {
            for (int j = 0; j < rectanglesHoldBrick[i].length; j++) {
                setRectangleData(0, rectanglesHoldBrick[i][j]); // 0 = transparent cell
            }
        }
    }

    // get display of teh bricks snapped to the background
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

    // display score when a row is cleared
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


    // Event
    // pause the game
    private void pauseGame() {
        gamePanel.requestFocus();
        if (isPause.getValue() == true){
            isPause.setValue(Boolean.FALSE);
        } else if (isPause.getValue() == false){
            isPause.setValue(Boolean.TRUE);
        }
        PauseMenu.setVisible(isPause.getValue());
        pauseImage.setVisible(!isPause.getValue());
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
    }

    // show game over
    public void gameOver() {
        timeLine.stop();
        GameOverMenu.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
    }

    // go back to home menu
    private void returnHome(){
        try {
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
                e -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }
//
}
