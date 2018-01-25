package ReversiNet;

import ReversiGUI.GameGui;
import javafx.application.Platform;

public class PlayMoveRunnable implements Runnable{
    private TcpReversiClient clintEndsocket;
    private GameGui gameGui;
    private int  row, col;

    public PlayMoveRunnable(TcpReversiClient socket, GameGui gui){
        clintEndsocket = socket;
        gameGui = gui;
    }

    /*****************************************************************************
     * Function name: constructor                                                *
     ****************************************************************************/
    public int[] gettCell(){
        int[] move = new int[2];
        move[0] = row;
        move[1] = col;
        return move;
    }

    /*****************************************************************************
     * Function name: run                                                        *
     * Operation: run play move in different thread                              *
     ****************************************************************************/
    @Override
    public void run() {
        try {
            int[] move = clintEndsocket.readMove();
            row = move[0];
            col = move[1];
        }
        catch (Exception e){
            row = col = TcpReversiClient.GAME_ENDED;
        }

        //  start new game in gui's thread
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gameGui.OnlinePlayed();
            }
        });
    }
}
