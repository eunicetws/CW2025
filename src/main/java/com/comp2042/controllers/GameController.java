package com.comp2042.controllers;

import com.comp2042.enums.EventSource;
import com.comp2042.interfaces.Board;
import com.comp2042.interfaces.InputEventListener;
import com.comp2042.logic.ClearRow;
import com.comp2042.logic.DownData;
import com.comp2042.logic.MoveEvent;
import com.comp2042.logic.SimpleBoard;
import com.comp2042.view.ViewData;

public class GameController implements InputEventListener {

    private final Board board = new SimpleBoard(25, 10);

    private final GuiController viewGuiController;

    public GameController(GuiController guiController) {
        this.viewGuiController = guiController;
        board.createNewBrick(4, 1);
        board.moveGhostPiece();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
        viewGuiController.bindTotalClearedLines(board.getTotalLinesCleared().PropertiesTotalLinesCleared());
        viewGuiController.bindLevel(board.getLevel().propertiesLevel());
        viewGuiController.setSpeed(board.getLevel().calcSpeed());
    }

    @Override
    public DownData onDownEvent(MoveEvent event, boolean isHardDrop) {
        boolean canMove = true;
        if (isHardDrop){
            while (canMove) {
                canMove = board.moveBrickDown();
                board.getScore().add(1);
            }
        }
        if (!isHardDrop) {
            canMove = board.moveBrickDown();
        }

        ClearRow clearRow = null;
        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus()); //Add Score
                board.getTotalLinesCleared().add(clearRow.getLinesRemoved()); //Add Lines
                board.moveGhostPiece();

                // Level Up
                if (board.getLevel().reachLevelRequirement(board.getTotalLinesCleared().PropertiesTotalLinesCleared().getValue())){
                    board.getLevel().levelUp();
                    viewGuiController.setSpeed(board.getLevel().calcSpeed());
                }
            }
            if (board.createNewBrick(4, 1)) {
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());
            viewGuiController.refreshNextBrick(board.getViewData());
            board.moveGhostPiece();

        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        }
        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        board.moveGhostPiece();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        board.moveGhostPiece();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        board.moveGhostPiece();
        return board.getViewData();
    }

    public ViewData onHoldEvent(MoveEvent event) {
        if(board.holdBrick()) {
            board.getViewData().refreshHoldBrickData();
            return board.getViewData();
        } else {return null;}
    }

    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }
}
