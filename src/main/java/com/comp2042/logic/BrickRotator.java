package com.comp2042.logic;

import com.comp2042.data.NextShapeInfo;
import com.comp2042.interfaces.Brick;

public class BrickRotator {

    private Brick brick;
    private Brick holdBrick;
    private int currentShape = 0;

    /* Getters */
    // get next shape
    public NextShapeInfo getNextShape() {
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        // % to make sure it loops back to 0 after reaching the last shape.
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    // get current shape
    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    // get hold brick shape
    public int[][] getHoldBrick() {
        if (holdBrick == null) return null;
        return holdBrick.getShapeMatrix().getFirst();
    }

    /* Setters */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }

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

    public void resetHoldBrick() {
        holdBrick = null;
    }

}
