package com.comp2042.interfaces;

import java.util.List;

/**
 * This interface defines a brick and its shape configurations.
 * <p>
 * Implementations of this interface provide the different rotations of a brick.
 * Each rotation is represented as a 2D matrix.
 * </p>
 */
public interface Brick {

    /**
     * Returns all possible shape matrices of this brick.
     * <p>
     * Each element in the list represents a different rotation of the brick.
     * </p>
     *
     * @return a {@link List} of 2D integer arrays representing the brick's shape in all rotations
     */
    List<int[][]> getShapeMatrix();
}
