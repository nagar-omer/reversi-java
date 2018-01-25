package ReversiNet;

import ReversiGUI.SetUpGui;
import javafx.application.Platform;

public class NewGameRunnable implements Runnable{
    private TcpReversiClient clintEndsocket;
    private SetUpGui setUpGui;
    private Boolean valid;
    private String errorMsg, gameName;

    public Boolean isValid() {
        return valid;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public NewGameRunnable(TcpReversiClient socket, SetUpGui gui){
        clintEndsocket = socket;
        setUpGui = gui;
        valid = true;
        errorMsg = "";
        gameName = "";
    }

    /*****************************************************************************
     * Function name: constructor                                                *
     ****************************************************************************/
    public void setSocket(TcpReversiClient socket){
        clintEndsocket = socket;
    }

    /*****************************************************************************
     * Function name: run                                                        *
     * Operation: run start new online in different thread                       *
     ****************************************************************************/
    @Override
    public void run() {
        valid = true;
        try {
            if(!clintEndsocket.openNewGame(gameName)) {
                errorMsg = "Game Name already exist";
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
                setUpGui.startNewOnlineGame();
            }
        });
    }
}
