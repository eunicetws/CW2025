package com.comp2042.view;

import com.comp2042.logic.MatrixOperations;

public final class ViewData {

    private int[][] brickData;
    private int[][] holdBrickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int[][] holdBrickData) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.holdBrickData = holdBrickData;
    }

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    public int[][] getHoldBrickData(){
        if (holdBrickData != null) {
            return MatrixOperations.copy(holdBrickData);
        } else  {return null;}
    }

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
