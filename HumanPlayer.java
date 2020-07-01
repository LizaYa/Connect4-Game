/**
 * // CLASS: HumanPlayer
 * //
 * // Author: YYelizaveta Yashin
 * //
 * // REMARKS: This class implements Player interface and Human interface. It will process the Human player
 * //          and interact with the TextUI.
 * //
 * //-----------------------------------------
 **/
public class HumanPlayer implements Player, Human {
    private int lastCol;
    private Status winner;
    private int size;
    private GameLogic gl;
    private int col;
    private SwingGUI ui;


    /**
     * //------------------------------------------------------
     * // HumanPlayer
     * //
     * // PURPOSE: constructor that creates the local ui.
     * //------------------------------------------------------
     **/
    public HumanPlayer() {
        ui = new SwingGUI();
    }


    /**
     * //------------------------------------------------------
     * // setInfo
     * //
     * // PURPOSE: called before any other action. Sets the gl and size of the board.
     * //          Then call setInfo method on the local ui object and pass this human and
     * //          the size of the board.
     * //
     * // PARAMETERS:
     * //      size - the size of the board
     * //      gl - GameLogic type variable
     * //------------------------------------------------------
     **/
    public void setInfo(int size, GameLogic gl) {
        this.gl = gl;
        ui.setInfo(this, size);
    }


    /**
     * //------------------------------------------------------
     * // lastMove
     * //
     * // PURPOSE: called to indicate the last move of the opponent.
     * //          Calls lastMove method on ui.
     * //
     * // PARAMETERS:
     * //      lastCol - column where the opponent played.
     * //------------------------------------------------------
     **/
    public void lastMove(int lastCol) {
        ui.lastMove(lastCol);
    }


    /**
     * //------------------------------------------------------
     * // gameOver
     * //
     * // PURPOSE: called when the game is over and human wins.
     * //           Calls gameOver on ui and passed the winner.
     * //
     * // PARAMETERS:
     * //      winner - the winner
     * //------------------------------------------------------
     **/
    public void gameOver(Status winner) {
        ui.gameOver(winner);
    }


    /**
     * //------------------------------------------------------
     * // setAnswer
     * //
     * // PURPOSE: called by the ui when the human plays.
     * //          Then, calss setAnswer method on gl to indicate
     * //           that AI should play now.
     * //
     * // PARAMETERS:
     * //      col - tthe column the human just played on.
     * //------------------------------------------------------
     **/
    public void setAnswer(int col) {
        this.col = col;
        gl.setAnswer(col);
    }
}
