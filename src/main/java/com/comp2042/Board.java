package com.comp2042;

import com.comp2042.logic.*;

public interface Board {
    //Brick Controls
    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean holdBrick();
    //

    boolean createNewBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    ClearRow clearRows();

    // Getters
    Score getScore();

    LinesCleared getTotalLinesCleared();

    Level getLevel();
    //

    void newGame();
}
