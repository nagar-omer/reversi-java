/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex3                                                      *
 ****************************************************************************/
package ReversiGUI;


import ReversiLogic.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class SetUpGui {
    Main guiHandler;
    @FXML
    private ComboBox gameModeCombo;
    @FXML
    private ComboBox boardSizeCombo;
    @FXML
    private TextField firstPlayerName;
    @FXML
    private TextField secondPlayerName;
    @FXML
    private Label firstPlayerError;
    @FXML
    private Label secondPlayerError;
    private Player onlinePlayer;
    private ReversiPlayer reversiPc, reversiOnline, reversiPlayer1, reversiPlayer2;
    String settingsFileName = "set_up.txt";

    /*************************************************************************
     * Function name: getBoardSize                                           *
     ************************************************************************/
    public int getBoardSize(){
        return (int)boardSizeCombo.getValue();
    }

    /*************************************************************************
     * Function name: getReversiPlayer1                                      *
     ************************************************************************/
    public ReversiPlayer getReversiPlayer1() {
        return reversiPlayer1;
    }

    /*************************************************************************
     * Function name: getReversiPlayer2                                      *
     ************************************************************************/
    public ReversiPlayer getReversiPlayer2() {
        return reversiPlayer2;
    }

    /*************************************************************************
     * Function name: updateSettingsFile                                     *
     * Operation: update settings file with new input                        *
     ************************************************************************/
    private void updateSettingsFile(){
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            // open file for writing
            fw = new FileWriter(settingsFileName);
            bw = new BufferedWriter(fw);
            // write data
            bw.write(gameModeCombo.getValue().toString() + "\n");
            bw.write(boardSizeCombo.getValue().toString() + "\n");
            bw.write(firstPlayerName.getText() + "\n");
            bw.write(secondPlayerName.getText());
            bw.close();

        } catch (Exception e) {}
    }

    /*************************************************************************
     * Function name: initialize                                             *
     * Operation: initialize set-up GUI                                      *
     ************************************************************************/
    public void initialize() throws IOException {

        // init data
        int inputBoardSize = 8 ;
        String inputgameType = "play against local player";
        String inputFirstName = "";
        String inputSecondName = "";
        String fileName = settingsFileName;

        // try reading data from existing file if possible
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            inputgameType = bufferedReader.readLine();
            inputBoardSize = Integer.parseInt(bufferedReader.readLine());
            inputFirstName = bufferedReader.readLine();
            inputSecondName = bufferedReader.readLine();

            // close files.
            bufferedReader.close();
        }
        catch(Exception ex) {}

        // set options for board size combo box
        List<Integer> sizeArray = new ArrayList<>();
        for (int i = 4; i < 25; i++)
            sizeArray.add(i);

        // set first player and second player text fields
        firstPlayerName.setText(inputFirstName);
        secondPlayerName.setText(inputSecondName);

        // set options for game mode combo box
        ObservableList<String> gameType = FXCollections.observableArrayList("play against local player",
                "play against PC");
        gameModeCombo.setItems(gameType);
        gameModeCombo.setValue(inputgameType);

        // set options for board size combo box
        ObservableList<Integer> size = FXCollections.observableArrayList(sizeArray);
        boardSizeCombo.setItems(size);
        boardSizeCombo.setValue(inputBoardSize);

        // init Pc/Online players
        reversiPc = new ReversiPcPlayer(new Player("PC",Player.playerType.PC), Board.WHITE);
        onlinePlayer = new Player("Remote",Player.playerType.ONLINE_PLAYER);

    }

    /*************************************************************************
     * Function name: checkLocalGame                                         *
     * Output: case local games check that fields are valid                  *
     ************************************************************************/
    private Boolean checkLocalGame(){
        Boolean valid = true;
        // check that the user entered two players names
        if( firstPlayerName.getText().trim().isEmpty()){
            firstPlayerError.setVisible(true);
            valid = false;
        }
        if( secondPlayerName.getText().trim().isEmpty()){
            secondPlayerError.setVisible(true);
            valid = false;
        }
        return valid;
    }

    /*************************************************************************
     * Function name: checkPcGame                                            *
     * Output: case local games check that fields are valid                  *
     ************************************************************************/
    private Boolean checkPcGame(){
        // check that user entered one user
        if( firstPlayerName.getText().trim().isEmpty()){
            firstPlayerError.setVisible(true);
            return false;
        }
        return true;
    }

    /*************************************************************************
     * Function name: enableSecondPlayer                                     *
     * Output: enable/ disable second user input according to game type      *
     ************************************************************************/
    @FXML
    private void enableSecondPlayer(){
        // case 2 players local game -> enable
        if (gameModeCombo.getValue() == "play against local player"){
            secondPlayerName.setText("");
            secondPlayerName.setDisable(false);
        }
        // case against PC local game -> disable
        else if (gameModeCombo.getValue() == "play against PC"){
            secondPlayerName.setText("PC");
            secondPlayerName.setDisable(true);
        }
    }

    /*************************************************************************
     * Function name: startGameActionHandler                                 *
     * Operation: handler for start game bottun                              *
     * check if form is valid and switch to game board                       *
     ************************************************************************/
    @FXML
    private void startGameActionHandler() throws Exception{
        // unmark errors
        firstPlayerError.setVisible(false);
        secondPlayerError.setVisible(false);
        Boolean valid = false;
        // check validation and init players
        if (gameModeCombo.getValue() == "play against local player" && (valid = checkLocalGame())){
            // init two local players according to input
            reversiPlayer1 = new ReversiLocalPlayer(new Player(firstPlayerName.getText(), Player.playerType.LOCAL_PLAYER), Board.BLACK);
            reversiPlayer2 = new ReversiLocalPlayer(new Player(secondPlayerName.getText(), Player.playerType.LOCAL_PLAYER), Board.WHITE);
        }
        // check validation and init player
        else if (gameModeCombo.getValue() == "play against PC" && (valid = checkPcGame())){
            // init one local player according to input
            reversiPlayer1 = new ReversiLocalPlayer(new Player(firstPlayerName.getText(), Player.playerType.LOCAL_PLAYER), Board.BLACK);
            reversiPlayer2 = reversiPc;
        }
        // if all valid start game
        if (valid) {
            // update settings file
            updateSettingsFile();
            guiHandler.switchToGame();
        }
    }

    /*************************************************************************
     * Function name: setGuiHandler                                          *
     * Output: set gui handler - Main switch between GUIs                    *
     ************************************************************************/
    void setGuiHandler(Main handler){
        guiHandler = handler;
    }
}
