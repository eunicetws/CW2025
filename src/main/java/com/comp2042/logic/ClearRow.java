package com.comp2042.logic;

/**
 * This class represents the result of clearing the rows in the game matrix.
 *
 * <p>
 * This class acts as a data container that stores results when a row-
 * It includes the number of lines removed, the new board matrix after the removal,
 * and the score bonus based on the cleared those lines.
 * </p>
 */
public final class ClearRow {
    /**
     * The total number of lines removed
     */
    private final int linesRemoved;
    /**
     * The new matrix.
     */
    private final int[][] newMatrix;
    /**
     * The score bonus according to lines cleared.
     */
    private final int scoreBonus;

    /**
     * Creates a new {@link ClearRow} object containing the total lines removed
     * , new board matrix and the scores based ont the number of lines cleared
     *
     * @param linesRemoved the total number of lines removed
     * @param newMatrix the new board matrix
     * @param scoreBonus the scores according to lines cleared
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    }

    /**
     * Returns the total number of lines removed
     * @return the total number of lines removed
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Returns a copy of the new matrix.
     *
     * @return a copy of the new matrix.
     */
    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    /**
     * Returns the scores according to lines cleared
     * @return the scores according to lines cleared
     */
    public int getScoreBonus() {
        return scoreBonus;
    }
}
