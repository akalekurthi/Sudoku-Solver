package com.example.sudoku.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SudokuSolverServiceTest {

    private SudokuSolverService sudokuSolverService;

    @BeforeEach
    void setUp() {
        sudokuSolverService = new SudokuSolverService();
    }

    @Test
    void testSolveValidSudoku() {
        // Valid Sudoku puzzle
        int[][] board = {
            {5, 3, 0, 0, 7, 0, 0, 0, 0},
            {6, 0, 0, 1, 9, 5, 0, 0, 0},
            {0, 9, 8, 0, 0, 0, 0, 6, 0},
            {8, 0, 0, 0, 6, 0, 0, 0, 3},
            {4, 0, 0, 8, 0, 3, 0, 0, 1},
            {7, 0, 0, 0, 2, 0, 0, 0, 6},
            {0, 6, 0, 0, 0, 0, 2, 8, 0},
            {0, 0, 0, 4, 1, 9, 0, 0, 5},
            {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };

        boolean result = sudokuSolverService.solveSudoku(board);
        
        assertTrue(result, "Valid Sudoku should be solvable");
        
        // Check if the solution is valid
        assertTrue(isValidSolution(board), "Solution should be valid");
    }

    @Test
    void testSolveEmptyBoard() {
        int[][] board = new int[9][9]; // All zeros
        
        boolean result = sudokuSolverService.solveSudoku(board);
        
        assertTrue(result, "Empty board should be solvable");
        assertTrue(isValidSolution(board), "Solution should be valid");
    }

    @Test
    void testSolveInvalidSudoku() {
        // Invalid Sudoku with conflicting numbers in first row
        int[][] board = {
            {1, 1, 0, 0, 0, 0, 0, 0, 0}, // Two 1s in first row
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        boolean result = sudokuSolverService.solveSudoku(board);
        
        assertFalse(result, "Invalid Sudoku should not be solvable");
    }

    @Test
    void testGetSamplePuzzle() {
        int[][] samplePuzzle = sudokuSolverService.getSamplePuzzle();
        
        assertNotNull(samplePuzzle, "Sample puzzle should not be null");
        assertEquals(9, samplePuzzle.length, "Sample puzzle should be 9x9");
        assertEquals(9, samplePuzzle[0].length, "Sample puzzle should be 9x9");
        
        // Check if it's solvable
        int[][] puzzleCopy = sudokuSolverService.copyBoard(samplePuzzle);
        boolean result = sudokuSolverService.solveSudoku(puzzleCopy);
        assertTrue(result, "Sample puzzle should be solvable");
    }

    @Test
    void testGetEmptyBoard() {
        int[][] emptyBoard = sudokuSolverService.getEmptyBoard();
        
        assertNotNull(emptyBoard, "Empty board should not be null");
        assertEquals(9, emptyBoard.length, "Empty board should be 9x9");
        assertEquals(9, emptyBoard[0].length, "Empty board should be 9x9");
        
        // Check if all cells are empty (0)
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                assertEquals(0, emptyBoard[i][j], "All cells should be empty");
            }
        }
    }

    @Test
    void testCopyBoard() {
        int[][] original = {
            {1, 2, 3, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        
        int[][] copy = sudokuSolverService.copyBoard(original);
        
        // Check if copy is identical
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                assertEquals(original[i][j], copy[i][j], "Copy should be identical to original");
            }
        }
        
        // Modify copy and ensure original is unchanged
        copy[0][0] = 9;
        assertNotEquals(original[0][0], copy[0][0], "Modifying copy should not affect original");
    }

    /**
     * Helper method to validate if a Sudoku solution is correct
     */
    private boolean isValidSolution(int[][] board) {
        // Check rows
        for (int i = 0; i < 9; i++) {
            boolean[] used = new boolean[10];
            for (int j = 0; j < 9; j++) {
                if (board[i][j] < 1 || board[i][j] > 9 || used[board[i][j]]) {
                    return false;
                }
                used[board[i][j]] = true;
            }
        }
        
        // Check columns
        for (int j = 0; j < 9; j++) {
            boolean[] used = new boolean[10];
            for (int i = 0; i < 9; i++) {
                if (used[board[i][j]]) {
                    return false;
                }
                used[board[i][j]] = true;
            }
        }
        
        // Check 3x3 boxes
        for (int box = 0; box < 9; box++) {
            boolean[] used = new boolean[10];
            int startRow = (box / 3) * 3;
            int startCol = (box % 3) * 3;
            for (int i = startRow; i < startRow + 3; i++) {
                for (int j = startCol; j < startCol + 3; j++) {
                    if (used[board[i][j]]) {
                        return false;
                    }
                    used[board[i][j]] = true;
                }
            }
        }
        
        return true;
    }
} 