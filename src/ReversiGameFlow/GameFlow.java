/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex3                                                      *
 ****************************************************************************/
package ReversiGameFlow;
import ReversiGUI.GameGui;
import ReversiLogic.*;

import java.util.ArrayList;
import java.util.List;




/*****************************************************************************
 * Reversi Class: this class contains reversi game itself                    *
 ****************************************************************************/
public class GameFlow{
    private static final int PLAYER_ENDED_GAME = (-2);
    private static final int PLAYER_CANT_MOVE = (-1);
    private ReversiPlayer player1, player2, myTurn;
    private Board board;
    private GameGui gameGui;
    private Boolean oneTimeCantPlay;

    /*****************************************************************************
     * Function name: play                                                       *
     * Input: RevesiPlayer - player (not Player)                                 *
     * Output: play move for requested player                                    *
     ****************************************************************************/
    private Boolean play(){

        int move[] = new int[2];
        // check if previous turn the player couldn't play
        if(board.possibleMoves(myTurn.getColor()).isEmpty()) {
            if (oneTimeCantPlay) {
                gameGui.endGame();
                return false;
            }
            else {
                // if not mark turn as played and forward turn to other player
                oneTimeCantPlay = true;
                gameGui.informPlayerCantPlay();
                turnOver();
                gameGui.unmarkLegalMoves();
                gameGui.markLegalMoves(ReversiPlayer.stringOptionToVector(board.possibleMoves(myTurn.getColor())));
                return play();
            }
        }
        // try request move from player
        // local player - get input from GUI
        if(myTurn.getPlayer().getPlayerType() == Player.playerType.LOCAL_PLAYER) {
            return true;
        }
        // get input from server/PC-logic
        else
            try {
                myTurn.playReversiMove(move, board);
            }
            catch (Exception msg) {
                throw msg;
            }
        // sign for End Game
        if (move[0] == PLAYER_ENDED_GAME) {
            // TODO EX7 close connection
            gameGui.endGame();
            // closeConnection(player);
            return false;
        }
        oneTimeCantPlay = false;
        // Play the requested move
        List cellsToColor = new ArrayList<Point>();
        board.playColor(move[0], move[1], myTurn.getColor(), cellsToColor);
        gameGui.playColor(cellsToColor, myTurn.getColor());
        // end turn
        turnOver();
        gameGui.unmarkLegalMoves();
        gameGui.markLegalMoves(ReversiPlayer.stringOptionToVector(board.possibleMoves(myTurn.getColor())));
        play();
        return true;
    }

    /*****************************************************************************
     * Function name: turnOver                                                   *
     * Opration: move turn to other player                                       *
     ****************************************************************************/
    void turnOver(){
        gameGui.unmarkLegalMoves();
        if (myTurn == player1) {
            myTurn = player2;
            return;
        }
        myTurn = player1;
    }

    /*****************************************************************************
     * Function name: localPlayerPlayed                                          *
     * Input: row and col that the user clicked on                               *
     * Output: this function is activated when the local user is clicking on the *
     * board. when activated the function checks if the move is legal and handle *
     * the played move.                                                          *
     ****************************************************************************/
    public void localPlayerPlayed(int row, int col){
        // get string of possible option to play
        List<Point> options = ReversiPlayer.stringOptionToVector(board.possibleMoves(myTurn.getColor()));
        Boolean legal = false;
        // check if the move(row,col) is legal
        for (int i = 0; i < options.size(); i++)
            if(options.get(i).getX() == row && options.get(i).getY() == col)
                legal = true;
        // if move legal -> play turn
        if(legal){
            // list for returning all cells that changed color after the turn
            List cellsToColor = new ArrayList<Point>();
            // play move on real move
            board.playColor(row, col, myTurn.getColor(), cellsToColor);
            // show move results at the GUI
            gameGui.playColor(cellsToColor, myTurn.getColor());
            // end turn and show moves for other player
            turnOver();
            gameGui.unmarkLegalMoves();
            gameGui.markLegalMoves(ReversiPlayer.stringOptionToVector(board.possibleMoves(myTurn.getColor())));
            // move turn to other player
            play();
            return;
        }
        // if move is not legal inform player and play again (without calling turnOver()
        play();
    }


    /*****************************************************************************
     * Function name: default constructor - initiate players and board           *
     ****************************************************************************/
    public GameFlow(GameGui gui,int size, ReversiPlayer firstPlayer, ReversiPlayer secondPlayer){
        player1 = firstPlayer;
        player2 = secondPlayer;
        board = new Board(size, size);
        gameGui = gui;
        myTurn = player1;
        oneTimeCantPlay = false;
    }

    /*****************************************************************************
     * Function name: startGame                                                  *
     * Input: override method for Game "Interface" - starting game               *
     ****************************************************************************/
    public void startGame(){
        gameGui.markLegalMoves(ReversiPlayer.stringOptionToVector(board.possibleMoves(myTurn.getColor())));
        play();
    }

}
