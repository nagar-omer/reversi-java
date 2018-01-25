package ReversiNet;

import ReversiGUI.SetUpGui;
import javafx.application.Platform;

public class GamesListRunnable implements Runnable {
    private TcpReversiClient clintEndsocket;
    private SetUpGui setUpGui;
    private Boolean valid;
    private String errorMsg;
    private String[] gameList;

    public Boolean isValid() {
        return valid;
    }

    public String[] getGameList() {
        return gameList;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setSocket(TcpReversiClient socket){
        clintEndsocket = socket;
    }

    /*****************************************************************************
     * Function name: constructor                                                *
     ****************************************************************************/
    public GamesListRunnable(TcpReversiClient socket, SetUpGui gui){
        clintEndsocket = socket;
        setUpGui = gui;
        valid = true;
        errorMsg = "";
        gameList = new String[0];
    }

    /*****************************************************************************
     * Function name: run                                                        *
     * Operation: run get games list in different thread                         *
     ****************************************************************************/
    @Override
    public void run() {
        valid = true;
        try {
            gameList = clintEndsocket.getOpenGaes();
            if(gameList.length == 0) {
                errorMsg = "Game List is empty";
                valid = false;
            }
        }
        catch (Exception e){
            errorMsg = "Cant Connect Server!";
            valid = false;
        }

        //  start new game in gui's thread
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setUpGui.printOnlineGames();
            }
        });
    }
}

