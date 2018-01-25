/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex3                                                      *
 ****************************************************************************/
package ReversiGUI;


import ReversiLogic.*;
import ReversiNet.GamesListRunnable;
import ReversiNet.JoinGameRunnable;
import ReversiNet.NewGameRunnable;
import ReversiNet.TcpReversiClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

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
    @FXML
    private Label gameNameError;
    @FXML
    private TextField gameNameText;
    @FXML
    private Label gameNameLabel;
    @FXML
    private Label waitForServerLabel;
    @FXML
    private Button startButton;
    @FXML
    private ListView gamesListView;
    @FXML
    private Pane gameListPane;

    private TcpReversiClient clintEndsocket;
    private Player onlinePlayer;
    private ReversiPlayer reversiPc;
    private ReversiPlayer reversiOnline;
    private ReversiPlayer reversiPlayer1;
    private ReversiPlayer reversiPlayer2;
    private NewGameRunnable newGameRun;
    private JoinGameRunnable joinGameRun;
    private GamesListRunnable gamesListRunnable;
    private Boolean isOnline;
    private String firstNameStr, secondNameStr;
    private String serverAddress;
    private int serverPort;
    private final int defaultBoardSize = 8;

    private String settingsFileName = "set_up.txt";
    private String socketFileName = "server_info.txt";


    public TcpReversiClient getClintEndsocket() {
        return clintEndsocket;
    }

    /*************************************************************************
     * Function name: waitForSever                                           *
     * Operation: freeze all                                                 *
     ************************************************************************/
    private void waitForSever(){
        startButton.setDisable(true);
        firstPlayerName.setDisable(true);
        secondPlayerName.setDisable(true);
        gameNameText.setDisable(true);
        gameModeCombo.setDisable(true);
        boardSizeCombo.setDisable(true);
        waitForServerLabel.setVisible(true);

        if(gameModeCombo.getValue().toString() == "open new online game") {
            newGameRun.setGameName(gameNameText.getText().trim());
            // start new game on server
            (new Thread(newGameRun)).start();
        }
        else if (gameModeCombo.getValue().toString() == "join existing online game") {
            joinGameRun.setGameName(gameNameText.getText().trim());
            (new Thread(joinGameRun)).start();
        }
    }

    /*************************************************************************
     * Function name: returnFromServer                                       *
     * Operation: unfreeze + start game                                      *
     ************************************************************************/
    public void returnFromServer(){
        startButton.setDisable(false);
        waitForServerLabel.setVisible(false);
        gameNameText.setDisable(false);
        gameModeCombo.setDisable(false);
        boardSizeCombo.setDisable(false);
        enableSetUpFields();
    }

    /*************************************************************************
     * Function name: startNewOnlineGame                                     *
     * Operation: try starting new online game                               *
     ************************************************************************/
    public void startNewOnlineGame(){
        updateSettingsFile();
        returnFromServer();
        // case game not started
        if (!newGameRun.isValid()){
            gameNameError.setText(newGameRun.getErrorMsg());
            gameNameError.setVisible(true);
            return;
        }

        // init players , local - Black | online - White
        reversiPlayer1 = new ReversiLocalPlayer(new Player(firstNameStr,
            Player.playerType.LOCAL_PLAYER), Board.BLACK);
        reversiPlayer2 = new ReversiOnlinePlayer(new Player(secondNameStr,
            Player.playerType.ONLINE_PLAYER), Board.WHITE, clintEndsocket);
        try {
            guiHandler.switchToGame();
        }
        catch (Exception e){}
    }

    /*************************************************************************
     * Function name: startJoinedOnlineGame                                  *
     * Operation: try joining online game                                    *
     ************************************************************************/
    public void startJoinedOnlineGame(){
        updateSettingsFile();
        returnFromServer();
        // case game not started
        if (!joinGameRun.isValid()){
            gameNameError.setText(joinGameRun.getErrorMsg());
            gameNameError.setVisible(true);
            return ;
        }

        // init players , local - White | online - Black
        reversiPlayer1 = new ReversiOnlinePlayer(new Player(firstNameStr,
        Player.playerType.ONLINE_PLAYER), Board.BLACK, clintEndsocket);
        reversiPlayer2 = new ReversiLocalPlayer(new Player(secondNameStr,
        Player.playerType.LOCAL_PLAYER), Board.WHITE);
        try {
            guiHandler.switchToGame();
        }
        catch (Exception e){}


    }

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

    public void printOnlineGames(){
        ObservableList<String> items = FXCollections.observableArrayList (gamesListRunnable.getGameList());
        gamesListView.setItems(items);
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
            if(gameModeCombo.getValue().toString() == "open new online game" ||
                    gameModeCombo.getValue().toString() == "join existing online game")
                bw.write(gameNameText.getText() + "\n");
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

        isOnline = false;

        // make online invisible
        gameNameError.setVisible(false);
        gameNameText.setVisible(false);
        gameNameLabel.setVisible(false);
        waitForServerLabel.setVisible(false);

        // init data
        int inputBoardSize = 8 ;
        String inputgameType = "play against local player";
        String inputGameName = "";
        String inputFirstName = "";
        String inputSecondName = "";
        serverAddress = "127.0.0.1";
        serverPort = 2020;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(socketFileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            serverAddress = bufferedReader.readLine().trim();
            serverPort = Integer.parseInt(bufferedReader.readLine());

            // close files.
            bufferedReader.close();
        }
        catch(Exception ex) {}

        // try reading data from existing file if possible
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(settingsFileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            inputgameType = bufferedReader.readLine().trim();
            // if online read game name and make online options visible
            if(inputgameType == "open new online game" || inputgameType == "join existing online game"){
                // set name of game in text field
                inputGameName = bufferedReader.readLine().trim();
                gameNameText.setText(inputGameName);
            }
            inputBoardSize = Integer.parseInt(bufferedReader.readLine());
            inputFirstName = bufferedReader.readLine().trim();
            inputSecondName = bufferedReader.readLine().trim();

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
                "play against PC", "open new online game", "join existing online game");
        gameModeCombo.setItems(gameType);
        gameModeCombo.setValue(inputgameType);

        // set options for board size combo box
        ObservableList<Integer> size = FXCollections.observableArrayList(sizeArray);
        boardSizeCombo.setItems(size);
        boardSizeCombo.setValue(inputBoardSize);

        // init Pc/Online players
        reversiPc = new ReversiPcPlayer(new Player("PC",Player.playerType.PC), Board.WHITE);
        onlinePlayer = new Player("ONLINE",Player.playerType.ONLINE_PLAYER);

        // init threads for communication with server
        newGameRun = new NewGameRunnable(clintEndsocket, this);
        joinGameRun = new JoinGameRunnable(clintEndsocket, this);
        gamesListRunnable = new GamesListRunnable(clintEndsocket, this);

        // make visible/invisible
        enableSetUpFields();

        // get list if on join
        actionGameMode();

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
     * Function name: checkOnlineGame                                        *
     * Output: case local games check that fields are valid                  *
     ************************************************************************/
    private Boolean checkOnlineGame(){
        openConnection();
        Boolean valid  = true;
        // check that user entered one user
        if (gameNameText.getText().trim().isEmpty()) {
            gameNameError.setText("Enter Game Name");
            gameNameError.setVisible(true);
            valid = false;
        }
        // case new game - check user entered name for black player
        if (gameModeCombo.getValue() == "open new online game") {
            // check fist player
            if(firstPlayerName.getText().trim().isEmpty()){
                firstPlayerError.setVisible(true);
                valid = false;
            }
        }
        // case join game - check user entered name for white player
        else if (gameModeCombo.getValue() == "join existing online game") {
            // check second player
            if( secondPlayerName.getText().trim().isEmpty()){
                secondPlayerError.setVisible(true);
                valid = false;
            }
        }
        // check connection available
        if (!valid)
            return false;
        if (!isOnline){
            gameNameError.setText("Cant connect server");
            gameNameError.setVisible(true);
            return false;
        }
        return true;
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
     * Function name: openConnection                                         *
     * Output: connect to server with new socket                             *
     ************************************************************************/
    private void openConnection(){
        try {
            clintEndsocket = new TcpReversiClient(serverAddress, serverPort, gameNameText.getText());
            isOnline = true;
            gamesListRunnable.setSocket(clintEndsocket);
            joinGameRun.setSocket(clintEndsocket);
            newGameRun.setSocket(clintEndsocket);
        } catch (Exception e) {
            isOnline = false;
        }

    }

    /*************************************************************************
     * Function name: actionGameMode                                         *
     * Output: handle change on game mode                                    *
     ************************************************************************/
    @FXML
    private void actionGameMode(){
        if(gameModeCombo.getValue().toString() == "join existing online game") {
            openConnection();
            (new Thread(gamesListRunnable)).start();
        }
        enableSetUpFields();
    }

    /*************************************************************************
     * Function name: enableSecondPlayer                                     *
     * Output: enable/ disable second user input according to game type      *
     ************************************************************************/

    private void enableSetUpFields(){
        // make errors invisible
        gameNameError.setVisible(false);
        firstPlayerError.setVisible(false);
        secondPlayerError.setVisible(false);
        // case 2 players local game -> enable second + first
        if (gameModeCombo.getValue() == "play against local player"){
            gameListPane.setVisible(false);
            firstPlayerName.setText("");
            firstPlayerName.setDisable(false);
            secondPlayerName.setText("");
            secondPlayerName.setDisable(false);
            gameNameText.setVisible(false);
            gameNameLabel.setVisible(false);
            boardSizeCombo.setDisable(false);
        }
        // case against PC local game -> disable second
        else if (gameModeCombo.getValue() == "play against PC"){
            gameListPane.setVisible(false);
            firstPlayerName.setText("");
            firstPlayerName.setDisable(false);
            secondPlayerName.setText("PC");
            secondPlayerName.setDisable(true);
            gameNameText.setVisible(false);
            gameNameLabel.setVisible(false);
            boardSizeCombo.setDisable(false);
        }
        // case new online game -> disable second
        else if(gameModeCombo.getValue().toString() == "open new online game"){
            gameListPane.setVisible(false);
            firstPlayerName.setText("");
            firstPlayerName.setDisable(false);
            secondPlayerName.setText("ONLINE");
            secondPlayerName.setDisable(true);
            gameNameText.setVisible(true);
            gameNameLabel.setVisible(true);
            boardSizeCombo.setValue(defaultBoardSize);
            boardSizeCombo.setDisable(true);
        }
        // case join online game -> disable first
        else if(gameModeCombo.getValue().toString() == "join existing online game"){
            //(new Thread(gamesListRunnable)).start();
            gameListPane.setVisible(true);
            firstPlayerName.setText("ONLINE");
            firstPlayerName.setDisable(true);
            secondPlayerName.setText("");
            secondPlayerName.setDisable(false);
            gameNameText.setVisible(true);
            gameNameLabel.setVisible(true);
            boardSizeCombo.setValue(defaultBoardSize);
            boardSizeCombo.setDisable(true);
        }
    }

    /*************************************************************************
     * Function name: startGameActionHandler                                 *
     * Operation: handler for start game bottun                              *
     * check if form is valid and switch to game board                       *
     ************************************************************************/
    @FXML
    private void startGameActionHandler() throws Exception{
        firstNameStr = firstPlayerName.getText();
        secondNameStr = secondPlayerName.getText();
        // unmark errors
        firstPlayerError.setVisible(false);
        secondPlayerError.setVisible(false);
        gameNameError.setVisible(false);
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
        if(gameModeCombo.getValue().toString() == "open new online game" ||
                gameModeCombo.getValue().toString() == "join existing online game" ) {
            // freeze + wait for server
            if (checkOnlineGame()) {
                waitForSever();
                // don't start game - game will be started / or not by the thread above
                valid = false;
            }
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
