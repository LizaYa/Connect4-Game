import java.util.Random;
import java.util.Arrays;

// CLASS: GAME
//
// Author: YYelizaveta Yashin
//
// REMARKS: This class implements GameLogic.
//
//-----------------------------------------

public class Game implements GameLogic {
    private int col;    //the current column
    private Status player;  //the current player
    private HumanPlayer humanPlayer;    //human player
    private AIplayer aiPlayer;  //AI player
    private Status board[][];   //identical board to keep truck of the winner
    private int boardSize;  //board size
    private int counter;    //counter to help search for a winner
    private int totalPlayed; //the number of total columns that are taken.


    /**
     * //------------------------------------------------------
     * // Game
     * //
     * // PURPOSE: contructor for this class. It is responsible to generate
     * //         a random size board between 6-12, creating a board, creating
     * //         the AI and human players. It calls setInfo method on both players
     * //         before the game starts. It is also responsible to randomly choose
     * //         the first player to play, and the game starts by calling lastMove method
     * //         on the player and passing -1.
     * //
     * //------------------------------------------------------
     **/
    public Game() {
        //generate a random number between 6-12 for the board size
        Random random = new Random();
        boardSize = random.nextInt(6 + 1) + 6;

        /*This will create a copy board to keep truck of who wins*/
        createBoard();

        //create a AI player and Human player
        humanPlayer = new HumanPlayer();
        aiPlayer = new AIplayer();


        //set info for both AI player and Human player before starting the game
        humanPlayer.setInfo(boardSize, this);
        aiPlayer.setInfo(boardSize, this);


        //if 0 then human goes first, otherwise AI goes first
        if (random.nextInt(2) == 0) {
            player = Status.ONE;
            System.out.println("The player is: Human");
            humanPlayer.lastMove(-1);
        } else {
            player = Status.TWO;
            System.out.println("The player is: AI");
            aiPlayer.lastMove(-1);
        }
    }


    /**
     * //------------------------------------------------------
     * // setAnswer
     * //
     * // PURPOSE: responsible to alternate between players.
     * //          Before moving to next player first check if anyone is winning.
     * //
     * //------------------------------------------------------
     **/
    public void setAnswer(int col) {
        this.col = col;
        addBoard(col, player);
        totalPlayed++;

        if (!checkWin()) {
            if (player == Status.ONE) {
                player = Status.TWO;
                aiPlayer.lastMove(col);

            } else {
                player = Status.ONE;
                humanPlayer.lastMove(col);
            }
        }
    }


    /**
     * //------------------------------------------------------
     * // checkWin
     * //
     * // PURPOSE: responsible to check if someone is winning using the local board
     * //          we created.
     * //
     * // RETURN:
     * //       return true is someone wins, otherwise false.
     * //
     * //------------------------------------------------------
     **/
    private boolean checkWin() {
        boolean result = false;

        if(totalPlayed == boardSize*boardSize) {
            aiPlayer.gameOver(Status.NEITHER);
            humanPlayer.gameOver(Status.NEITHER);
            System.out.println("Neither win.");
            result = true;
            return result;
        }

        for (int row = board.length-1; row >= 0; row--) {

            for (int col = 0; col < board.length; col++) {

                if (board[row][col] == Status.ONE) {  //this is human position

                    //check human player
                    if (checkRow(row, col, Status.ONE) || checkDiagonalRight(row, col, Status.ONE) ||
                            checkCol(row, col, Status.ONE) || checkDiagonalLeft(row, col, Status.ONE)) {
                        System.out.println("Human wins");
                        result = true;
                        humanPlayer.gameOver(Status.ONE);
                        aiPlayer.gameOver(Status.ONE);
                    }

                    //check AI player
                } else if (board[row][col] == Status.TWO) {
                    if (checkRow(row, col, Status.TWO) || checkDiagonalRight(row, col, Status.TWO) ||
                            checkCol(row, col, Status.TWO) || checkDiagonalLeft(row, col, Status.TWO)) {
                        System.out.println("AI wins");
                        result = true;
                        aiPlayer.gameOver(Status.TWO);
                        humanPlayer.gameOver(Status.TWO);
                    }
                }
            }
        }

        return result;
    }


    /**
     * //------------------------------------------------------
     * // checkRow
     * //
     * // PURPOSE: private helper method that check the row to right, 4 columns in total
     * //          and if there is a pattern of the same player, then return true.
     * //
     * // PARAMETERS:
     * //      row - the row to start searching from.
     * //      col - the col to start searching from.
     * //      status - the player to search for.
     * //
     * //RETURN:
     * //      return true if a patter found, otherwise false.
     * //------------------------------------------------------
     **/
    private boolean checkRow(int row, int col, Status status) {
        counter = 4;

        while (col < boardSize && board[row][col] == status && counter > 0) {
            counter--;
            col++;
        }

        return counter == 0;
    }


    /**
     * //------------------------------------------------------
     * // checkCol
     * //
     * // PURPOSE: private helper method that check the col to top, 4 columns in total
     * //          and if there is a pattern of the same player, then return true.
     * //
     * // PARAMETERS:
     * //      row - the row to start searching from.
     * //      col - the col to start searching from.
     * //      status - the player to search for.
     * //
     * //RETURN:
     * //      return true if a patter found, otherwise false.
     * //------------------------------------------------------
     **/
    private boolean checkCol(int row, int col, Status status) {
        counter = 4;

        while (row >= 0 && board[row][col] == status && counter > 0) {
            counter--;
            row--;
        }

        return counter == 0;
    }


    /**
     * //------------------------------------------------------
     * // checkDiagonalRight
     * //
     * // PURPOSE: private helper method that check 4 col to right, and 4 rows up,
     * //          and if there is a pattern of the same player, then return true.
     * //
     * // PARAMETERS:
     * //      row - the row to start searching from.
     * //      col - the col to start searching from.
     * //      status - the player to search for.
     * //
     * //RETURN:
     * //      return true if a patter found, otherwise false.
     * //------------------------------------------------------
     **/
    private boolean checkDiagonalRight(int row, int col, Status status) {
        counter = 4;

        while (col < boardSize && row >= 0 && board[row][col] == status && counter > 0) {
            counter--;
            row--;
            col++;
        }
        return counter == 0;
    }


    /**
     * //------------------------------------------------------
     * // checkDiagonalLeft
     * //
     * // PURPOSE: private helper method that check 4 col to left, and 4 rows up,
     * //          and if there is a pattern of the same player, then return true.
     * //
     * // PARAMETERS:
     * //      row - the row to start searching from.
     * //      col - the col to start searching from.
     * //      status - the player to search for.
     * //
     * //RETURN:
     * //      return true if a patter found, otherwise false.
     * //------------------------------------------------------
     **/
    private boolean checkDiagonalLeft(int row, int col, Status status) {
        counter = 4;

        while (col >= 0 && row >= 0 && board[row][col] == status && counter > 0) {
            counter--;
            row--;
            col--;
        }
        return counter == 0;
    }


    /**
     * //------------------------------------------------------
     * // createBoard
     * //
     * // PURPOSE: create a local board and initialize to NEITHER.
     * //------------------------------------------------------
     **/
    private void createBoard() {
        board = new Status[boardSize][boardSize];
        for (Status[] s : board) {
            Arrays.fill(s, Status.NEITHER);
        }
    }


    /**
     * //------------------------------------------------------
     * // addBoard
     * //
     * // PURPOSE: called to add a player to the current board.
     * //
     * // PARAMETERS:
     * //      col - the col where to add the player.
     * //      status - what player to add - Human or AI?
     * //------------------------------------------------------
     **/
    private void addBoard(int col, Status status) {
        int posn = drop(col);
        board[posn][col] = status;
    }


    /**
     * //------------------------------------------------------
     * // drop
     * //
     * // PURPOSE:a private helper method that finds the position of a marker
     * //         when it is dropped in a column.
     * //
     * // PARAMETERS:
     * //      col - the column where the piece is dropped.
     * //RETURN:
     * //      return the row where the piece lands
     * //------------------------------------------------------
     **/
    private int drop(int col) {
        int posn = 0;
        while (posn < board.length && board[posn][col] == Status.NEITHER) {
            posn++;
        }
        return posn - 1;
    }


}
