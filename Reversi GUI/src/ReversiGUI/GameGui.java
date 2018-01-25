/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex3                                                      *
 ****************************************************************************/
package ReversiGUI;

import ReversiGameFlow.GameFlow;
import ReversiLogic.Board;
import ReversiLogic.Player;
import ReversiLogic.Point;
import ReversiLogic.ReversiPlayer;
import ReversiNet.PlayMoveRunnable;
import ReversiNet.TcpReversiClient;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class GameGui {
    @FXML
    private Label firstCantPlay;
    @FXML
    private Label secondCantPlay;
    @FXML
    private Label gameEndedLabel;
    @FXML
    private Pane gameEndedPane;
    @FXML
    private Label winnerName;
    @FXML
    private Label firstPlayerNameGame;
    @FXML
    private Label secondPlayerNameGame;
    @FXML
    private Label firstPlayerScore;
    @FXML
    private Label secondPlayerScore;
    @FXML
    private GridPane gameBoardGrid;
    @FXML
    private ProgressBar firstPlayerProgress;
    @FXML
    private ProgressBar secondPlayerProgress;
    // static vars necessary for game GUI
    private int boardSize;
    private List<Point> legalMovesVec;
    private Rectangle[][] legalMoveGrid;
    private Circle[][] blackCircles;
    private Circle[][] whiteCircles;
    private GameFlow game;
    private ReversiPlayer myTurn, player1, player2;
    private Main guiHandler;
    private FadeTransition[][] redFade;
    private FadeTransition[][] greenFade;
    private Rectangle[][] redFadeSquaresGrid;
    private Rectangle[][] greenFadeSquaresGrid;
    private long timeMArk, timeClicked;
    private FadeTransition firstCantFade, secondCantFade;
    private PlayMoveRunnable playRun;
    private TcpReversiClient clientEndSocket;
    private Boolean startedHandelingMousePressed;



    /*****************************************************************************
     * Function name: userClickedBoard                                           *
     * Input: mouseEvent                                                         *
     * Output: manage player clicking on board                                   *
     ****************************************************************************/
    @FXML
    private void userClickedBoard(MouseEvent event){

        // handle double time clicking as result of long press
        if ((System.currentTimeMillis() - timeClicked) < 500)
            return;
        timeClicked = System.currentTimeMillis();

        int rowClicked = 0, colClicked = 0;
        double row = event.getX();
        double col = event.getY();
        // get col clicked
        for(int i = 0; i <= boardSize; i++)
            if(row <= (i+1)*(540/boardSize)){
                colClicked = i;
                break;
            }
        // get row clicked
        for(int i = 0; i <= boardSize; i++)
            if(col <= (i+1)*(540/boardSize)){
                rowClicked = i;
                break;
            }
        // mark ilegal only if click after half a second again
        if ((System.currentTimeMillis() - timeMArk) > 500)
            ilegalMove(rowClicked, colClicked);
        // play move
        game.localPlayerPlayed(rowClicked, colClicked);

    }

    /*****************************************************************************
     * Function name: initialize                                                 *
     * Operation: initiate board                                                 *
     ****************************************************************************/
    @FXML
    public void initialize() {
        startedHandelingMousePressed = false;
        firstCantFade = new FadeTransition(Duration.millis(3000), firstCantPlay);
        firstCantFade.setFromValue(1.0);
        firstCantFade.setToValue(0.0);

        secondCantFade = new FadeTransition(Duration.millis(3000), secondCantPlay);
        secondCantFade.setFromValue(1.0);
        secondCantFade.setToValue(0.0);

        // hide game ended
        gameEndedPane.setVisible(false);

        // set score 2 - 2
        firstPlayerScore.setText("2");
        secondPlayerScore.setText("2");
        timeClicked = System.currentTimeMillis();

        gameBoardGrid.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                userClickedBoard(mouseEvent);
            }
        });
    }

    /*****************************************************************************
     * Function name: setBoardSize                                               *
     * Operation: initiate board + all additional grids for size                 *
     ****************************************************************************/
    private void setBoardSize( int size ){
        boardSize = size;
        // set board grid
        for (int i = 0; i < size - 1; i++) {
            // add col
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setMaxWidth(gameBoardGrid.getMaxWidth() / size);
            colConst.setMinWidth(gameBoardGrid.getMaxWidth() / size);
            colConst.setPrefWidth(gameBoardGrid.getMaxWidth() / size);
            gameBoardGrid.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < size - 1; i++) {
            // add row
            RowConstraints rowConst = new RowConstraints();
            rowConst.setMaxHeight(gameBoardGrid.getMaxHeight() / size);
            rowConst.setMinHeight(gameBoardGrid.getMaxHeight() / size);
            rowConst.setPrefHeight(gameBoardGrid.getMaxHeight() / size);
            gameBoardGrid.getRowConstraints().add(rowConst);
        }

        legalMoveGrid = new Rectangle[size][size];
        blackCircles = new Circle[size][size];
        whiteCircles = new Circle[size][size];
        redFade = new FadeTransition[size][size];
        greenFade = new FadeTransition[size][size];
        redFadeSquaresGrid = new Rectangle[size][size];
        greenFadeSquaresGrid = new Rectangle[size][size];


        for (int i = 0; i < size; i ++)
            for (int j = 0; j < size; j++){
                //  grid of not visible green squares
                Rectangle greenSquare = new Rectangle(540/boardSize, 540/boardSize);
                greenSquare.setStroke(Color.GREEN);
                greenSquare.setFill(Color.TRANSPARENT);
                greenSquare.setStrokeWidth(3);
                greenSquare.setStrokeType(StrokeType.INSIDE);
                greenSquare.setVisible(false);
                legalMoveGrid[i][j] = greenSquare;
                gameBoardGrid.add(greenSquare, j, i);
                gameBoardGrid.setHalignment(greenSquare, HPos.CENTER);
                gameBoardGrid.setValignment(greenSquare, VPos.CENTER);

                // grid of red squares transition fade
                Rectangle redSquare = new Rectangle(540/boardSize - 2, 540/boardSize - 2);
                redSquare.setFill(Color.rgb(200, 0, 0, 0.5));
                redSquare.setVisible(false);
                redFadeSquaresGrid[i][j] = redSquare;
                gameBoardGrid.add(redSquare, j, i);
                gameBoardGrid.setHalignment(redSquare, HPos.CENTER);
                gameBoardGrid.setValignment(redSquare, VPos.CENTER);
                redFade[i][j] = new FadeTransition(Duration.millis(3000), redSquare);
                redFade[i][j].setFromValue(1.0);
                redFade[i][j].setToValue(0.0);
                redFade[i][j].setRate(5);

                // grid of green squares transition fade
                Rectangle greenFadeSquare = new Rectangle(540/boardSize - 2, 540/boardSize - 2);
                greenFadeSquare.setFill(Color.rgb(0, 200, 0, 0.5));
                greenFadeSquare.setVisible(false);
                greenFadeSquaresGrid[i][j] = greenFadeSquare;
                gameBoardGrid.add(greenFadeSquare, j, i);
                gameBoardGrid.setHalignment(greenFadeSquare, HPos.CENTER);
                gameBoardGrid.setValignment(greenFadeSquare, VPos.CENTER);
                greenFade[i][j] = new FadeTransition(Duration.millis(3000), greenFadeSquare);
                greenFade[i][j].setFromValue(1.0);
                greenFade[i][j].setToValue(0.0);
                greenFade[i][j].setRate(5);


                // white circle grid
                Circle white = new Circle(540 / (2.5 * boardSize));
                white.setFill(Color.WHITE);
                whiteCircles[i][j] = white;
                white.setVisible(false);
                gameBoardGrid.add(white, j, i);
                gameBoardGrid.setHalignment(white, HPos.CENTER);
                gameBoardGrid.setValignment(white, VPos.CENTER);

                // black circle grid
                Circle black = new Circle(540 / (2.5 * boardSize));
                black.setFill(Color.BLACK);
                blackCircles[i][j] = black;
                black.setVisible(false);
                gameBoardGrid.add(black, j, i);
                gameBoardGrid.setHalignment(black, HPos.CENTER);
                gameBoardGrid.setValignment(black, VPos.CENTER);
            }

        // put checkers in the middle of the board
        setWhite(boardSize/2-1,boardSize/2-1);
        setWhite(boardSize/2,boardSize/2);
        setBlack(boardSize/2-1,boardSize/2);
        setBlack(boardSize/2,boardSize/2-1);

    }

    /*****************************************************************************
     * Function name: setWhite                                                   *
     * Operation: play white at (i,j)                                            *
     ****************************************************************************/
    private void setWhite(int i, int j){
        blackCircles[i][j].setVisible(false);
        whiteCircles[i][j].setVisible(true);
    }

    /*****************************************************************************
     * Function name: setBlack                                                   *
     * operation: play white at (i,j)                                            *
     ****************************************************************************/
    private void setBlack(int i, int j){
        whiteCircles[i][j].setVisible(false);
        blackCircles[i][j].setVisible(true);
    }

    /*****************************************************************************
     * Function name: setAsLegal                                                 *
     * Operation: mark (i,j) as possible move                                    *
     ****************************************************************************/
    private void setAsLegal(int i, int j){
        legalMoveGrid[i][j].setVisible(true);
    }

    /*****************************************************************************
     * Function name: switchBackToSetUp                                          *
     * Operation: switch back to main set up gui                                 *
     ****************************************************************************/

    public void switchBackToSetUp(){
        guiHandler.switchToSetUp();
    }

    public void closeGame() {
        try {
            clientEndSocket.closeGame();
        }
        catch (Exception e) {}
    }

    @FXML
    public void quitButtonAction(){
        try {
            clientEndSocket.closeGame();
        }
        catch (Exception e) {}
        guiHandler.switchToSetUp();
    }

    /*****************************************************************************
     * Function name: setGuiHandler                                              *
     * Operation: set the gui manager (creator)                                  *
     ****************************************************************************/
    public void setGuiHandler(Main handler){
        guiHandler = handler;
    }

    /*****************************************************************************
     * Function name: disableBoard                                               *
     * Operation: disable board for clicking                                     *
     ****************************************************************************/
    public void disableBoard(){
        gameBoardGrid.setDisable(true);
    }

    /*****************************************************************************
     * Function name: enableBoard                                                *
     * Operation: enables board for clicking                                     *
     ****************************************************************************/
    public void enableBoard(){
        gameBoardGrid.setDisable(false);
    }

    /*****************************************************************************
     * Function name: waitForServerToPlay                                        *
     * Operation: wait for online player to play                                 *
     ****************************************************************************/
    public void waitForServerToPlay(){
        disableBoard();
        (new Thread(playRun)).start();
    }

    /*****************************************************************************
     * Function name: OnlinePlayed                                               *
     * Operation: manage online player move                                      *
     ****************************************************************************/
    public void OnlinePlayed(){
        enableBoard();
        int[] move = playRun.gettCell();
        game.onlinePlayerPlayed(move[0], move[1]);
    }

    /*****************************************************************************
     * Function name: playplayColor                                              *
     * Input: RevesiPlayer - list of points to color and color (BLACK/WHITE)     *
     * Operation: color board and update score                                   *
     ****************************************************************************/
    public void playColor(List<Point> colored, Boolean color){
        // get scores as ints
        int score1 = Integer.parseInt(firstPlayerScore.getText());
        int score2 = Integer.parseInt(secondPlayerScore.getText());

        for(int i = 0; i <colored.size(); i++) {
            if (color == Board.BLACK) {
                // calculate new score
                if(blackCircles[colored.get(i).getX()][colored.get(i).getY()].isVisible())
                    continue;
                if(whiteCircles[colored.get(i).getX()][colored.get(i).getY()].isVisible())
                    score2--;
                score1++;
                // paint Black
                setBlack(colored.get(i).getX(), colored.get(i).getY());

            }
            else {
                // calculate new score
                if(whiteCircles[colored.get(i).getX()][colored.get(i).getY()].isVisible())
                    continue;
                if(blackCircles[colored.get(i).getX()][colored.get(i).getY()].isVisible())
                    score1--;
                score2++;
                // paint White
                setWhite(colored.get(i).getX(), colored.get(i).getY());
            }
        }
        // update score and switch turns
        setScore(String.valueOf(score1), String.valueOf(score2));
        switchTurns();

    }

    /*****************************************************************************
     * Function name: initGameGui                                                *
     * Operatioin: int board with input of players and board size                *
     ****************************************************************************/
    public void initGameGui(int boardSize, ReversiPlayer firstPlayer, ReversiPlayer secondPlayer, TcpReversiClient sock){
        initialize();
        // init socket
        clientEndSocket = sock;
        // set board
        setBoardSize(boardSize);
        // set players
        player1 = firstPlayer;
        player2 = secondPlayer;
        firstPlayerNameGame.setText(player1.getPlayer().nick());
        secondPlayerNameGame.setText(player2.getPlayer().nick());

        // set turn
        if (player1.getColor() == Board.BLACK)
            myTurn = player2;
        else
            myTurn = player1;
        switchTurns();

        // create game object
        game = new GameFlow(this, boardSize, player1, player2);
        playRun = new PlayMoveRunnable(clientEndSocket,this);
        legalMovesVec = new ArrayList<Point>();
    }

    /*****************************************************************************
     * Function name: setScore                                                   *
     * Input: scores as strings                                                  *
     * Output: update scores and progress bar                                    *
     ****************************************************************************/
    private void setScore(String firstScore,String secondScore){
        // update scores label
        firstPlayerScore.setText(firstScore);
        secondPlayerScore.setText(secondScore);
        // save scores as int and update progress bar
        int score1 = Integer.parseInt(firstPlayerScore.getText());
        int score2 = Integer.parseInt(secondPlayerScore.getText());
        firstPlayerProgress.setProgress((double)score1/(double)(boardSize*boardSize));
        secondPlayerProgress.setProgress((double)score2/(double)(boardSize*boardSize));
    }

    /*****************************************************************************
     * Function name: switchTurns                                                *
     * Output: play move for requested player                                    *
     ****************************************************************************/
    private void switchTurns(){
        // check who is playing and switch labels and turns
        if(myTurn == player2){
            myTurn = player1;
            firstPlayerNameGame.setTextFill(Color.GREEN);
            firstPlayerNameGame.setFont(Font.font ("system", 24));
            secondPlayerNameGame.setTextFill(Color.BLACK);
            secondPlayerNameGame.setFont(Font.font ("system", 18));
            return;
        }
        myTurn = player2;
        firstPlayerNameGame.setTextFill(Color.BLACK);
        firstPlayerNameGame.setFont(Font.font ("system", 18));
        secondPlayerNameGame.setTextFill(Color.GREEN);
        secondPlayerNameGame.setFont(Font.font ("system", 24));
    }

    /*****************************************************************************
     * Function name: ilegalMove                                                 *
     * Input: (i,j) - cell that the user clicked on                              *
     * Operation: fade cell red/green if move ilegal/legal                       *
     ****************************************************************************/
    public void ilegalMove(int i, int j){
        timeMArk = System.currentTimeMillis();
        Boolean green = legalMoveGrid[i][j].isVisible();
        // move legal - fade green
        if(green) {
            greenFadeSquaresGrid[i][j].setVisible(true);
            greenFade[i][j].play();
        }
        // move not legal - fade red
        else {
            redFadeSquaresGrid[i][j].setVisible(true);
            redFade[i][j].play();
        }
    }

    /*****************************************************************************
     * Function name: markLegalMoves                                             *
     * Operation: make green frames for legal moves                              *
     ****************************************************************************/
    public void markLegalMoves(List<Point> legalMovesVec) {
        // make green squares visible for legal moves
        this.legalMovesVec = legalMovesVec;
        for(int i = 0; i<legalMovesVec.size(); i++)
            setAsLegal(legalMovesVec.get(i).getX(), legalMovesVec.get(i).getY());
    }

    /*****************************************************************************
     * Function name: startGame                                                  *
     * Output: start game                                                        *
     ****************************************************************************/
    public void startGame(){
        game.startGame();
    }

    /*****************************************************************************
     * Function name: endGame                                                    *
     * Operation: show window withh info about winner/Tie                        *
     ****************************************************************************/
    public void endGame() {
        int score1 = Integer.parseInt(firstPlayerScore.getText());
        int score2 = Integer.parseInt(secondPlayerScore.getText());
        // if tie
        if(score1 == score2)
            gameEndedLabel.setText("Tie !");
        // if player 1 is the winner
        else if (score1 > score2) {
            gameEndedLabel.setText("The Winner is");
            winnerName.setText(player1.getPlayer().nick());
        }
        // if player 2 is the winner
        else{
            gameEndedLabel.setText("The Winner is");
            winnerName.setText(player2.getPlayer().nick());
        }
        // disable board grid
        gameBoardGrid.setDisable(true);
        gameEndedPane.setVisible(true);
    }

    /*****************************************************************************
     * Function name: unmarkLegalMoves                                           *
     * Output: make invisible all green frames                                   *
     ****************************************************************************/
    public void unmarkLegalMoves() {
        for(int i = 0; i<legalMovesVec.size(); i++)
            legalMoveGrid[legalMovesVec.get(i).getX()][legalMovesVec.get(i).getY()].setVisible(false);

    }

    /*****************************************************************************
     * Function name: informPlayerCantPlay                                       *
     * Operation: make "Cant Play label appear next to player who cant play      *
     ****************************************************************************/
    public void informPlayerCantPlay() {
        // make label appear
        if(myTurn == player1){
            firstCantPlay.setVisible(true);
            firstCantFade.play();
        }
        else{
            secondCantPlay.setVisible(true);
            secondCantFade.play();
        }
        // move turn to other player
        switchTurns();
    }

}
