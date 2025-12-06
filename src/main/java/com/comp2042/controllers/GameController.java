package com.comp2042.controllers;

import com.comp2042.enums.EventSource;
import com.comp2042.interfaces.Board;
import com.comp2042.interfaces.InputEventListener;
import com.comp2042.data.ClearRow;
import com.comp2042.data.DownData;
import com.comp2042.data.MoveEvent;
import com.comp2042.logic.SimpleBoard;
import com.comp2042.view.ViewData;

/**
 * Manages the user input and gameplay logic during the gameplay.
 *
 * <p>
 * It connects the game logic implemented in {@link Board} and the
 * game interface provided by {@link GuiController}.
 * User actions are process through {@link Board}.
 * Game interface is updated using {@link GuiController} and {@link ViewData}.
 * </p>
 *
 * <p>
 * The class is responsible for:
 * </p>
 * <ul>
 *     <li>Handling the brick movement.</li>
 *     <li>Managing level progression and game speed.</li>
 *     <li>Refreshing the GUI display.</li>
 *     <li>Detecting game-over.</li>
 * </ul>
 */

public class GameController implements InputEventListener {

    /**
     * Initiate the game board size and game logic.
     */
    private final Board board = new SimpleBoard(25, 10);

    /**
     * The controller for managing and updating the game's GUI elements.
     */
    private final GuiController viewGuiController;


    /**
     * Initialises the gameplay, the GUI and the game board.
     * <p>Creates a new {@code GameController} and initialise the game logic from {@link SimpleBoard}
     * and the GUI from {@link GuiController}</p>
     *
     * @param guiController the {@link GuiController} that is responsible for the game's GUI interactions
     */
    public GameController(GuiController guiController) {
        this.viewGuiController = guiController;
        board.createNewBrick(4, 1);
        board.moveGhostPiece();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
        viewGuiController.bindTotalClearedLines(board.getTotalLinesCleared().PropertiesTotalLinesCleared());
        viewGuiController.bindLevel(board.getLevel().propertiesLevel());
        viewGuiController.setSpeed(board.getLevel().calcSpeed());
    }

    /**
     * Handles the downward movement of the current brick, including both hard drop and soft drop actions.
     * <p>
     *     A hard drop instantly moves the current brick to the bottom of the board,
     *     while a soft drop moves it down by one unit along the y-axis.
     * </p>
     * <p>
     *     If the move is triggered by a user event, the score is increased by one for each downward movement.
     *     Each time the brick moves, the method checks whether it can continue moving down.
     *     If it cannot, the brick is merged into the game matrix via the {@link Board}.
     *     After merging, the board checks for any completed rows, removes them from the matrix,
     *     return the updated matrix along with the number of rows cleared and
     *     create a new brick with their ghost piece.
     * </p>
     * <p> If the new brick intersects with the game matrix, it will tell
     *     the {@link GuiController} the game is over.
     * </p>
     * <p>
     *     The player's level is recalculated based on the number of cleared rows, and the game speed is adjusted
     *     accordingly.
     * </p>
     *
     * @param event the {@link MoveEvent} verify if it is a user input
     * @param isHardDrop {@code true} if performing a hard drop; {@code false} for a soft drop
     * @return a {@link DownData} object contains the data of the cleared rows and the updated view data
     */

    @Override
    public DownData onDownEvent(MoveEvent event, boolean isHardDrop) {
        boolean canMove = true;
        if (isHardDrop){
            while (canMove) {
                canMove = board.moveBrickDown();
                board.getScore().add(1);
            }
        }
        if (!isHardDrop) {
            canMove = board.moveBrickDown();
        }

        ClearRow clearRow = null;
        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus()); //Add Score
                board.getTotalLinesCleared().add(clearRow.getLinesRemoved()); //Add Lines
                board.moveGhostPiece();

                // Level Up
                if (board.getLevel().reachLevelRequirement(board.getTotalLinesCleared().PropertiesTotalLinesCleared().getValue())){
                    board.getLevel().levelUp();
                    viewGuiController.setSpeed(board.getLevel().calcSpeed());
                }
            }
            if (board.createNewBrick(4, 1)) {
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());
            viewGuiController.refreshNextBrick(board.getViewData());
            board.moveGhostPiece();

        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        }
        return new DownData(clearRow, board.getViewData());
    }

    /**
     * Moves the current brick one unit to the left on the x-axis.
     *
     * <p>
     * After moving the brick, the ghost piece is recalculated to show where the brick
     * will land using {@link Board}.
     * </p>
     *
     * @param event the {@link MoveEvent} representing the user input or game thread event
     * @return {@link ViewData} containing the updated positions of the current brick and ghost piece
     */

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        board.moveGhostPiece();
        return board.getViewData();
    }

    /**
     * Moves the current brick one unit to the right on the x-axis.
     *
     * <p>
     * After moving the brick, the ghost piece is recalculated to show where the brick
     * will land using {@link Board}.
     * </p>
     *
     * @param event the {@link MoveEvent} representing the user input or game thread event
     * @return {@link ViewData} containing the updated positions of the current brick and ghost piece
     */

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        board.moveGhostPiece();
        return board.getViewData();
    }

    /**
     * Rotates the current brick 90 degrees clockwise.
     *
     * <p>
     * {@link Board}is used to rotate the brick and recalculated the ghost piece to reflect the rotated brick's
     * new landing position.The method ensures the both bricks stays within the bounds of the board and does
     * not overlap existing blocks.
     * </p>
     *
     * @param event the {@link MoveEvent} representing the user input or game thread event
     * @return {@link ViewData} containing the updated positions of the current brick and ghost piece
     */

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        board.moveGhostPiece();
        return board.getViewData();
    }

    /**
     * Attempts to hold the current brick.
     *
     * <p>
     *  This method uses {@link Board} to get the hold brick.
     *  If a brick is already held, the current and held bricks are swapped.
     *  If there is no brick that is currently in the hold slot,
     *  the current brick is placed there and a new brick is spawned.
     *  The {@link ViewData} is updated to reflect the held piece.
     * </p>
     *
     * @param event the {@link MoveEvent} triggering the hold action
     * @return the updated {@link ViewData} if a brick is held successfully; {@code null} if hold is not allowed
     */

    public ViewData onHoldEvent(MoveEvent event) {
        if(board.holdBrick()) {
            board.getViewData().refreshHoldBrickData();
            return board.getViewData();
        } else {return null;}
    }

    /**
     * Resets the game board and initializes a new game.
     *
     * <p>
     * This method clears the board, spawns a new brick using {@link Board} and refreshes the GUI display
     * via {@link GuiController}. All scores, levels, and timers are reset.
     * </p>
     */

    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }

    /**
     * Returns the current {@link Board} instance used by this {@code GameController}.
     *
     * <p>
     * This method is primarily used for testing or debugging purposes.
     * </p>
     *
     * @return the {@link Board} instance currently used by the game controller
     */

    public Board getBoard(){
        return this.board;
    }
}
