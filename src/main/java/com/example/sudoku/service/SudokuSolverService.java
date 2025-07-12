package com.example.sudoku.service;

import org.springframework.stereotype.Service;

@Service
public class SudokuSolverService {

    private static final int GRID_SIZE = 9;
    private static final int BOX_SIZE = 3;

    /**
     * Solves a Sudoku puzzle using backtracking algorithm
     * @param board The 9x9 Sudoku board (0 represents empty cells)
     * @return true if solution exists, false otherwise
     */
    public boolean solveSudoku(int[][] board) {
        if (board == null || board.length != GRID_SIZE || board[0].length != GRID_SIZE) {
            return false;
        }
        
        // Validate the initial board
        if (!isValidBoard(board)) {
            return false;
        }
        
        return solveSudokuHelper(board);
    }

    /**
     * Helper method for backtracking algorithm
     */
    private boolean solveSudokuHelper(int[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (board[row][col] == 0) {
                    // Try digits 1-9
                    for (int num = 1; num <= GRID_SIZE; num++) {
                        if (isValidMove(board, row, col, num)) {
                            board[row][col] = num;
                            
                            // Recursively try to solve the rest
                            if (solveSudokuHelper(board)) {
                                return true;
                            }
                            
                            // Backtrack
                            board[row][col] = 0;
                        }
                    }
                    return false; // No valid number found for this cell
                }
            }
        }
        return true; // All cells are filled
    }

    /**
     * Checks if placing a number at the given position is valid
     */
    private boolean isValidMove(int[][] board, int row, int col, int num) {
        // Check row
        for (int x = 0; x < GRID_SIZE; x++) {
            if (board[row][x] == num) {
                return false;
            }
        }
        
        // Check column
        for (int x = 0; x < GRID_SIZE; x++) {
            if (board[x][col] == num) {
                return false;
            }
        }
        
        // Check 3x3 box
        int startRow = row - row % BOX_SIZE;
        int startCol = col - col % BOX_SIZE;
        for (int i = 0; i < BOX_SIZE; i++) {
            for (int j = 0; j < BOX_SIZE; j++) {
                if (board[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }
        
        return true;
    }

    /**
     * Validates if the initial board is valid (no conflicts)
     */
    private boolean isValidBoard(int[][] board) {
        // Check for conflicts in filled cells
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (board[row][col] != 0) {
                    int num = board[row][col];
                    board[row][col] = 0; // Temporarily remove
                    if (!isValidMove(board, row, col, num)) {
                        board[row][col] = num; // Restore
                        return false;
                    }
                    board[row][col] = num; // Restore
                }
            }
        }
        return true;
    }

    /**
     * Creates a deep copy of the board
     */
    public int[][] copyBoard(int[][] board) {
        int[][] copy = new int[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, GRID_SIZE);
        }
        return copy;
    }

    /**
     * Gets a sample Sudoku puzzle for demonstration
     */
    public int[][] getSamplePuzzle() {
        return new int[][] {
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
    }

    /**
     * Gets an empty Sudoku board
     */
    public int[][] getEmptyBoard() {
        return new int[GRID_SIZE][GRID_SIZE];
    }
} 