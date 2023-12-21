package tictactoe;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Tic_Tac_Toe game = new Tic_Tac_Toe();
        String move;
        String currentGrid;
        int turn = 0;

        // Print the initial grid.
        String initialGrid = "_________";
        System.out.print(game.buildGrid(initialGrid));

        // Set the stage of the game.
        game.setStage(initialGrid);
        Stages stage = game.getStage();

        currentGrid = initialGrid;

        while (!game.isFinished(stage)) {
            // Read the next move.
            move = game.nextMove(currentGrid);
            // Update the current grid.
            currentGrid = game.updateGrid(move, currentGrid, turn);
            // Print the grid.
            System.out.print(game.buildGrid(currentGrid));
            // Update the stage of the game.
            game.setStage(currentGrid);
            stage = game.getStage();
            // Update turn.
            turn++;
        }
        // Print the message informing the user if it's a wiw or a draw.
        System.out.print(stage.getDesc()+"\n");
    }
}


class Tic_Tac_Toe {

    private Stages stage;
    private Scanner sc = new Scanner(System.in);


    // Read a move and check if it's a legal move.
    public String nextMove(String currentGrid) {
        // Read the next move.
        String move = sc.nextLine();

        // Verify if the input is a valid move.
        while (!isInteger(move) || !isValidMove(move)
                || isOccupied(move, currentGrid)) {
            move = sc.nextLine();
        }
        return move;
    }


    // Return true if the game is finished.
    public boolean isFinished(Stages stage) {
        if (stage == Stages.DRAW || stage == Stages.X_WINS
                || stage == Stages.O_WINS) {
            return true;
        }
        return false;
    }


    // Take a string and return an int array.
    private int[] toArrayInt(String move) {
        String[] array = move.split(" ");
        int[] index = new int[]{Integer.parseInt(array[0]), Integer.parseInt(array[1])};
        return index;
    }


    // Verify that the move is in the range allowed.
    public boolean isValidMove(String move) {

        int[] index = toArrayInt(move);
        if (index[0] <= 3 && index[0] >= 1 && index[1] <= 3 && index[1] >= 1) {
            return true;
        } else {
            System.out.print("Coordinates should be from 1 to 3!`\n");
            return false;
        }
    }


    // Verify that the input are integers.
    public boolean isInteger(String move) {
        String[] array = move.split(" ");
        try {
            int index0 = Integer.parseInt(array[0]);
            int index1 = Integer.parseInt(array[1]);
            return true;
        } catch (NumberFormatException e) {
            System.out.print("You should enter numbers!\n");
            return false;
        }
    }


    // Return true if the position is occupied.
    public boolean isOccupied(String move, String grid) {
        int[] index = toArrayInt(move);
        StringBuilder updateGrid = new StringBuilder(grid);
        // Get the position where we want to place the move.
        int position = ((index[0] - 1) * 3) + (index[1] - 1); // -1 since the counting starts from 0.
        if (grid.charAt(position) != '_') System.out.print("This cell is occupied! Choose another one!\n");
        return (grid.charAt(position) != '_');
    }


    // Update the grid after a move from the player.
    public String updateGrid(String move, String grid, int turn) {
        int[] index = toArrayInt(move);
        StringBuilder updateGrid = new StringBuilder(grid);

        int position = ((index[0] - 1) * 3) + (index[1] - 1); // -1 since the counting starts from 0.

        // Make the move.
        if (turn%2==0) updateGrid.setCharAt(position, 'X');
        else updateGrid.setCharAt(position, 'O');

        return updateGrid.toString();
    }


    public String buildGrid(String str) {
        StringBuilder grid = new StringBuilder(printLineHorizontal() + "\n");

        for (int i = 0; i < 9; i += 3) {
            grid.append("| ");
            for (int j = 0; j < 3; j++) {
                grid.append(str.charAt(j + i) + " ");
            }
            grid.append("|\n");
        }

        grid.append(printLineHorizontal() + "\n");
        return grid.toString();
    }

    public Stages getStage() {
        return this.stage;
    }

    public void setStage(String str) {
        if (isImpossible(str)) {
            this.stage = Stages.IMPOSSIBLE;
        } else if (X_wins(str)) {
            this.stage = Stages.X_WINS;
        } else if (O_wins(str)) {
            this.stage = Stages.O_WINS;
        } else if (isDraw(str)) {
            this.stage = Stages.DRAW;
        } else if (isNotFinished(str)) {
            this.stage = Stages.GAME_NOT_FINISHED;
        }
    }


    private boolean isNotFinished(String str) {
        boolean result = false;
        if (!X_wins(str) && !O_wins(str)) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == '_') {
                    result = true;
                }
            }
        }
        return result;
    }

    private boolean isDraw(String str) {
        if (!X_wins(str) && !O_wins(str)) {
            // Check if there is still empty cells.
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) == '_') {
                    return false;
                }
            }
            return true;
        } else return false;
    }

    // Return true if the game is impossible.
    private boolean isImpossible(String str) {
        if (X_wins(str) && O_wins(str)) return true;
        else if (isNotBalanced(str)) return true;
        else return false;
    }

    private boolean isNotBalanced(String str) {
        int len_X = count(str, 'X');
        int len_o = count(str, 'O');

        if (Math.abs(len_X - len_o) > 1) return true;
        else return false;

    }

    private int count(String str, char c) {
        int len = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                len++;
            }
        }
        return len;
    }


    private boolean X_wins(String str) {
        // Check win row
        if (win_row(str, 'X')) return true;

        // Check win column
        if (win_column(str, 'X')) return true;

        // Check win diagonal
        if (win_diagonal(str, 'X')) return true;

        return false;
    }

    private boolean win_row(String str, char c) {
        boolean result = false;
        for (int i = 0; i < 7; i += 3) {
            if (str.charAt(i) == c) {
                result = str.charAt(i) == str.charAt(i + 1) && str.charAt(i) == str.charAt(i + 2);
                if (result) return result;
            }

        }
        return result;
    }

    private boolean win_column(String str, char c) {
        boolean result = false;
        for (int i = 0; i < 3; i++) {
            if (str.charAt(i) == c) {
                result = str.charAt(i) == str.charAt(i + 3) && str.charAt(i + 3) == str.charAt(i + 6);
                if (result) return result;
            }

        }
        return result;
    }

    private boolean O_wins(String str) {
        // Check win row
        if (win_row(str, 'O')) return true;

        // Check win column
        if (win_column(str, 'O')) return true;

        // Check win diagonal
        if (win_diagonal(str, 'O')) return true;

        return false;
    }

    private boolean win_diagonal(String str, char c) {
        boolean diago1 = false;
        if (str.charAt(0) == c)
            diago1 = (str.charAt(0) == str.charAt(4) && str.charAt(0) == str.charAt(8));

        boolean diago2 = false;
        if (str.charAt(2) == c)
            diago2 = (str.charAt(2) == str.charAt(4) && str.charAt(2) == str.charAt(6));

        return diago1 || diago2;

    }


    private String printLineHorizontal() {
        return "---------";

    }
}

enum Stages {GAME_FINISHED("Game finished"),
    DRAW("Draw"),
    X_WINS("X wins"),
    O_WINS("O wins"),
    IMPOSSIBLE("Impossible"),
    GAME_NOT_FINISHED("Game not finished");


    private String desc;

    Stages(String str) {
        this.desc = str;
    }

    public String getDesc() {
        return this.desc;
    }

}
