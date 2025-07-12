package com.example.sudoku.controller;

import com.example.sudoku.service.SudokuSolverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
public class SudokuController {

    @Autowired
    private SudokuSolverService sudokuSolverService;

    /**
     * Display the main Sudoku solver page
     */
    @GetMapping("/")
    public String index(Model model) {
        // Initialize with empty board
        int[][] board = sudokuSolverService.getEmptyBoard();
        model.addAttribute("board", board);
        model.addAttribute("solved", false);
        model.addAttribute("error", false);
        model.addAttribute("errorMessage", "");
        return "index";
    }

    /**
     * Handle Sudoku solve request
     */
    @PostMapping("/solve")
    public String solveSudoku(@RequestParam("board") String[] boardValues, Model model) {
        try {
            // Convert string array to 2D int array
            int[][] board = new int[9][9];
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    String value = boardValues[i * 9 + j];
                    board[i][j] = value.isEmpty() ? 0 : Integer.parseInt(value);
                }
            }

            // Create a copy for solving
            int[][] boardCopy = sudokuSolverService.copyBoard(board);
            
            // Solve the puzzle
            long startTime = System.currentTimeMillis();
            boolean solved = sudokuSolverService.solveSudoku(boardCopy);
            long endTime = System.currentTimeMillis();
            
            if (solved) {
                model.addAttribute("board", boardCopy);
                model.addAttribute("solved", true);
                model.addAttribute("error", false);
                model.addAttribute("solveTime", endTime - startTime);
            } else {
                model.addAttribute("board", board);
                model.addAttribute("solved", false);
                model.addAttribute("error", true);
                model.addAttribute("errorMessage", "No solution exists for this puzzle!");
            }
            
        } catch (NumberFormatException e) {
            model.addAttribute("board", sudokuSolverService.getEmptyBoard());
            model.addAttribute("solved", false);
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", "Invalid input! Please enter only numbers 1-9 or leave empty.");
        } catch (Exception e) {
            model.addAttribute("board", sudokuSolverService.getEmptyBoard());
            model.addAttribute("solved", false);
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", "An error occurred while solving the puzzle.");
        }
        
        return "index";
    }

    /**
     * Load sample puzzle
     */
    @GetMapping("/sample")
    public String loadSample(Model model) {
        int[][] board = sudokuSolverService.getSamplePuzzle();
        model.addAttribute("board", board);
        model.addAttribute("solved", false);
        model.addAttribute("error", false);
        model.addAttribute("errorMessage", "");
        return "index";
    }

    /**
     * Clear the board
     */
    @GetMapping("/clear")
    public String clearBoard(Model model) {
        int[][] board = sudokuSolverService.getEmptyBoard();
        model.addAttribute("board", board);
        model.addAttribute("solved", false);
        model.addAttribute("error", false);
        model.addAttribute("errorMessage", "");
        return "index";
    }
} 