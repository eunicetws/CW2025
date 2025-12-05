package com.comp2042.logic;

import com.comp2042.enums.SaveDataType;
import com.comp2042.media.Sfx;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;


/**
 * This class is used in manipulating the matrix operations.
 *
 * <p>
 * This is a static utility class.
 * </p>
 *
 * <p>This class is responsible for: </p>
 * <ul>
 *     <li>Checking for intersections between bricks and the game board</li>
 *     <li>Managing and updating the game matrix</li>
 *     <li>Copying matrix data</li>
 * </ul>
 */

public class MatrixOperations {


    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private MatrixOperations(){

    }

    /**
     * Determines whether a given brick intersects with a matrix at a specific position.
     *
     * <p>
     * An intersection occurs if:
     * </p>
     * <ul>
     *     <li>Any non-zero cell of the brick overlaps with a non-zero cell in the matrix</li>
     *     <li>Any part of the brick is placed out of the matrix bounds</li>
     * </ul>
     *
     * @param matrix the game board matrix, where each element represents a block or empty space
     * @param brick a 2D array representing the brick shape
     * @param x the x-coordinate (row index) in the matrix to place the top-left corner of the brick
     * @param y the y-coordinate (column index) in the matrix to place the top-left corner of the brick
     * @return {@code true} if the brick collides with the matrix or is out of bounds; {@code false} otherwise
     */
    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0 && (checkOutOfBound(matrix, targetX, targetY) || matrix[targetY][targetX] != 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks whether a coordinate is out of bounds in a matrix.
     *
     * @param matrix the game matrix
     * @param targetX the x-coordinate to check
     * @param targetY the y-coordinate to check
     * @return {@code true} if the coordinate is outside the bounds of the matrix; {@code false} otherwise
     */

    private static boolean checkOutOfBound(int[][] matrix, int targetX, int targetY) {
        return targetX < 0 || targetY >= matrix.length || targetX >= matrix[targetY].length;
    }


    /**
     * Creates a deep copy of a 2D integer matrix.
     *
     * @param original the original 2D matrix
     * @return the copy of the original matrix
     */

    public static int[][] copy(int[][] original) {
        int[][] myInt = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            int[] aMatrix = original[i];
            int aLength = aMatrix.length;
            myInt[i] = new int[aLength];
            System.arraycopy(aMatrix, 0, myInt[i], 0, aLength);
        }
        return myInt;
    }

    /**
     * Merges a brick into a matrix at a specified position without modifying the original matrix.
     *
     * <p>
     * This method creates a copy of the given matrix and inserts the brick's shape data
     * at the provided (x, y) coordinates.
     * </p>
     *
     * @param filledFields the original game matrix to copy
     * @param brick the brick that is to be merged into the matrix
     * @param x the x-coordinate where the brick's top-left corner will be placed
     * @param y the y-coordinate where the brick's top-left corner will be placed
     * @return 2D matrix representing the game state with the brick merged
     */

    public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) {
        int[][] copy = copy(filledFields);
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0) {
                    copy[targetY][targetX] = brick[j][i];
                }
            }
        }
        return copy;
    }

    /**
     * Checks for fully completed rows in the matrix, removes them, and computes
     * the corresponding score bonus.
     *
     * <p>
     * Each row that is completely filled is cleared.
     * The cleared rows are removed from the matrix, the remaining rows are shifted down, and empty
     * rows are added at the top.
     * </p>
     *
     * <p>
     * The score bonus is calculated as {@code 50 * (number of cleared rows)^2}.
     * A line-clear sound effect is played for each cleared row.
     * </p>
     *
     * @param matrix the game board matrix
     * @return {@link ClearRow} containing the cleared row data
     */

    public static ClearRow checkRemoving(final int[][] matrix) {
        int[][] tmp = new int[matrix.length][matrix[0].length];
        Deque<int[]> newRows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            int[] tmpRow = new int[matrix[i].length];
            boolean rowToClear = true;
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    rowToClear = false;
                }
                tmpRow[j] = matrix[i][j];
            }
            if (rowToClear) {
                clearedRows.add(i);
                Sfx.play(SaveDataType.CLEARLINES);
            } else {
                newRows.add(tmpRow);
            }
        }
        for (int i = matrix.length - 1; i >= 0; i--) {
            int[] row = newRows.pollLast();
            if (row != null) {
                tmp[i] = row;
            } else {
                break;
            }
        }
        int scoreBonus = 50 * clearedRows.size() * clearedRows.size();
        return new ClearRow(clearedRows.size(), tmp, scoreBonus);
    }

    /**
     * Creates a deep copy of a list of 2D matrices.
     *
     * <p>
     * Each matrix in the list is individually copied so that modifications
     * to the returned list do not affect the original matrices.
     * </p>
     *
     * @param list a copy of the matrix
     * @return a new list containing deep copies of the matrices
     */
    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList());
    }

}
