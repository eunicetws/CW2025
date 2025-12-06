package com.comp2042.controllers;

import com.comp2042.logic.Timer;
import com.comp2042.enums.EventSource;
import com.comp2042.enums.EventType;
import com.comp2042.data.SaveData;
import com.comp2042.enums.SaveDataType;
import com.comp2042.interfaces.InputEventListener;
import com.comp2042.data.DownData;
import com.comp2042.data.MoveEvent;
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

/**
 * This controller manages all graphical user interface (GUI) operations
 * for the gameplay display in the Tetris-style game.
 *
 * <p>
 *  It is responsible for initializing, rendering, and updating all visual elements
 *  of the game, including:
 * </p>
 * <ul>
 *   <li>The main game board (grid and current falling brick)</li>
 *   <li>The ghost piece showing where the current brick will land</li>
 *   <li>The next brick preview and held brick panel</li>
 *   <li>Score, level, and total cleared lines labels</li>
 *   <li>Pause menu and game-over menu overlays</li>
 *   <li>Keyboard shortcuts and keybinding display</li>
 * </ul>
 *
 * <p>
 * The class uses {@link InputEventListener} to forward user input events to the game logic
 * and {@link Timer} to control the in-game timing of brick drops. Mouse interactions
 * trigger sound effects via {@link Sfx} and may also open the settings menu through
 * {@link SettingsController}.
 * </p>
 *
 * <p>
 * The GUI components are defined in the FXML layout file referenced by {@code gameLayout.fxml},
 * which should be linked to this controller using {@code fx:controller}.
 * </p>
 *
 * <p>
 * The controller handles both "soft drop" and "hard drop" actions, updates the display
 * matrices for the current brick, next brick, and hold brick, and ensures all updates
 * respect the game's paused or game-over state.
 * </p>
 *
 * <p>
 * This class is uses {@link ViewData} to stores the position and color
 * information for bricks, and {@link SaveData} to get user keybindings and toggle preferences.
 * </p>
 */



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
            HoldKeyLabel, PauseKeyLabel, RestartKeyLabel, HardDropKeyLabel, HighScoreDisplay;
    @FXML private VBox TimerPanel, NextPanel, HoldPanel, KeyboardKeys;

    //Pause Menu
    @FXML private StackPane PauseMenu, Resume;
    @FXML private Label Pause_Restart, Pause_Home, Pause_Settings;

    // Game Over
    @FXML private StackPane GameOverMenu;
    @FXML private Label GameOver_Restart, GameOver_Home, GameOver_Settings;

// Playing
    /**
     * The game board matrix that displays all bricks currently on the board.
     */
    private Rectangle[][] displayMatrix;

    /**
     * The matrix representing the current falling brick.
     */
    private Rectangle[][] rectangles;

    /**
     * The matrix showing the next upcoming brick in the queue.
     */
    private Rectangle[][] rectanglesNextBrick;

    /**
     * The matrix showing the currently held brick, if any.
     */
    private Rectangle[][] rectanglesHoldBrick;

    /**
     * The matrix used to display the ghost piece's projected landing position.
     */
    private Rectangle[][] rectanglesGhostPiece;

    /**
     * Handles all input events triggered by the player during gameplay.
     */
    private InputEventListener eventListener;

    /**
     * Controls the game's main update loop, triggering movement and logic at fixed intervals.
     */
    private Timeline timeLine;

    /**
     * Indicates whether the player has used the hold action during the current turn.
     */
    private boolean isHoldOn;

    /**
     * Represents whether the game is currently paused.
     */
    private final BooleanProperty isPause = new SimpleBooleanProperty();

    /**
     * Represents whether the game has ended.
     */
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();


    /**
     * The root container for the settings panel UI.
     */
    Parent settingsPane;

    /**
     * Initializes the GUI components and sets up keyboard and mouse events.
     * <ul>
     *   <li>Sets up keyboard controls using {@link SaveData} to retrieve the user's
     *       custom key bindings, and {@link SaveDataType} to determine which actions
     *       to trigger.</li>
     *   <li>Sets up mouse events with sound effects using {@link Sfx}.</li>
     *   <li>Starts the in-game timer.</li>
     * </ul>
     *
     * @param location  the location used to resolve relative paths
     * @param resources the resources used to localize the root object
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set Pause Button in playing
        Image normal = new Image("/images/buttons/Buttons-pause.png");
        Image hover = new Image("/images/buttons/Buttons-pause-pressed.png");
        pauseImage.setImage(normal);
        pauseImage.setOnMouseEntered(_ -> pauseImage.setImage(hover));
        pauseImage.setOnMouseExited(_ -> pauseImage.setImage(normal));

        // Playing Labels
        Timer.setLabel(TimerDisplay, TimerPanel);
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
            if (keyEvent.getCode() == getKeyCode(SaveData.getKeyEvent(SaveDataType.PAUSE))) {
                pauseGame();
                keyEvent.consume();
            }

            // New Game / Restart
            if (keyEvent.getCode() == getKeyCode(SaveData.getKeyEvent(SaveDataType.RESTART))) {
                newGame();
                keyEvent.consume();
            }

            // If game is not paused and not over
            if (!isPause.getValue() && !isGameOver.getValue()) {

                // Move Left
                if (keyEvent.getCode() == getKeyCode(SaveData.getKeyEvent(SaveDataType.LEFT))) {
                    refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                    keyEvent.consume();
                }
                // Move Right
                else if (keyEvent.getCode() == getKeyCode(SaveData.getKeyEvent(SaveDataType.RIGHT))) {
                    refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                    keyEvent.consume();
                }
                // Rotate
                else if (keyEvent.getCode() == getKeyCode(SaveData.getKeyEvent(SaveDataType.ROTATE))) {
                    refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                    keyEvent.consume();
                }
                // Move Down
                else if (keyEvent.getCode() == getKeyCode(SaveData.getKeyEvent(SaveDataType.DOWN))) {
                    moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                    keyEvent.consume();
                }
                // Hold
                else if (isHoldOn && (keyEvent.getCode() == getKeyCode(SaveData.getKeyEvent(SaveDataType.HOLD)))) {
                    refreshHoldBrick(eventListener.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER)));
                    keyEvent.consume();
                }
                else if (keyEvent.getCode() == getKeyCode(SaveData.getKeyEvent(SaveDataType.HARDDROP))) {
                    HardDrop(new MoveEvent(EventType.DOWN, EventSource.USER));
                    keyEvent.consume();
                }
            }
        });

        /* Playing Mouse Events */
        pauseImage.setOnMouseClicked(_ -> {
            Sfx.play(SaveDataType.BUTTONS);
            pauseGame();
        });

        /* Pause Mouse Events */
        Resume.setOnMouseClicked(_ -> {
            Sfx.play(SaveDataType.BUTTONS);
            pauseGame();
        });

        Pause_Restart.setOnMouseClicked(_ -> {
            Sfx.play(SaveDataType.BUTTONS);
            pauseGame();
            newGame();
        });

        Pause_Home.setOnMouseClicked(_ -> {
            Sfx.play(SaveDataType.BUTTONS);
            returnHome();
        });

        Pause_Settings.setOnMouseClicked(_ -> {
            Sfx.play(SaveDataType.BUTTONS);
            settingsPane = SettingsController.openSettings(rootPane);
        });

        /* Game Over Mouse Events*/
        GameOver_Restart.setOnMouseClicked(_ -> {
            Sfx.play(SaveDataType.BUTTONS);
            newGame();
        });

        GameOver_Home.setOnMouseClicked(_ -> {
            Sfx.play(SaveDataType.BUTTONS);
            returnHome();
        });

        GameOver_Settings.setOnMouseClicked(_ -> {
            Sfx.play(SaveDataType.BUTTONS);
            settingsPane = SettingsController.openSettings(rootPane);
        });

        Timer.start();
    }

    /**
     * Initializes the game view including background grid, current brick, ghost piece,
     * next brick display, and hold brick display.
     *
     * @param boardMatrix the current game board matrix
     * @param brick       the current brick and its associated view data
     */
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

    /**
     * Returns the fill color associated with the specified block type.
     *
     * <p>The index corresponds to predefined colors used for rendering
     * different block types in the game. If the index does not match any
     * known type, {@code Color.WHITE} is returned as the default.</p>
     *
     * <table>
     *   <tr><th>Index</th><th>Color</th></tr>
     *   <tr><td>0</td><td>Transparent</td></tr>
     *   <tr><td>1</td><td>Light Blue (#caf0f8)</td></tr>
     *   <tr><td>2</td><td>Blue (#ccdbfd)</td></tr>
     *   <tr><td>3</td><td>Green (#d8f3dc)</td></tr>
     *   <tr><td>4</td><td>Orange (#f8dda4)</td></tr>
     *   <tr><td>5</td><td>Pink (#ffc4d6)</td></tr>
     *   <tr><td>6</td><td>Purple (#e7c6ff)</td></tr>
     *   <tr><td>7</td><td>Brown (#e6ccb2)</td></tr>
     * </table>
     *
     * @param i the block type index
     * @return the corresponding {@link Paint} color, or {@code Color.WHITE} if the index is invalid
     */

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

    /**
     * Returns the darker variant of the colour compare to {@link GuiController#getFillColor}
     * associated with the specified block type as the border colour.
     *
     * <p>The index corresponds to predefined colors used for rendering
     * different block types in the game. If the index does not match any
     * known type, {@code Color.WHITE} is returned as the default.</p>
     *
     * <table>
     *   <tr><th>Index</th><th>Border Color</th></tr>
     *   <tr><td>0</td><td>Transparent</td></tr>
     *   <tr><td>1</td><td>Light Blue (#90e0ef)</td></tr>
     *   <tr><td>2</td><td>Blue (#abc4ff)</td></tr>
     *   <tr><td>3</td><td>Green (#95d5b2)</td></tr>
     *   <tr><td>4</td><td>Orange (#f9c784)</td></tr>
     *   <tr><td>5</td><td>Pink (#ffa6c1)</td></tr>
     *   <tr><td>6</td><td>Purple (#c8b6ff)</td></tr>
     *   <tr><td>7</td><td>Brown (#ddb892)</td></tr>
     * </table>
     *
     * @param i the block type index
     * @return the corresponding {@link Paint} color, or {@code Color.WHITE} if the index is invalid
     */

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

    /**
     * Refreshes the display of the current brick.
     *
     * <p>
     * Using the GridPane defined in the FXML layout, this method updates the
     * brick's display matrix with {@code setRectangleData} and the layout position (x and y coordinates) based on its {@link ViewData},
     * and also refreshes the rendering of its ghost piece.
     * </p>
     *
     * @param brick the {@link ViewData} representing the current brick
     */

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

    /**
     * Refreshes the display of the ghost piece.
     *
     * <p>
     * This method updates the ghost piece's display matrix with {@code setRectangleData} based on the current brick,
     * using the GridPane defined in the FXML layout. The layout position (x and y coordinates)
     * of the ghost piece is set according to
     * {@link ViewData#getGhostPieceXPosition()} and {@link ViewData#getGhostPieceYPosition()}.
     * </p>
     *
     * @param brick the {@link ViewData} representing the current brick
     */

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

    /**
     * Refreshes the display of the next piece.
     *
     * <p>
     * This method updates the next piece's display matrix based on
     * {@link ViewData#getNextBrickData()}, using the GridPane defined in the FXML layout
     * with {@code setRectangleData}.
     * The layout position (x and y coordinates) remains unchanged.
     * </p>
     *
     * @param brick the {@link ViewData} representing the current brick's data
     */

    public void refreshNextBrick(ViewData brick){
        if (isPause.getValue() == Boolean.FALSE) {
            for (int i = 0; i < brick.getNextBrickData().length; i++) {
                for (int j = 0; j < brick.getNextBrickData()[i].length; j++) {
                    setRectangleData(brick.getNextBrickData()[i][j], rectanglesNextBrick[i][j]);
                }
            }
        }
    }

    /**
     * Refreshes the display of the hold piece.
     *
     * <p>
     * This method displays the hold piece's display with the
     * {@link ViewData#getHoldBrickData()}, using the GridPane defined in the FXML layout
     * with {@code setRectangleData}.
     * The layout position (x and y coordinates) remains unchanged.
     * </p>
     *
     * @param brick the {@link ViewData} representing the current brick's data
     */

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

    /**
     * Refreshes the hold-piece display by rendering an empty grid.
     *
     * <p>
     * This method resets the visual display of the held panel by replacing
     * the current display matrix with a transparent grid using {@code setRectangleData}.
     * The layout position (x and y coordinates) remains unchanged.
     * </p>
     */

    public void resetHoldBrickDisplay() {
        if (rectanglesHoldBrick == null) return;

        for (Rectangle[] value : rectanglesHoldBrick) {
            for (Rectangle rectangle : value) {
                setRectangleData(0, rectangle); // 0 = transparent cell
            }
        }
    }

    /**
     * Refreshes the game background display based on the current board state.
     *
     * <p>
     * This method iterates through the board matrix and updates each
     * corresponding brick data in the display matrix using {@code setRectangleData}.
     * </p>
     *
     * @param board the matrix of the current board state
     */

    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    /**
     * Updates the display of a single brick cell.
     *
     * <p>
     * This method sets the fill color, border color, and styling of the given
     * {@code Rectangle} based on the provided color value.
     * The fill color is obtained from {@link GuiController#getFillColor(int)},
     * and the border color is obtained from {@link GuiController#getBorderColour(int)}.
     * </p>
     *
     * @param color the value used to determine the rectangle's fill and border colors
     * @param rectangle the rectangle representing a single cell in the game grid
     */

    // display the tetris blocks' shape, color, and rounded corner
    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setStroke(getBorderColour(color));
        rectangle.setStrokeType(StrokeType.INSIDE);
        rectangle.setStrokeWidth(1);
    }

    /**
     * Handles a soft drop action, moving the active brick downward by one tile.
     *
     * <p>
     * If the game is not paused, this method calls
     * {@link InputEventListener#onDownEvent(MoveEvent, boolean)} to perform the downward
     * movement, passing {@code false} to indicate that the action not a hard drop.
     * If there is any rows cleared, a score notification will is displayed based on how many
     * rows were removed.
     * </p>
     *
     * <p>
     * Once the movement is processed, this method updates the display
     * by calling {@link GuiController#refreshBrick(ViewData)} to refresh the brick's and ghost piece's visual.
     * </p>
     *
     * @param event the {@link MoveEvent} representing the soft drop action
     */

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
        }
        gamePanel.requestFocus();
    }

    /**
     * Handles the hard-drop action, instantly dropping the active brick to the lowest
     * valid position.
     *
     * <p>
     * If the game is not paused, this method calls
     * {@link InputEventListener#onDownEvent(MoveEvent, boolean)} to perform the downward
     * movement, passing {@code true} to indicate that is a hard drop.
     * If there is any rows cleared, a score notification will is displayed based on how many
     * rows were removed.
     * </p>
     *
     * <p>
     * Once the movement is processed, this method updates the display
     * by calling {@link GuiController#refreshBrick(ViewData)} to refresh the brick's and ghost piece's visual.
     * </p>
     *
     * @param event the {@link MoveEvent} representing the soft drop action
     */

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

    /**
     * Retrieves a keyboard key binding from the save file.
     *
     * <p>
     * This method calls {@link SaveData#ReadKeyCode(int)} to load the key associated with
     * a specific action, based on the line number provided.
     * </p>
     *
     * @param saveDataLine the line number in the save data that stores the key binding
     * @return the {@link KeyCode} assigned to the action
     * @throws RuntimeException if the save file cannot be read
     */

    private KeyCode getKeyCode(int saveDataLine){
        try {
            return SaveData.ReadKeyCode(saveDataLine);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the visibility of various UI panels based on the user's saved toggle preferences.
     *
     * <p>
     * This method reads boolean values from {@link SaveData} to determine whether
     * the hold panel, next panel, ghost piece, or keyboard controls should be displayed.
     * Panels will be shown if the value is true and hidden if the value is false.
     * </p>
     *
     * @throws RuntimeException if toggle data cannot be read from the save file
     */

    private void checkToggles (){
        try {
            isHoldOn = SaveData.ReadBoolean(SaveData.getKeyEvent(SaveDataType.TOGGLE_HOLD));
            HoldPanel.setVisible(isHoldOn);
            HoldPanel.setManaged(isHoldOn);

            if (!SaveData.ReadBoolean(SaveData.getKeyEvent(SaveDataType.TOGGLE_NEXT))) {
                NextPanel.setVisible(false);
                NextPanel.setManaged(false);
            }
            else {
                NextPanel.setVisible(true);
                NextPanel.setManaged(true);
            }

            if (!SaveData.ReadBoolean(SaveData.getKeyEvent(SaveDataType.TOGGLE_GHOST))) {
                ghostPiecePanel.setVisible(false);
                ghostPiecePanel.setManaged(false);
            }else {
                ghostPiecePanel.setVisible(true);
                ghostPiecePanel.setManaged(true);
            }

            if (!SaveData.ReadBoolean(SaveData.getKeyEvent(SaveDataType.TOGGLE_CONTROLS))) {
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
    /**
     * Manages the paused state of the game.
     *
     * <p>
     * This method toggles the pause flag, instructs the {@link Timer} to pause or resume,
     * and updates the visibility of the pause menu accordingly. If the player has changed
     * any settings, the UI toggles and key binding labels are refreshed to reflect the
     * updated configuration. After all updates are applied, focus is returned to the game
     * panel.
     * </p>
     */

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
    /**
     * Displays the game-over screen and stops all gameplay activity.
     *
     * <p>
     * This method stops the timeline, shows the game-over menu, updates the
     * game-over state flag and stops {@link Timer}.
     * </p>
     */

    public void gameOver() {
        timeLine.stop();
        GameOverMenu.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
        Timer.stop();
    }

    /**
     * Returns the player to the home menu screen.
     *
     * <p>
     * This method stops {@link Timer}, loads the home screen layout, and replaces
     * the current scene. Any loading errors will return {@link RuntimeException}.
     * </p>
     */

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
    /**
     * Binds the score label to an integer property so that it automatically updates
     * whenever the score changes.vcrcrrv
     *
     * @param integerProperty the {@link javafx.beans.property.IntegerProperty} representing the current score
     */

    public void bindScore(IntegerProperty integerProperty) {
        scoreLabel.textProperty().bind(integerProperty.asString("Score: %d"));
        HighScoreDisplay.textProperty().bind(integerProperty.asString("%d"));
    }

    /**
     * Binds the total cleared lines label to an integer property so that it automatically updates
     * whenever the total lines cleared changes.
     *
     * @param integerProperty the {@link javafx.beans.property.IntegerProperty} representing the number
     *                        of lines cleared
     */

    public void bindTotalClearedLines(IntegerProperty integerProperty) {
        totalClearedLinesLabel.textProperty().bind(integerProperty.asString("Lines: %d"));
    }

    /**
     * Binds the level label to an integer property so that it automatically updates
     * whenever the level changes.
     *
     * @param integerProperty the {@link javafx.beans.property.IntegerProperty} representing the current level
     */

    public void bindLevel(IntegerProperty integerProperty) {
        levelLabel.textProperty().bind(integerProperty.asString("Level: %d"));
    }

    /**
     * Sets the label text to inform the user of the current control shortcuts.
     *
     * <p>
     * Retrieves the user's custom key bindings using {@link SaveData} and determines
     * which keys to display using {@link SaveDataType}.
     * </p>
     */

    public void updateKeyLabels() {
        try {
            LeftKeyLabel.setText("Left : " +SaveData.ReadKeyCode(SaveData.getKeyEvent(SaveDataType.LEFT)).getName());
            RightKeyLabel.setText("Right : " +SaveData.ReadKeyCode(SaveData.getKeyEvent(SaveDataType.RIGHT)).getName());
            RotateKeyLabel.setText("Rotate : " +SaveData.ReadKeyCode(SaveData.getKeyEvent(SaveDataType.ROTATE)).getName());
            DownKeyLabel.setText("Down : " +SaveData.ReadKeyCode(SaveData.getKeyEvent(SaveDataType.DOWN)).getName());
            HoldKeyLabel.setText("Hold : " +SaveData.ReadKeyCode(SaveData.getKeyEvent(SaveDataType.HOLD)).getName());
            PauseKeyLabel.setText("Pause : " +SaveData.ReadKeyCode(SaveData.getKeyEvent(SaveDataType.PAUSE)).getName());
            RestartKeyLabel.setText("Restart : " +SaveData.ReadKeyCode(SaveData.getKeyEvent(SaveDataType.RESTART)).getName());
            HardDropKeyLabel.setText("HardDrop : " +SaveData.ReadKeyCode(SaveData.getKeyEvent(SaveDataType.HARDDROP)).getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //setters

    /**
     * Sets the drop speed for the falling bricks.
     *
     * <p>
     * This method redefines the timeline whenever the speed needs to be changed.
     * A shorter timeline interval results in faster falling blocks.
     * </p>
     *
     * @param speed the drop interval in milliseconds
     */

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

    /**
     * Sets the {@link InputEventListener} to communicate user input events to game logic.
     *
     * @param eventListener the listener handling input events
     */
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }
//
}
