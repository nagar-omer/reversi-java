/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex3                                                      *
 ****************************************************************************/
package ReversiGUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {
    // primary stage + game Gui
    private Stage primary;
    private Scene setUp;
    private SetUpGui setUpController;

    /*************************************************************************
     * Function name: start                                                  *
     * Operation: the function creates game gui and runs it                  *
     ************************************************************************/
    public void switchToGame() throws Exception{

        // create Game gui and controller
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("game_gui.fxml"));
        AnchorPane rootGame = gameLoader.load();
        // Get the Controller from the FXMLLoader
        GameGui gameGuiController = gameLoader.getController();
        Scene gameScene = new Scene(rootGame, 900, 600);

        // init Game Gui
        gameGuiController.initGameGui(setUpController.getBoardSize(),
                setUpController.getReversiPlayer1(), setUpController.getReversiPlayer2(), setUpController.getClintEndsocket());
        gameGuiController.setGuiHandler(this);
        // create Reversi-Game and start game
        primary.setScene(gameScene);
        gameGuiController.startGame();
    }

    /*************************************************************************
     * Function name: switchToSetUp                                          *
     ************************************************************************/
    public void switchToSetUp(){
        primary.setScene(setUp);
    }

    /*************************************************************************
     * Function name: start                                                  *
     * Operation: the function creates set-up gui and runs it                *
     ************************************************************************/
    @Override
    public void start(Stage primaryStage) throws Exception{
        primary = primaryStage;

        FXMLLoader setUploader = new FXMLLoader(getClass().getResource("set_up_gui.fxml"));
        AnchorPane rootSetUp = setUploader.load();
        // Get the Controller from the FXMLLoader
        setUpController = setUploader.getController();
        // set handler as this object
        setUpController.setGuiHandler(this);
        setUp = new Scene(rootSetUp, 900, 600);

        primaryStage.setTitle("Reversi");
        primaryStage.setScene(setUp);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    /*************************************************************************
     * Function name: main                                                   *
     ************************************************************************/
    public static void main(String[] args) {
        launch(args);
    }
}
