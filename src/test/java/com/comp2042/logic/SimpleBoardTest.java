package com.comp2042.logic;

import com.comp2042.data.SaveData;
import com.comp2042.view.ViewData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.text.View;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class SimpleBoardTest {
    private SimpleBoard board;

    @BeforeEach
    void setUp() {
        SaveData.createSaveFile();
        board = new SimpleBoard(10, 20);
        board.newGame();
    }

    @Test
    public void rotateLeftBrickTest() {
        ViewData before = board.getViewData();
        int[][] beforeBrickData = before.getBrickData();
        board.rotateLeftBrick();
        ViewData after = board.getViewData();
        int[][] afterBrickData = after.getBrickData();

        boolean arraysEqual = true;
        for (int i = 0; i < beforeBrickData.length; i++) {
            for (int j = 0; j < beforeBrickData[i].length; j++) {
                if (beforeBrickData[i][j] != afterBrickData[i][j]) {
                    arraysEqual = false;
                    break;
                }
            }
            if (!arraysEqual) break;
        }

        assertFalse(arraysEqual, "The brick data should have changed and not be equal to the current brick data");

        beforeBrickData = before.getNextBrickData();

        arraysEqual = true;
        for (int i = 0; i < beforeBrickData.length; i++) {
            for (int j = 0; j < beforeBrickData[i].length; j++) {
                if (beforeBrickData[i][j] != afterBrickData[i][j]) {
                    arraysEqual = false;
                    break;
                }
            }
            if (!arraysEqual) break;
        }
        assertFalse(arraysEqual, "The brick data should have changed and not be equal to the next brick data");
    }

    @Test
    void testGhostPieceFallsToLowestPosition() {
        // Get the initial brick position
        Point initialOffset = new Point(board.getViewData().getxPosition(), board.getViewData().getyPosition());

        board.moveGhostPiece();
        Point ghostOffset = new Point(board.getViewData().getxPosition(), board.getViewData().getyPosition());

        // Ghost piece always be below or same at y-axis of the current offset
        assertTrue(ghostOffset.y >= initialOffset.y, "Ghost piece should be at or below current brick");
        assertEquals(ghostOffset.x, initialOffset.x, "Ghost piece should be at same x-axis as current brick");

        //move the brick down a little before merging to the background;
        for(int i = 0; i < 4; i++){
            board.moveBrickDown();
        }
        ViewData viewData = board.getViewData();


        board.mergeBrickToBackground();

        board.createNewBrick(4,1);
        board.moveGhostPiece();
        Point newGhostOffset = new Point(board.getViewData().getxPosition(), board.getViewData().getyPosition());

        // Ghost piece should now above the merged brick
        assertTrue(newGhostOffset.y < board.getBoardMatrix()[0].length, "Ghost piece should be above the floor");
        assertTrue(newGhostOffset.y < viewData.getyPosition() , "Ghost piece should not be at top row when floor exists");
    }
}