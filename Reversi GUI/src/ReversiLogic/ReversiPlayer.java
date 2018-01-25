/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex3                                                      *
 ****************************************************************************/

package ReversiLogic;

import jdk.nashorn.internal.runtime.ListAdapter;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.util.Scanner;

import static com.sun.org.apache.xalan.internal.lib.ExsltStrings.split;

/*****************************************************************************
 * Board Class: this class contains the board of the game                    *
 ****************************************************************************/
public class ReversiPlayer {
    protected Player playerRef;
    protected Boolean color;
    protected char flag;

    /*****************************************************************************
     * Function name: stringOptionToVector                                       *
     * Input: string representing move options                                   *
     * Output: vector containing Points for all possible options                 *
     ****************************************************************************/
    static public List<Point> stringOptionToVector(String options){
        List<Point> vec = new ArrayList<Point>();
        if (options.isEmpty())
            return vec;
        String temp = options;
        // replace '(' ')' and ',' with spaces
        temp = temp.replace(',', ' ');
        temp = temp.replace(")", "");
        temp = temp.replace("(", "");

        // enter options to vec
        String[] arrOption = temp.split(" ");
        for(int i = 0; i < arrOption.length; i+=2) {
            int currentOption[] = new int[2];
            currentOption[0] = Integer.parseInt(arrOption[i]);
            currentOption[1] = Integer.parseInt(arrOption[i + 1]);
            vec.add(new Point(currentOption[0],currentOption[1]));
        }
        return vec;
    }

    /*****************************************************************************
     * Function name: PressKeyToContinue - waiting for ENTER                     *
     ****************************************************************************/
    private Boolean checkMoveInput(String input){
        int len = input.length();
        // minimum len for row,col
        if(len < 3)
            return false;
        // check that first char in a number
        if (input.charAt(0) > '9' || input.charAt(0) < '0')
            return false;
        int i = 0;
        // skip all numbers
        while (i < len && input.charAt(i) <= '9' && input.charAt(i) >= '0')
            i++;
        // if the input looks like 1234\0
        if(i >= len)
            return false;
        else
            // expecting a comma after first number
            if(input.charAt(i) != ',')
                return false;
        i++;
        // if input looks like 1234,\0
        if(i >= len)
            return false;
        else
        if(input.charAt(i) > '9' || input.charAt(i) < '0')
            return false;
        // skip all numbers
        while(i < len && input.charAt(i) <= '9' && input.charAt(i) >= '0')
            i++;
        if(i >= len || Character.isWhitespace(input.charAt(i)))
            return true;
        return false;
    }

    /*****************************************************************************
     * Function name: ReversiPlayer constructor                                  *
     * Input: player and color(BLACK / WHITE                                     *
     * Output: initialize player reference and save the color                    *
     ****************************************************************************/
    public ReversiPlayer(Player player, Boolean color){
        playerRef = player;
        this.color = color;
        if(color)
            flag = 'X';
        else
            flag = 'O';
    }

    /*****************************************************************************
     * Function name: getPlayer - return player reference                        *
     ****************************************************************************/
    public Player getPlayer() { return playerRef; }

    /*****************************************************************************
     * Function name: getColor - returs color (BLACK/WHITE)                      *
     ****************************************************************************/
    public Boolean getColor() { return color; }

    /*****************************************************************************
     * Function name: playReversiMove                                            *
     * Input: array to return result, string representing the move options       *
     * Output: initialize player reference and save the color                    *
     ****************************************************************************/
    public Boolean playReversiMove(int result[], Board board) { return false; }

    public void sendLastMove(int move[]) {}
}

