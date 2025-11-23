package com.comp2042.interfaces;

import com.comp2042.logic.ClearRow;
import com.comp2042.view.ViewData;
import com.comp2042.data.Level;
import com.comp2042.data.LinesCleared;
import com.comp2042.data.Score;

public interface Board {
    //Brick Controls
    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean holdBrick();
    //

    boolean createNewBrick(int x, int y);

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
