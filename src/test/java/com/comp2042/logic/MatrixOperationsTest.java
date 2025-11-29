package com.comp2042.logic;

import com.comp2042.enums.KeyEventType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatrixOperationsTest {

    @Test
    void testIntersect() {
        int[][] matrix = new int[4][4];
        int[][] brick = new int[][]{
                {1, 0},
                {1, 1}
        };

        // no intersection
        assertFalse(MatrixOperations.intersect(matrix, brick, 0, 0),"Expected no intersection at position (0,0)");

        // intersection with boundary
        assertTrue(MatrixOperations.intersect(matrix, brick, 3, 3), "Expected intersection at boundary (3,3)");

        // intersection with filled cell
        matrix[1][1] = 1;
        assertTrue(MatrixOperations.intersect(matrix, brick, 0, 0), "Expected intersection with filled cell at (0,0)");
    }

    @Test
    void testCopy() {
        int[][] original = {{1, 2}, {3, 4}};
        int[][] copied = MatrixOperations.copy(original);

        assertArrayEquals(original, copied, "Copied matrix should match the original");
    }

    @Test
    void testMerge() {
        int[][] matrix = new int[2][2];
        int[][] brick = {{1, 0}, {0, 2}};

        int[][] result = MatrixOperations.merge(matrix, brick, 0, 0);

        int[][] expected = {{1, 0}, {0, 2}};
        assertArrayEquals(expected, result, "Merged matrix should contain brick values");
    }

    @Test
    void testCheckRemoving() {
        int[][] matrix = {
                {0, 0},
                {1, 1}  // full row
        };

        ClearRow clearRow = MatrixOperations.checkRemoving(matrix);

        // One row cleared
        assertEquals(1, clearRow.getLinesRemoved(), "Should clear 1 row");
        assertArrayEquals(new int[][]{
                {0, 0},
                {0, 0}
        }, clearRow.getNewMatrix(),"New matrix should have cleared row at top" );

        // Score bonus: 50 * n^2 = 50
        assertEquals(50, clearRow.getScoreBonus(), "Score bonus should be 50 for 1 cleared row");
    }

    @Test
    void testDeepCopyList() {
        int[][] m1 = {{1}};
        int[][] m2 = {{2}};
        List<int[][]> list = List.of(m1, m2);

        List<int[][]> copiedList = MatrixOperations.deepCopyList(list);

        copiedList.getFirst()[0][0] = 99;
        assertEquals(1, list.getFirst()[0][0],  "Original list should not change when deep copy is modified");
    }
}
