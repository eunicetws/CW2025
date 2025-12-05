package com.comp2042.view;

import com.comp2042.logic.MatrixOperations;

import java.awt.*;

/**
 * This class represents all visual data required by the GUI to render the current game state.
 * This includes the active brick shape and position, the next brick, the held brick,
 * and the ghost piece's projected position.
 */

public final class ViewData {

    private int[][] brickData;
    private int[][] holdBrickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;
    Point ghostPieceOffset;

    /**
     * Creates a new {@code ViewData} object containing all visual information
     * required by the GUI to render the current game state. This includes the
     * active brick, its position, the next brick, the held brick, and the ghost
     * piece projection.
     *
     * @param brickData        the shape data of the currently active brick
     * @param xPosition        the x-coordinate of the active brick on the game grid
     * @param yPosition        the y-coordinate of the active brick on the game grid
     * @param nextBrickData    the shape data of the upcoming next brick
     * @param holdBrickData    the shape data of the currently held brick, if any
     * @param ghostPieceOffset the projected (x, y) offset where the ghost piece should appear
     */

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int[][] holdBrickData, Point ghostPieceOffset) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.holdBrickData = holdBrickData;
        this.ghostPieceOffset = ghostPieceOffset;
    }

    /**
     * Returns a copy of the active brick's data.
     *
     * @return a copy of the active brick's data
     */

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    /**
     * Returns the x-coordinate of the active brick.
     *
     * @return the current x coordinate of the brick
     */

    public int getxPosition() {
        return xPosition;
    }

    /**
     * Returns the y-coordinate of the active brick.
     *
     * @return the current y coordinate of the brick
     */

    public int getyPosition() {
        return yPosition;
    }

    /**
     * Returns the x-coordinate of the ghost brick.
     *
     * @return the current x coordinate of the ghost brick
     */
    public double getGhostPieceXPosition(){
        return ghostPieceOffset.getX();
    }

    /**
     * Returns the y-coordinate of the ghost brick.
     *
     * @return the current y coordinate of the ghost brick
     */
    public double getGhostPieceYPosition(){
        return ghostPieceOffset.getY();
    }

    /**
     * Returns a copy of the next brick's data.
     *
     * @return a copy of the next brick's data
     */
    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    /**
     * Returns a copy of the hold brick's data.
     *
     * @return a copy of the hold brick's data if true and {@code null} if false
     */
    public int[][] getHoldBrickData(){
        if (holdBrickData != null) {
            return MatrixOperations.copy(holdBrickData);
        } else  {return null;}
    }

    /**
     * Updates the hold brick data.
     * <p>
     * If there is no currently held brick, the active brick is stored and
     * replaced with the next brick.
     * Otherwise, the active brick is swapped with the held brick.
     */
    public void refreshHoldBrickData() {
        if (holdBrickData == null) {
            holdBrickData = brickData;
            brickData = getNextBrickData();
        } else {
            int[][] temp;
            temp = holdBrickData;
            holdBrickData = brickData;
            brickData = temp;
        }
    }
}
