package com.comp2042;

import com.comp2042.logic.bricks.Brick;

public class BrickRotator {

    private Brick brick;
    private Brick holdBrick;
    private int currentShape = 0;

    /* Getters */
    public NextShapeInfo getNextShape() {
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        // % to make sure it loops back to 0 after reaching the last shape.
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    public int[][] getHoldBrick() {
        if (holdBrick == null) return null;
        return holdBrick.getShapeMatrix().get(0);
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

}
