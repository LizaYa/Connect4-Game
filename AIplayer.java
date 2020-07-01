import javax.print.attribute.standard.PresentationDirection;
import javax.swing.*;
import java.util.Arrays;
import java.util.Random;

/**
 * // CLASS: AIplayer
 * //
 * // Author: YYelizaveta Yashin
 * //
 * // REMARKS: This class implements Player interface. It will process the AI player and calculate
 * //          where the AI should play.
 * //
 * //-----------------------------------------
 **/

public class AIplayer implements Player {
    private int lastCol;
    private Status winner;
    private int size;  //size of the board
    private GameLogic gl;  //pointer to gameLogic
    private Status[][] board;   //board to keep truck for the AI

    /**
     * //------------------------------------------------------
     * // setInfo
     * //
     * // PURPOSE: called before any other action. Sets the gl and size of the board.
     * //          Also create an identical board to keep truck for where the AI should play.
     * //
     * // PARAMETERS:
     * //      size - the size of the board
     * //      gl - GameLogic type variable
     * //------------------------------------------------------
     **/
    public void setInfo(int size, GameLogic gl) {
        this.size = size;
        this.gl = gl;
        createBoard();
    }


    /**
     * //------------------------------------------------------
     * // lastMove
     * //
     * // PURPOSE: called to indicate the last move of the opponent.
     * //          Also add the opponent move and the current move of AI
     * //          to the current board.
     * //
     * // PARAMETERS:
     * //      lastCol - column where the opponent played.
     * //------------------------------------------------------
     **/
    public void lastMove(int lastCol) {
        this.lastCol = lastCol;
        if (lastCol != -1)
            addBoard(lastCol, Status.ONE);
        int move = getMove();
        addBoard(move, Status.TWO);
        gl.setAnswer(move);
    }


    /**
     * //------------------------------------------------------
     * // createBoard
     * //
     * // PURPOSE: called to create an extra board and initialize
     * //          it to NEITHER player.
     * //------------------------------------------------------
     **/
    private void createBoard() {
        board = new Status[size][size];
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
        while (posn < size && board[posn][col] == Status.NEITHER) {
            posn++;
        }
        return posn - 1;
    }


    /**
     * //------------------------------------------------------
     * // getMove
     * //
     * // PURPOSE: private helper method to find the best move
     * //          for AI that satisfy the specifications for the
     * //          assignment. First check if can be played defensive,
     * //          otherwise offensive, and if neither than randomly
     * //          assign a position to play for the AI player.
     * //
     * // RETURN:
     * //          return an integer as a position to move.
     * //------------------------------------------------------
     **/
    private int getMove() {
        int position;

        position = defensive();
        if (position != -1)
            return position;

        position = offensive();
        if (position != -1)
            return position;

        Random random = new Random();
        return random.nextInt(size);
    }


    /**
     * //------------------------------------------------------
     * // defensive
     * //
     * // PURPOSE: private helper method to check if AI can be played
     * //          defensive. That is, check 3 columns to the right
     * //          OR 3 columns to the top OR 3 columns diagonally to the
     * //          right and to the left. If there is a patter of Human than return
     * //          position or -1.
     * //
     * // RETURN:
     * //          return an integer that the position we found that the
     * //          AI should move. If we did not find, return -1.
     * //------------------------------------------------------
     **/
    private int defensive() {
        int counter = 3;
        int pos = -1;
        for (int row = size - 1; row >= 0; row--) {

            for (int col = 0; col < size; col++) {

                //call other private helper methods to search for a pattern of the human that might win
                //Then return the position if found where to play.
                pos = checkRowRight(row, col, Status.ONE);
                if (pos != -1)
                    return pos;

                pos = checkRowLeft(row, col, Status.ONE);
                if (pos != -1)
                    return pos;

                pos = checkCol(row, col, Status.ONE);
                if (pos != -1)
                    return pos;

                pos = checkDiagonalRight(row, col, Status.ONE);
                if (pos != -1)
                    return pos;

                pos = checkDiagonalLeft(row, col, Status.ONE);
                if (pos != -1)
                    return pos;
            }
        }
        return -1;
    }


    /**
     * //------------------------------------------------------
     * // offensive
     * //
     * // PURPOSE: private helper method to check if AI can be played
     * //          offensive. That is, check 3 columns to the right
     * //          OR 3 columns to the top OR 3 columns diagonally to the
     * //          right and to the left. If there is a patter of AI, than return
     * //          position.
     * //
     * // RETURN:
     * //          return an integer that the position we found that the
     * //          AI should move in order to win. If we did not find, return -1.
     * //------------------------------------------------------
     **/
    private int offensive() {
        int counter = 3;
        int pos;
        for (int row = size - 1; row >= 0; row--) {

            for (int col = 0; col < size; col++) {

                //call other private helper methods to search for a pattern of the AI that might win
                //Then return the position if found a place to play.
                pos = checkRowRight(row, col, Status.TWO);
                if (pos != -1)
                    return pos;

                pos = checkRowLeft(row, col, Status.TWO);
                if (pos != -1)
                    return pos;

                pos = checkCol(row, col, Status.TWO);
                if (pos != -1)
                    return pos;

                pos = checkDiagonalRight(row, col, Status.TWO);
                if (pos != -1)
                    return pos;

                pos = checkDiagonalLeft(row, col, Status.TWO);
                if (pos != -1)
                    return pos;
            }
        }
        return -1;
    }


    /**
     * //------------------------------------------------------
     * // checkRowRight
     * //
     * // PURPOSE: private helper method that is called from defensive/
     * //          offensive methods. It check the row to right, 3 columns in total
     * //          and if there is a pattern of the same player, then return true.
     * //
     * // PARAMETERS:
     * //      row - the row to start searching from.
     * //      col - the col to start searching from.
     * //      status - the player to search for.
     * //
     * //RETURN:
     * //     return the position if a patter found, otherwise -1.
     * //------------------------------------------------------
     **/
    private int checkRowRight(int row, int col, Status status) {
        int position = -1;

        if (board[row][col] == status) {
            if (col + 1 < size && board[row][col + 1] == status) {
                if (col + 2 < size && board[row][col + 2] == status && col + 3 < size && board[row][col + 3] == Status.NEITHER)
                    position = col + 3;
                else {
                    if (col + 3 < size && board[row][col + 3] == status && board[row][col + 2] == Status.NEITHER)
                        position = col + 2;
                }
            } else if (col + 3 < size && board[row][col + 2] == status && board[row][col + 3] == status && board[row][col + 1] == Status.NEITHER)
                position = col + 1;
        }
        return position;
    }


    /**
     * //------------------------------------------------------
     * // checkRowLeft
     * //
     * // PURPOSE: private helper method that is called from defensive/
     * //          offensive methods. It check the row to right, 3 columns in total
     * //          and if there is a pattern of the same player, then return true.
     * //
     * // PARAMETERS:
     * //      row - the row to start searching from.
     * //      col - the col to start searching from.
     * //      status - the player to search for.
     * //
     * //RETURN:
     * //      return the position if a patter found, otherwise -1.
     * //------------------------------------------------------
     **/
    private int checkRowLeft(int row, int col, Status status) {
        int position = -1;

        if (board[row][col] == status) {
            if (col - 1 >= 0 && board[row][col - 1] == status) {
                if (col - 2 >= 0 && board[row][col - 2] == status && col - 3 >= 0 && board[row][col - 3] == Status.NEITHER)
                    position = col - 3;
                else {
                    if (col - 3 >= 0 && board[row][col - 3] == status && board[row][col - 2] == Status.NEITHER)
                        position = col - 2;
                }
            } else if (col - 3 >= 0 && board[row][col - 2] == status && board[row][col - 3] == status && board[row][col - 1] == Status.NEITHER)
                position = col - 1;
        }
        return position;
    }


    /**
     * //------------------------------------------------------
     * // checkCol
     * //
     * // PURPOSE: private helper method that is called from defensive/
     * //          offensive methods. It check the col to top, 3 columns in total
     * //          and if there is a pattern of the same player, then return true.
     * //
     * // PARAMETERS:
     * //      row - the row to start searching from.
     * //      col - the col to start searching from.
     * //      status - the player to search for.
     * //
     * //RETURN:
     * //       return the position if a patter found, otherwise -1.
     * //------------------------------------------------------
     **/
    private int checkCol(int row, int col, Status status) {
        int counter = 3;
        int position = -1;
        int tempRow = row;

        while (tempRow > 0 && board[tempRow][col] == status && counter > 0) {
            counter--;
            tempRow--;
        }
        if(counter == 0 && row - 3 >= 0 && board[row-3][col] == Status.NEITHER)
            position = col;
        return position;
    }


    /**
     * //------------------------------------------------------
     * // checkDiagonalRight
     * //
     * // PURPOSE: private helper method that is called from defensive/
     * //          offensive methods. It check 3 col to right, and 3 rows up,
     * //          and if there is a pattern of the same player, then return true.
     * //
     * // PARAMETERS:
     * //      row - the row to start searching from.
     * //      col - the col to start searching from.
     * //      status - the player to search for.
     * //
     * //RETURN:
     * //      return the position if a patter found, otherwise -1.
     * //------------------------------------------------------
     **/
    private int checkDiagonalRight(int row, int col, Status status) {
        int counter = 3;
        int pos = -1;
        int tempRow = row;
        int tempCol = col;

        while (tempCol < size && tempRow >= 0 && board[tempRow][tempCol] == status && counter > 0) {
            counter--;
            tempRow--;
            tempCol++;
        }
        if (counter == 0 && row - 3 >= 0 && col + 3 < size && board[row - 3][col + 3] == Status.NEITHER)
            pos = col + 3;
        return pos;
    }


    /**
     * //------------------------------------------------------
     * // checkDiagonalLeft
     * //
     * // PURPOSE: private helper method that is called from defensive/
     * //          offensive methods. It check 3 col to left, and 3 rows up,
     * //          and if there is a pattern of the same player, then return true.
     * //
     * // PARAMETERS:
     * //      row - the row to start searching from.
     * //      col - the col to start searching from.
     * //      status - the player to search for.
     * //
     * //RETURN:
     * //       return the position if a patter found, otherwise -1.
     * //------------------------------------------------------
     **/
    private int checkDiagonalLeft(int row, int col, Status status) {
        int counter = 3;
        int pos = -1;
        int tempRow = row;
        int tempCol = col;

        while (tempCol >= 0 && tempRow >= 0 && board[tempRow][tempCol] == status && counter > 0) {
            counter--;
            tempRow--;
            tempCol--;
        }
        if (counter == 0 && row - 3 >= 0 && col - 3 >= 0 && board[row - 3][col - 3] == Status.NEITHER)
            pos = col - 3;
        return pos;
    }


    /**
     * //------------------------------------------------------
     * // gameOver
     * //
     * // PURPOSE: called when the game is over and AI wins.
     * //
     * // PARAMETERS:
     * //      winner - the winner (AI)
     * //------------------------------------------------------
     **/
    public void gameOver(Status winner) {
        this.winner = winner;
    }
}
