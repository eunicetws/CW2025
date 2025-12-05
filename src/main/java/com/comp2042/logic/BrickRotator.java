package com.comp2042.logic;

import com.comp2042.data.NextShapeInfo;
import com.comp2042.interfaces.Brick;

/**
 * This class handles the shape rotation of the active bricks.
 *
 * <p>The class is responsible for: </p>
 * <ul>
 *     <li>Tracking the current rotation of the active brick</li>
 *     <li>Return the active brick shape</li>
 *     <li>Manage the hold brick</li>
 * </ul>
 */
public class BrickRotator {

    /**
     * The current brick data
     */
    private Brick brick;
    /**
     * The current hold brick data
     */
    private Brick holdBrick;
    /**
     * The current rotation index
     */
    private int currentShape = 0;

/* Getters */

    /**
     * Returns the next rotation state of the current brick.
     *
     * <p>The rotation index increments by one and goes back to zero using
     * the modulo operator when it reaches the final shape in the rotation list.</p>
     *
     * @return a {@link NextShapeInfo} object containing the next shape matrix
     *         and its rotation index
     */

    public NextShapeInfo getNextShape() {
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        // % to make sure it loops back to 0 after reaching the last shape.
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    /**
     * Returns the current rotation of the active brick.
     *
     * @return 2D integer array representing the current shape matrix
     */

    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    /**
     * Returns the shape matrix of the held brick.
     *
     * <p>If no brick is currently held, this method returns {@code null}.</p>
     *
     * @return the first rotation of the held brick, or {@code null} if none is held
     */
    public int[][] getHoldBrick() {
        if (holdBrick == null) return null;
        return holdBrick.getShapeMatrix().getFirst();
    }

    /* Setters */

    /**
     * Sets the currently active shape in the game.
     * @param currentShape the active shape
     */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * Sets the current brick.
     *
     * @param brick the brick data
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }

    /**
     * Sets the hold brick
     *
     * <p>
     * The held brick is swapped with the current brick.
     * If no brick has been held yet, the current brick is stored as the held brick.
     * </p>
     */
    public void setHoldBrick() {
        if (holdBrick == null) {
            holdBrick = brick;
        } else {
            Brick temp = holdBrick;
            holdBrick = brick;
            brick = temp;
        }
        currentShape = 0;
    }

    /**
     * Resets the held brick to null.
     */
    public void resetHoldBrick() {
        holdBrick = null;
    }

}
