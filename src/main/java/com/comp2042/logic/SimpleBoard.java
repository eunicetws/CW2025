package com.comp2042.logic;

import com.comp2042.view.ViewData;
import com.comp2042.data.*;
import com.comp2042.interfaces.Board;
import com.comp2042.interfaces.Brick;
import com.comp2042.interfaces.BrickGenerator;
import com.comp2042.data.bricks.RandomBrickGenerator;

import java.awt.*;

/**
 * This class represents the game board.
 *
 * <p>This class handles the core game logic including: </p>
 * <ul>
 *     <li>Managing the game matrix</li>
 *     <li>The movement events</li>
 *     <li>Updating the game data</li>
 *     <li>Creating view data for the GUI</li>
 * </ul>
 */
public class SimpleBoard implements Board {

    private final int width, height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset, ghostPieceOffset;
    private final Score score;
    private final LinesCleared totalLinesCleared;
    private final Level level;

    /**
     * Constructs a {@code SimpleBoard} with the specified width and height.
     *
     * <p>
     *     This method creates a board using the specified width and height,
     *     sets the current brick data and binds the label.
     * </p>
     *
     * @param width  the width of the game board (number of columns)
     * @param height the height of the game board (number of rows)
     */
    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();

        score = new Score();
        totalLinesCleared = new LinesCleared();
        level = new Level();
    }

    // Brick Movement
    /**
     * Move the current brick down by one row.
     *
     * <p>
     *     Check if the brick can be moved using {@link MatrixOperations}.
     *      If there is no collision,  brick's position will increases along the y-axis by 1.
     *      Else the position stays the same.
     * </p>
     *
     * @return {@code true} if the brick moves, {@code false} if it collides
     */
    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Returns the brick's falling position.
     *
     * <p>
     *     The ghost piece's position will continuously shift downwards until
     *     the brick cannot be moved through checking with {@link MatrixOperations}.
     * </p>
     *
     *
     */
    @Override
    public void moveGhostPiece() {
        Point p = new Point(currentOffset);

        while (!MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), p.x, p.y + 1)) {
            p.translate(0, 1);
        }

        ghostPieceOffset = p;
    }

    /**
     * Move the current brick left by one row.
     *
     * <p>
     *     Check if the brick can be moved using {@link MatrixOperations}.
     *      If there is no collision, the brick's position will decrease along the x-axis by 1.
     *      Else the position stays the same.
     * </p>
     *
     * @return {@code true} if the brick moves, {@code false} if it collides
     */
    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Move the current brick right by one row.
     *
     * <p>
     *     Check if the brick can be moved using {@link MatrixOperations}.
     *      If there is no collision, the brick's position will increase along the x-axis by 1.
     *      Else the position stays the same.
     * </p>
     *
     * @return {@code true} if the brick moves, {@code false} if it collides
     */
    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Rotate the brick counter-clockwise
     *
     * <p>
     *     Get the rotated shape with {@link BrickRotator}.
     *     Check if the brick can be moved using {@link MatrixOperations}.
     *     If there is no collision, the brick will be rotated;
     *     Else the shape stays the same.
     * </p>
     *
     * @return {@code true} if the brick moves, {@code false} if it collides
     */
    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    /**
     * Allows player to hold the  brick.
     *
     * <p>
     *     The method checks if the brick can be held or swapped using {@link MatrixOperations} and {@link BrickGenerator}.
     * If no brick is currently held, the active brick will be held, but a new brick is created.
     * Otherwise, the held brick is swapped with the current brick if possible.</p>
     *
     * @return {@code true} if holding or swapping was successful, {@code false} if blocked
     */
    @Override
    public boolean holdBrick() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        boolean intersectWithNewBlock = !MatrixOperations.intersect( currentMatrix, brickGenerator.getNextBrick().getShapeMatrix().getFirst(), (int) currentOffset.getX(), (int) currentOffset.getY());
        if (brickRotator.getHoldBrick() == null && intersectWithNewBlock)
        {
            brickRotator.setHoldBrick();
            createNewBrick((int)currentOffset.getX(), (int)currentOffset.getY());

            return true;
        } else if (!MatrixOperations.intersect(currentMatrix, brickRotator.getHoldBrick(), (int) currentOffset.getX(), (int) currentOffset.getY())){
            brickRotator.setHoldBrick();
            return true;
        }
        return false;
    }
//

    /**
     * Creates a new brick at the specified coordinates.
     *
     * <p>
     *     The brick is generated using {@link BrickGenerator} and set to the {@link BrickRotator}
     * </p>
     * @param x the column of the board matrix
     * @param y the row of the board matrix
     * @return {@code true} if the new brick collides, {@code false} otherwise
     */
    @Override
    public boolean createNewBrick(int x, int y) {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(x, y);
        moveGhostPiece();
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * Merges the current brick into the game matrix using {@link MatrixOperations}
     */
    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * Clears filled rows from the board using {@link MatrixOperations}
     * and returns {@link ClearRow} which contains information about the
     * cleared rows and the new matrix.
     *
     * @return the result of the row clearing operation
     */
    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;

    }

//Getters
    /**
     * Returns the current game matrix.
     *
     * @return the matrix representing the current state of the board
     */
    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    /**
     * Returns a {@link ViewData} that contains all graphics information
     * of the board and bricks in the GUI.
     *
     * @return the view data for the current game state
     */
    @Override
    public ViewData getViewData() {
        return new ViewData(brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), brickGenerator.getNextBrick().getShapeMatrix().getFirst(), brickRotator.getHoldBrick(), ghostPieceOffset);
    }

    /**
     * Returns the current score.
     *
     * @return the current score
     */
    @Override
    public Score getScore() {
        return score;
    }

    /**
     * Returns the total number of cleared lines.
     *
     * @return the number lines cleared
     */
    @Override
    public LinesCleared getTotalLinesCleared() {
        return totalLinesCleared;
    }

    /**
     * Returns the current game level.
     *
     * @return the current level
     */
    @Override
    public Level getLevel() {
        return level;
    }
//

    /**
     * Starts a new game by resetting the board, the display and creates the first brick.
     */
    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        totalLinesCleared.reset();
        level.reset();
        brickRotator.resetHoldBrick();
        createNewBrick(4, 0);
    }
}
