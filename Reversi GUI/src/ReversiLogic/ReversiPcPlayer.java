/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex3                                                      *
 ****************************************************************************/
package ReversiLogic;
import java.util.ArrayList;
import java.util.List;

public class ReversiPcPlayer extends ReversiPlayer{

    public ReversiPcPlayer(Player player, Boolean color){
        super(player, color);
    }

    /*****************************************************************************
     * Function name: playReversiMove                                            *
     * Input: array for returning result, string for possible options            *
     * Output: computer picking according to possibilities                       *
     ****************************************************************************/
    public Boolean playReversiMove(int move[], Board board){
        move[0] = -1;
        move[1] = -1;

        String options = board.possibleMoves(color);
        if (options.isEmpty())
            return false;

        // vector of all possible options as points
        List<Point> vecOptions = stringOptionToVector(options);

        // first option is arbitrarily chosen to be the best
        // next the program will loop over the other option and will update the best if needed
        int opponentScoreForBestMove = board.OpponentScoreForMove(vecOptions.get(0).getX(), vecOptions.get(0).getY(),color);
        int bestMove = 0;
        for (int i = 1; i < vecOptions.size(); i++) {
            int temp = board.OpponentScoreForMove(vecOptions.get(0).getX(),vecOptions.get(0).getY(),color);
            if(temp < opponentScoreForBestMove){
                bestMove = i;
                opponentScoreForBestMove = temp;
            }
        }
        // return best option
        move[0] = vecOptions.get(0).getX();
        move[1] = vecOptions.get(0).getY();
        return true;
    }

}
