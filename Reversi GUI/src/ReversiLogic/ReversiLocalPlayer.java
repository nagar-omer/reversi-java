/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Exercise name:   Ex1                                                      *
 * File description: This file contains the Board Class header               *
 ****************************************************************************/
package ReversiLogic;
import java.util.ArrayList;
import java.util.List;

public class ReversiLocalPlayer extends ReversiPlayer {
    public ReversiLocalPlayer(Player player, Boolean color){
        super(player, color);
    }

    /*****************************************************************************
     * Function name: userMove                                                   *
     * Input: array for returning result, string for possible options            *
     * Output: user picking according to user input                              *
     ****************************************************************************/
    public Boolean playReversiMove(int move[], Board board){
        move[0] = 0;
        move[1] = 0;

        String options = board.possibleMoves(color);

        if (options.isEmpty()) {
            move[0] = -1;
            return false;
        }

        return true;
    }

}