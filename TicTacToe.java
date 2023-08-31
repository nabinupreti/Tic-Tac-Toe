import java.util.Scanner;

public class TicTacToe {
    private static final int BOARD_SIZE = 3;
    private static final char EMPTY = '-';
    private static final char PLAYER_X = 'X';
    private static final char PLAYER_O = 'O';

    private char[][] board;
    private char currentPlayer;

    // Stack implementation for undo feature
    private int[] moveStackRow;
    private int[] moveStackCol;
    private int stackTop;

    public TicTacToe() {
        board = new char[BOARD_SIZE][BOARD_SIZE];
        currentPlayer = PLAYER_X;
        initializeBoard();

        // Initialize stack
        moveStackRow = new int[BOARD_SIZE * BOARD_SIZE];
        moveStackCol = new int[BOARD_SIZE * BOARD_SIZE];
        stackTop = -1;
    }

    private void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    private void printBoard() {
        System.out.println("-------------");
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print("| " + board[i][j] + " ");
            }
            System.out.println("|");
            System.out.println("-------------");
        }
    }

    private boolean isBoardFull() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isGameOver() {
        return checkRows() || checkColumns() || checkDiagonals() || isBoardFull();
    }

    private boolean checkRows() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][0] != EMPTY && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return true;
            }
        }
        return false;
    }

    private boolean checkColumns() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[0][i] != EMPTY && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonals() {
        return (board[0][0] != EMPTY && board[0][0] == board[1][1] && board[1][1] == board[2][2]) ||
                (board[0][2] != EMPTY && board[0][2] == board[1][1] && board[1][1] == board[2][0]);
    }

    private void makeMove(int row, int col) {
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE || board[row][col] != EMPTY) {
            System.out.println("Invalid move. Try again.");
            return;
        }

        // Save the move to the stack for undo
        moveStackRow[++stackTop] = row;
        moveStackCol[stackTop] = col;

        board[row][col] = currentPlayer;
        currentPlayer = (currentPlayer == PLAYER_X) ? PLAYER_O : PLAYER_X;
    }

    private void undoLastMove() {
        if (stackTop >= 0) {
            int row = moveStackRow[stackTop];
            int col = moveStackCol[stackTop];
            board[row][col] = EMPTY;
            currentPlayer = (currentPlayer == PLAYER_X) ? PLAYER_O : PLAYER_X;
            stackTop--;
        } else {
            System.out.println("No move to undo.");
        }
    }

    private void play() {
        Scanner scanner = new Scanner(System.in);
        boolean undoOption = false;

        while (!isGameOver()) {
            printBoard();
            System.out.println("Player " + currentPlayer + ", enter row (0-2) and column (0-2):");

            if (undoOption) {
            System.out.println("Enter 'U' to undo the last move, or the row and column (e.g., 0 1):");
            String input = scanner.next();
            if (input.equalsIgnoreCase("U")) {
                undoLastMove();
                continue;
            } else {
                int row = Integer.parseInt(input);
                int col = scanner.nextInt();
                makeMove(row, col);
            }
            } else {
            int row = scanner.nextInt();
            int col = scanner.nextInt();
            makeMove(row, col);
            }

            // Allow undo option only for the current player's turn
            undoOption = !undoOption;
        }
        scanner.close();

        printBoard();
        if (checkRows() || checkColumns() || checkDiagonals()) {
            char winner = (currentPlayer == PLAYER_X) ? PLAYER_O : PLAYER_X;
            System.out.println("Player " + winner + " wins!");
        } else {
            System.out.println("It's a draw!");
        }
    }

    public static void main(String[] args) {
        TicTacToe ticTacToe = new TicTacToe();
        ticTacToe.play();
    }
}
