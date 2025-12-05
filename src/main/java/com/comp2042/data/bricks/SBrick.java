package com.comp2042.data.bricks;

import com.comp2042.interfaces.Brick;
import com.comp2042.logic.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the "S" Tetris brick shape.
 */
final class SBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();
    /**
     * Creates all possible rotations of the brick.
     */
    public SBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 5, 5, 0},
                {5, 5, 0, 0},
                {0, 0, 0, 0}
        });
        brickMatrix.add(new int[][]{
                {5, 0, 0, 0},
                {5, 5, 0, 0},
                {0, 5, 0, 0},
                {0, 0, 0, 0}
        });
    }

    /**
     * Returns the shape of the brick
     * @return matrix of the brick
     */
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }
}
