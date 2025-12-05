package com.comp2042.data;

import com.comp2042.logic.MatrixOperations;

/**
 * This class represents the information about the next brick shape.
 * <p>
 * It stores the shape's matrix and its rotation/position index.
 * </p>
 */
public final class NextShapeInfo {

    /** The position or rotation index of the shape. */
    private final int[][] shape;

    /** The position or rotation index of the shape. */
    private final int position;

    /**
     * Constructs a new {@code NextShapeInfo} instance.
     *
     * @param shape the 2D array representing the Tetris shape
     * @param position the rotation or position index of the shape
     */
    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    /**
     * Returns a copy of the shape matrix.
     * <p>
     * A copy is returned to ensure the original matrix cannot be modified externally.
     * </p>
     *
     * @return a copy of the shape matrix
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    /**
     * Returns the rotation / position index of the shape.
     *
     * @return the position index
     */
    public int getPosition() {
        return position;
    }
}
