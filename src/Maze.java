// Author: Polatkan Eren Polat
  

import java.util.Scanner;

public class Maze {

    public static void main(String[] args) {
        // Uncomment to test the init() method
        // Tests.testInit();

        // Uncomment to test the validMove() method
        // Tests.testValidMove();

        // Uncomment to run the game!
        runGame(); // Run the game!
    }

    /**
     * Initializes a 2D maze game board.
     *
     * Places the player in the upper left corner;
     * randomly places one gem in the maze;
     * and randomly assigns remaining squares to:
     * "Wolf", "Boar", "Elk", or "Hare".
     *
     * @param board an empty 2D String array
     */
    public static void init(String[][] board) {
        board[0][0] = "P"; // reserve player start

        // Place gem in randomized position
        int randomI, randomJ;
        do {
            randomI = (int) ((board.length) * Math.random());
            randomJ = (int) ((board[0].length) * Math.random());

            board[randomI][randomJ] = "GEM";
        } while ((randomI == 0 && randomJ == 0) || (randomI == 4 && randomJ == 4));

        // Assign hazards to remaining cells
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == null) {
                    int randomHazard = (int) (Math.random() * 5);
                    switch (randomHazard) {
                        case 0:
                            board[i][j] = "Wolf";
                            break;
                        case 1:
                            board[i][j] = "Boar";
                            break;
                        case 2:
                            board[i][j] = "Elk";
                            break;
                        case 3:
                        case 4:
                            board[i][j] = "Hare";
                            break;
                    }
                }
            }
        }
    }

    /**
     * Prints a 2D maze game board.
     *
     * @param board  An initialized 2D String array
     * @param player The current player position
     */
    public static void drawBoard(String[][] board, int[] player) {
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[0].length; ++j) {
                System.out.print("|");
                if (i == player[0] && j == player[1]) {
                    System.out.print("  P  ");
                } else if (board[i][j].equals("X")) {
                    System.out.print("  X  ");
                } else {
                    System.out.print("     ");
                }
            }
            System.out.println("|");
        }
    }

    /**
     * Assesses the validity of a player's requested
     * move and updates nextCell with values for the next move.
     *
     * @param move     a direction: 'u', 'd', 'l', or 'r'
     * @param board    an initialized 2D String game board array
     * @param player   the current player position
     * @param nextCell values for the player's next move;
     *                 {-1, -1} if move is invalid
     */
    public static void validMove(char move, String[][] board, int[] player, int[] nextCell) {
        int nextI = player[0], nextJ = player[1];
        switch (move) {
            case 'u':
                nextI--;
                break;
            case 'd':
                nextI++;
                break;
            case 'l':
                nextJ--;
                break;
            case 'r':
                nextJ++;
                break;
            default:
                nextI = nextJ = -1; // Invalid move
        }
        if (nextI >= 0 && nextI < board.length && nextJ >= 0 && nextJ < board[0].length) {
            nextCell[0] = nextI;
            nextCell[1] = nextJ;
        } else {
            nextCell[0] = nextCell[1] = -1; // Invalid move
        }
    }

    /**
     * Moves a player through the game board and
     * updates board array and health amount accordingly.
     *
     * @param nextCell directions for player movement
     * @param board    an initialized 2D String game board array
     * @param player   the current player position
     * @return the change to the player's health
     */
    public static int makeMove(int[] nextCell, String[][] board, int[] player) {
        int currentI = player[0];
        int currentJ = player[1];

        // Change current cell value to "X"
        board[currentI][currentJ] = "X";

        // Move player to new cell
        player[0] = nextCell[0];
        player[1] = nextCell[1];

        // Return the change in health based on the hazard in the new cell
        String hazard = board[player[0]][player[1]];
        switch (hazard) {
            case "Wolf":
                return -20;
            case "Boar":
                return -10;
            case "Elk":
                return -5;
            case "Hare":
                return 10;
            case "GEM":
                return 100;
            default:
                return 0;
        }
    }

    /**
     * Runs the game from beginning to end using
     * all methods in the class.
     *
     * This has been completed for you!
     */
    public static void runGame() {
        // Start Scanner incantation
        Scanner input = new Scanner(System.in);

        // Declare 2D 5x5 board array
        String[][] board = new String[5][5];

        int[] player = { 0, 0 }; // Initialize player position
        int health = 50; // Initialize health

        // Initialize nextCell to default to invalid move
        int[] nextCell = { -1, -1 };

        init(board); // Initialize board

        // Run game while player has health & has not reached finishing square
        while ((health != 0) && (!(player[0] == 4 && player[1] == 4))) {
            char move;

            // Print current game state
            drawBoard(board, player);
            System.out.println("\nHealth: " + health + "\n");

            // Ask for player input & move
            do {
                // Ask for & collect input
                // char move is 'u', 'd', 'l', or 'r'
                System.out.println("Enter a direction you want to move!");
                System.out.print("Up, down, left, or right? ");
                move = input.nextLine().toLowerCase().charAt(0);

                // Assess move validity
                validMove(move, board, player, nextCell);
                if (nextCell[0] == -1)
                    System.out.println("Invalid move! Try again.");
                System.out.println();

            } while (nextCell[0] == -1); // while move is invalid

            System.out.print("Your chosen cell has a: ");
            System.out.println(board[nextCell[0]][nextCell[1]]);
            System.out.print("Would you like to move there? Yes or no: ");
            char confirm = input.nextLine().toLowerCase().charAt(0);

            if (confirm == 'y') { // If player wants to move
                int effect = makeMove(nextCell, board, player);
                if (effect == 100) {
                    health *= 100;
                } else {
                    health += effect;
                }
                if (health < 0)
                    health = 0;

            }

            // Reset nextCell for next move
            nextCell[0] = -1;
            nextCell[1] = -1;

        }

        // Exit game
        if ((player[0] == 4 && player[1] == 4)) {
            System.out.println("You got to the end! You win!");
        } else {
            System.out.println("Sorry, you have no more health. Game over!");
        }

        input.close();
    }
}
