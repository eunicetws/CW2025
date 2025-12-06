package com.comp2042.interfaces;

import com.comp2042.data.ClearRow;
import com.comp2042.view.ViewData;
import com.comp2042.data.Level;
import com.comp2042.data.LinesCleared;
import com.comp2042.data.Score;

/**
 * This interface defines the operations for controlling bricks,
 * managing the game state, and retrieving game information.
 */
public interface Board {
//Brick Controls
    /**
     * Moves the current brick down.
     *
     * @return {@code true} if the brick successfully moved; {@code false} if it cannot move further
     */
    boolean moveBrickDown();

    /**
     * Moves the current brick to the left.
     *
     * @return {@code true} if the move is successful; {@code false} otherwise
     */
    boolean moveBrickLeft();

    /**
     * Moves the current brick to the right.
     *
     * @return {@code true} if the move is successful; {@code false} otherwise
     */
    boolean moveBrickRight();

    /**
     * Rotates the current brick to counter-clockwise.
     *
     * @return {@code true} if the rotation is successful; {@code false} otherwise
     */
    boolean rotateLeftBrick();

    /**
     * Holds the current brick and swapping it with the previously held brick.
     *
     * @return {@code true} if the hold is successful; {@code false} otherwise
     */
    boolean holdBrick();

    /**
     * Creates a new brick at the specified coordinates.
     *
     * @param x the x-coordinate on the board
     * @param y the y-coordinate on the board
     * @return {@code true} if the brick was successfully created; {@code false} otherwise
     */
    boolean createNewBrick(int x, int y);

    /**
     * Updates the position of the ghost piece.
     */
    void moveGhostPiece();

    /**
     * Merges the current brick into the board matrix.
     */
    void mergeBrickToBackground();

    /**
     * Clears any filled rows and returns information about the cleared rows.
     *
     * @return a {@link ClearRow} object containing cleared row data
     */
    ClearRow clearRows();

// Getters
    /**
     * Returns the current board matrix representing the placed and active bricks.
     *
     * @return a 2D integer array representing the board
     */
    int[][] getBoardMatrix();

    /**
     * Returns visual information required for rendering game.
     *
     * @return {@link ViewData} object containing rendering information
     */
    ViewData getViewData();

    /**
     * Returns the current score of the player.
     *
     * @return a {@link Score} object containing the player's score
     */
    Score getScore();


    /**
     * Returns the total number of lines cleared by the player.
     *
     * @return a {@link LinesCleared} containing the line count
     */
    LinesCleared getTotalLinesCleared();

    /**
     * Returns the current level of the player.
     *
     * @return a {@link Level} containing the player's level
     */
    Level getLevel();

    /**
     * Resets the board and starts a new game.
     */
    void newGame();
}
