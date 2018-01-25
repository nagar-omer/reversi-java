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
import com.sun.javafx.fxml.LoadListener;

import java.util.ArrayList;
import java.util.List;




/*****************************************************************************
 * Reversi Class: this class contains reversi game itself                    *
 ****************************************************************************/
public class GameFlow{
    private static final int PLAYER_ENDED_GAME = (-2);
    private static final int PLAYER_CANT_PLAY = (-1);
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
        // disable mouse clicking on board
        gameGui.disableBoard();

        int move[] = new int[2];
        myTurn.playReversiMove(move, board);
        // 2 turns in a row that players cant play
        if (move[0] == PLAYER_CANT_PLAY && oneTimeCantPlay) {
            gameGui.closeGame();
            gameGui.endGame();
            return false;
        }

        else if (move[0] == PLAYER_CANT_PLAY ){
            // if not mark turn as played and forward turn to other player
            oneTimeCantPlay = true;
            gameGui.informPlayerCantPlay();
            turnOver(move);
            return false;
        }

        // try request move from player
        // local player - get input from GUI
        if(myTurn.getPlayer().getPlayerType() == Player.playerType.LOCAL_PLAYER) {
            // if board full end game
            if((board.score(Board.BLACK) + board.score(Board.WHITE)) == (board.getRows()*board.getCols())){
                gameGui.closeGame();
                gameGui.endGame();
                return false;
            }
            gameGui.enableBoard();
            return true;
        }

        if(myTurn.getPlayer().getPlayerType() == Player.playerType.ONLINE_PLAYER){
            gameGui.waitForServerToPlay();
            return true;
        }
        oneTimeCantPlay = false;
        // Play the requested move
        List cellsToColor = new ArrayList<Point>();
        board.playColor(move[0], move[1], myTurn.getColor(), cellsToColor);
        gameGui.playColor(cellsToColor, myTurn.getColor());
        // end turn
        turnOver(move);
        return true;
    }

    /*****************************************************************************
     * Function name: turnOver                                                   *
     * Opration: move turn to other player                                       *
     ****************************************************************************/
    void turnOver(int[] lastMove){
        // change turns
        gameGui.unmarkLegalMoves();
        if (myTurn == player1) {
            myTurn = player2;
        }
        else
            myTurn = player1;

        // send last move to current player (after turn moved to other player)
        myTurn.sendLastMove(lastMove);

        // check if game is Over
        // mark legal move for next turn
        gameGui.unmarkLegalMoves();
        if(myTurn.getPlayer().getPlayerType() == Player.playerType.LOCAL_PLAYER)
            gameGui.markLegalMoves(ReversiPlayer.stringOptionToVector(board.possibleMoves(myTurn.getColor())));
        gameGui.enableBoard();
        // start second player turn
        play();
    }

    /*****************************************************************************
     * Function name: localPlayerPlayed                                          *
     * Input: row and col that the user clicked on                               *
     * Output: this function is activated when the online user is clicking on the*
     * board. when activated the function handle the played move.                *
     ****************************************************************************/
    public void onlinePlayerPlayed(int row, int col){

        int[] move =  {row, col};
        if(row == PLAYER_ENDED_GAME && oneTimeCantPlay) {
            gameGui.endGame();
            return;
        }
        if(row == PLAYER_ENDED_GAME) {
            gameGui.switchBackToSetUp();
            return;
        }
        else if(row == PLAYER_CANT_PLAY){
            oneTimeCantPlay = true;
            gameGui.informPlayerCantPlay();
            turnOver(move);
            return;
        }
        oneTimeCantPlay = false;
        // list for returning all cells that changed color after the turn
        List cellsToColor = new ArrayList<Point>();
        // play move on real move
        board.playColor(row, col, myTurn.getColor(), cellsToColor);
        // show move results at the GUI
        gameGui.playColor(cellsToColor, myTurn.getColor());
        // end turn and show moves for other player
        turnOver(move);
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
            int[] move =  {row, col};
            turnOver(move);
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
        if(myTurn.getPlayer().getPlayerType() == Player.playerType.LOCAL_PLAYER)
            gameGui.markLegalMoves(ReversiPlayer.stringOptionToVector(board.possibleMoves(myTurn.getColor())));
        play();
    }

}
