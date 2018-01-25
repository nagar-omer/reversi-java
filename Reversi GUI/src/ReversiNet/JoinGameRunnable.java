package ReversiNet;

import ReversiGUI.SetUpGui;
import ReversiLogic.Board;
import ReversiLogic.Player;
import ReversiLogic.ReversiLocalPlayer;
import ReversiLogic.ReversiOnlinePlayer;
import javafx.application.Platform;

public class JoinGameRunnable implements Runnable {
    private TcpReversiClient clintEndsocket;
    private SetUpGui setUpGui;
    private Boolean valid;
    private String errorMsg, gameName ;

    public Boolean isValid() {
        return valid;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setSocket(TcpReversiClient socket){
        clintEndsocket = socket;
    }

    /*****************************************************************************
     * Function name: constructor                                                *
     ****************************************************************************/
    public JoinGameRunnable(TcpReversiClient socket, SetUpGui gui){
        clintEndsocket = socket;
        setUpGui = gui;
        valid = true;
        errorMsg = "";
        gameName = "";
    }

    /*****************************************************************************
     * Function name: run                                                        *
     * Operation: run join game in different thread                              *
     ****************************************************************************/
    @Override
    public void run() {
        valid = true;
        try {
            if(!clintEndsocket.joinGame(gameName)) {
                errorMsg = "Game Name doesn't exist";
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
                setUpGui.startJoinedOnlineGame();
            }
        });
    }
}
