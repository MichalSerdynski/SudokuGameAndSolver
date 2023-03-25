import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Sudoku {
    private int[][] board;
    private static final int SIZE = 9;
    private static final int BOX_SIZE = 3;

    public Sudoku() {
        board = new int[SIZE][SIZE];
        generate();
    }

    private void generate() {
        // code to generate a random Sudoku board
        int[][] tmpBoard = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                tmpBoard[i][j] = 0;
            }
        }
        solve(tmpBoard, 0, 0);
        board = tmpBoard;
        removeCells();
    }

    private void removeCells() {
        // code to remove cells from the board to create a puzzle
        int cellsToRemove = (int) Math.round(Math.random() * 20) + 30;
        for (int i = 0; i < cellsToRemove; i++) {
            int row = (int) Math.round(Math.random() * (SIZE - 1));
            int col = (int) Math.round(Math.random() * (SIZE - 1));
            int tmp = board[row][col];
            board[row][col] = 0;
            if (!hasUniqueSolution()) {
                board[row][col] = tmp;
            }
        }
    }

    private boolean hasUniqueSolution() {
        // code to check if the current board has a unique solution
        int[][] tmpBoard = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                tmpBoard[i][j] = board[i][j];
            }
        }
        return solve(tmpBoard, 0, 0) == 1;
    }

    private int solve(int[][] board, int row, int col) {
        // code to solve the Sudoku board recursively using backtracking
        if (col == SIZE) {
            row++;
            col = 0;
            if (row == SIZE) {
                return 1;
            }
        }

        if (board[row][col] != 0) {
            return solve(board, row, col + 1);
        }

        for (int i = 1; i <= SIZE; i++) {
            if (isValid(board, row, col, i)) {
                board[row][col] = i;
                if (solve(board, row, col + 1) == 1) {
                    return 1;
                }
            }
        }

        board[row][col] = 0;
        return 0;
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        // code to check if a number can be placed in a given cell
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }

        int boxRow = row - row % BOX_SIZE;
        int boxCol = col - col % BOX_SIZE;
        for (int i = boxRow; i < boxRow + BOX_SIZE; i++) {
            for (int j = boxCol; j < boxCol + BOX_SIZE; j++) {
                if (board[i][j] == num) {
                    return false;
                }
            }
        }

        return true;
    }
    public void display() {
        // code to display the board in the console
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public int[][] getBoard() {
        // getter method to retrieve the Sudoku board
        return board;
    }

    public static void main(String[] args) {
        // code to create the GUI and handle user input
        JFrame frame = new JFrame("Sudoku");
        JPanel panel = new JPanel(new GridLayout(SIZE, SIZE));
        Sudoku sudoku = new Sudoku();

        int[][] board = sudoku.getBoard();
        JTextField[][] textFields = new JTextField[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                textFields[i][j] = new JTextField(board[i][j] == 0 ? "" : Integer.toString(board[i][j]));
                textFields[i][j].setHorizontalAlignment(JTextField.CENTER);
                textFields[i][j].setFont(new Font("Arial", Font.PLAIN, 20));
                textFields[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

                int finalI = i;
                int finalJ = j;
                textFields[i][j].addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        JTextField source = (JTextField) e.getSource();
                        String text = source.getText();

                        if (text.isEmpty()) {
                            board[finalI][finalJ] = 0;
                        } else {
                            int value = Integer.parseInt(text);
                            if (value < 1 || value > 9) {
                                JOptionPane.showMessageDialog(frame, "Invalid value: " + value);
                                source.setText("");
                                return;
                            }
                            board[finalI][finalJ] = value;
                        }
                    }
                });

                panel.add(textFields[i][j]);
            }
        }

        JButton solveButton = new JButton("Solve");
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][] solvedBoard = new int[SIZE][SIZE];
                for (int i = 0; i < SIZE; i++) {
                    for (int j = 0; j < SIZE; j++) {
                        solvedBoard[i][j] = board[i][j];
                    }
                }

                if (new Sudoku().solve(solvedBoard, 0, 0) == 1) {
                    for (int i = 0; i < SIZE; i++) {
                        for (int j = 0; j < SIZE; j++) {
                            textFields[i][j].setText(Integer.toString(solvedBoard[i][j]));
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Could not solve Sudoku");
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(solveButton);

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        frame.setSize(600, 600);
        frame.setVisible(true);
    }
}