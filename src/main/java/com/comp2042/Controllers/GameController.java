package com.comp2042.Controllers;

import com.comp2042.*;

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(25, 10);

    private final GuiController viewGuiController;

    public GameController(GuiController guiController) {
        this.viewGuiController = guiController;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
        viewGuiController.bindTotalClearedLines(board.getTotalLinesCleared().PropertiesTotalLinesCleared());
        viewGuiController.bindLevel(board.getLevel().propertiesLevel());
        viewGuiController.setSpeed(board.getLevel().calcSpeed());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        viewGuiController.setCurrentScore(board.getScore().scoreProperty().getValue());
        ClearRow clearRow = null;
        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus()); //Add Score
                board.getTotalLinesCleared().add(clearRow.getLinesRemoved()); //Add Lines

                // Level Up
                if (board.getLevel().reachLevelRequirement(board.getTotalLinesCleared().PropertiesTotalLinesCleared().getValue())){
                    board.getLevel().levelUp();
                    viewGuiController.setSpeed(board.getLevel().calcSpeed());
                }
            }
            if (board.createNewBrick()) {
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());
            viewGuiController.refreshNextBrick(board.getViewData());

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
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
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
