package com.comp2042.controllers;

import com.comp2042.data.SaveData;
import com.comp2042.enums.EventSource;
import com.comp2042.enums.EventType;
import com.comp2042.interfaces.InputEventListener;
import com.comp2042.data.DownData;
import com.comp2042.data.MoveEvent;
import javafx.beans.property.IntegerProperty;
import org.junit.jupiter.api.Test;

import com.comp2042.view.ViewData;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private GameController controller;

    @BeforeEach
    void setup() {
        SaveData.createSaveFile();
        GuiController dummyGui = new GuiController() {
            @Override public void setEventListener(InputEventListener eventListener) {}
            @Override public void initGameView(int[][] m, ViewData v) {}
            @Override public void bindScore(IntegerProperty p) {}
            @Override public void bindTotalClearedLines(IntegerProperty p) {}
            @Override public void bindLevel(IntegerProperty p) {}
            @Override public void setSpeed(int s) {}
            @Override public void refreshGameBackground(int[][] m) {}
            @Override public void refreshNextBrick(ViewData v) {}
            @Override public void gameOver() {}

        };

        controller = new GameController(dummyGui);
    }

    @Test
    void testHardDropEvent() {
        controller.getBoard().newGame(); // sets offset to (4,0)
        controller.createNewGame();

        int[][] beforeDrop = controller.getBoard().getViewData().getNextBrickData();

        // Perform hard drop
        controller.onDownEvent(new MoveEvent(EventType.DOWN, EventSource.USER), true);

        int[][] afterDrop = controller.getBoard().getViewData().getBrickData();
        assertArrayEquals(beforeDrop, afterDrop, "New brick wasn't created");
    }

    @Test
    void testSoftDrop() {
        controller.createNewGame();

        MoveEvent downEvent = new MoveEvent(EventType.DOWN, EventSource.USER);

        ViewData beforeDrop = controller.getBoard().getViewData();

        // Perform soft drop
        DownData downData = controller.onDownEvent(downEvent, false);
        ViewData afterDrop = downData.getViewData();

        assertEquals(afterDrop.getyPosition(), beforeDrop.getyPosition() + 1, "Y-axis should increase by 1 after soft drop");
        assertEquals(beforeDrop.getxPosition(), afterDrop.getxPosition(), "X-axis should remain the same after soft drop");
    }


    @Test
    void onLeftEvent() {
        controller.createNewGame();

        MoveEvent event = new MoveEvent(EventType.LEFT, EventSource.USER);

        ViewData before = controller.getBoard().getViewData();
        // move left
        ViewData after = controller.onLeftEvent(event);

        assertEquals(after.getyPosition(), before.getyPosition(), "Y-axis remains the same");
        assertEquals(before.getxPosition()-1, after.getxPosition(), "X-axis decrease by 1");
    }

    @Test
    void onRightEvent() {
        controller.createNewGame();
        MoveEvent event = new MoveEvent(EventType.RIGHT, EventSource.USER);
        ViewData before = controller.getBoard().getViewData();
        // move right
        ViewData after = controller.onRightEvent(event);
        assertEquals(after.getyPosition(), before.getyPosition(), "Y-axis remains the same");
        assertEquals(before.getxPosition()+1, after.getxPosition(),"X-axis decrease by 1");
    }

    @Test
    void onHoldEvent() {
        controller.createNewGame();
        MoveEvent event = new MoveEvent(EventType.HOLD, EventSource.USER);
        ViewData before = controller.getBoard().getViewData();
        // hold
        ViewData after = controller.onHoldEvent(event);
        assertArrayEquals(after.getHoldBrickData(), before.getBrickData(), "Hold did not add current brick");
        assertArrayEquals(after.getBrickData(), before.getNextBrickData(), "Next brick not added");
        // test if hold switch with current
        ViewData after2 = controller.onHoldEvent(event);
        assertArrayEquals(after2.getBrickData(), after.getHoldBrickData(), "Current brick is not hold");
    }

}