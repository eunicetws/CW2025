package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Labeled;
import javafx.scene.effect.Reflection;
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
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;

    //Playing
    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GridPane nextBrickDisplay;

    @FXML
    private GridPane holdBrickDisplay;

    @FXML
    private GameOverPanel gameOverPanel;

    @FXML
    private ImageView pauseImage;

    @FXML
    private Labeled scoreLabel;

    @FXML
    private Labeled totalClearedLinesLabel;

    @FXML
    private Labeled levelLabel;

    //Pause Menu
    @FXML
    private StackPane PauseMenu;

    @FXML
    private StackPane Resume;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private Rectangle[][] rectanglesNextBrick;

    private Rectangle[][] rectanglesHoldBrick;

    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set Pause Button in playing
        Image normal = new Image("/images/buttons/Buttons-pause.png");
        Image hover = new Image("/images/buttons/Buttons-pause-pressed.png");
        pauseImage.setImage(normal);
        pauseImage.setOnMouseEntered(e -> pauseImage.setImage(hover));
        pauseImage.setOnMouseExited(e -> pauseImage.setImage(normal));

        // Set pause Menu
        PauseMenu.setVisible(false);


        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        /* Keyboard Events */
        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ESCAPE || keyEvent.getCode() == KeyCode.P) {
                    pauseGame(null);
                    keyEvent.consume();
                }
                if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                    if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                        refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                        refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                        refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.H) {
                        refreshHoldBrick(eventListener.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER)));
                        keyEvent.consume();
                    }
                }
                if (keyEvent.getCode() == KeyCode.N) {
                    newGame(null);
                }
            }
        });

        /* Mouse Events */
        pauseImage.setOnMouseClicked(e -> pauseGame(null));
        Resume.setOnMouseClicked(e -> pauseGame(null));


        gameOverPanel.setVisible(false);

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.web("#ede0d4"));
                rectangle.setStrokeType(StrokeType.INSIDE);
                rectangle.setStrokeWidth(0.5);
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        // Get Current Brick
        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangle.setStroke(getBorderColour(brick.getBrickData()[i][j]));
                rectangle.setStrokeWidth(1);
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(-40 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);

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
        Paint returnPaint = switch (i) {
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
        return returnPaint;
    }

    private Paint getBorderColour(int i) {
        Paint returnPaint = switch (i) {
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
        return returnPaint;
    }


    private void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            // brick x and y position
            brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
            brickPanel.setLayoutY(-40 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);

            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
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

    public void refreshHoldBrick(ViewData brick){
        if (isPause.getValue() == Boolean.FALSE && brick.getHoldBrickData() != null) {
            for (int i = 0; i < brick.getHoldBrickData().length; i++) {
                for (int j = 0; j < brick.getHoldBrickData()[i].length; j++) {
                    setRectangleData(brick.getHoldBrickData()[i][j], rectanglesHoldBrick[i][j]);
                }
            }
            refreshNextBrick(brick);
        }
    }

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
            DownData downData = eventListener.onDownEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }


    // Event
    public void pauseGame(ActionEvent actionEvent) {
        gamePanel.requestFocus();
        if (isPause.getValue() == true){
            isPause.setValue(Boolean.FALSE);
        } else if (isPause.getValue() == false){
            isPause.setValue(Boolean.TRUE);
        }
        PauseMenu.setVisible(isPause.getValue());
        pauseImage.setVisible(!isPause.getValue());
    }

    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }
//

    // Label
    public void gameOver() {
        timeLine.stop();
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
    }

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
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }
//
}
