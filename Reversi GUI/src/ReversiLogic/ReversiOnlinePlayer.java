/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex3                                                      *
 ****************************************************************************/
package ReversiLogic;

import ReversiNet.TcpReversiClient;

public class ReversiOnlinePlayer extends ReversiPlayer {
    private TcpReversiClient server;

    /*****************************************************************************
     * Function name: default constructor                                        *
     ****************************************************************************/
    public ReversiOnlinePlayer(Player player, Boolean color, TcpReversiClient serverSocket) {
        super(player, color);
        // start TCP connection with the server
        server = serverSocket;
    }

    /*****************************************************************************
     * Function name: playReversiMove                                            *
     * Input: opponent last move and current game board                          *
     * Output: the players move according to answer from server                  *
     ****************************************************************************/
    public Boolean playReversiMove(int[] move, Board board){
        move[0] = 0;
        move[1] = 0;
        // wait for online player to make a move.....
        return true;
    }

    /*****************************************************************************
     * Function name: sendLastMove                                               *
     * Operation: send to the server the opponents last move                     *
     ****************************************************************************/
    public void sendLastMove(int[] move){
        try {
            server.writeMove(move[0], move[1]);
        }
        catch (Exception e){}
    }

}

