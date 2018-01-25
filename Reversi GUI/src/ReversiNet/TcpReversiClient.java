package ReversiNet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TcpReversiClient {
    public static final int GAME_ENDED = -2;
    private Socket clientEnd;
    private PrintWriter toServer;
    private BufferedReader fromServer;
    private String gameName;
    private String serverAddress;
    private int serverPort;
    private Boolean quitGame, waitingForServer;

    public void closeGame() throws Exception{
        quitGame = true;
        if(!waitingForServer) {
            toServer.println("close " + gameName + " ");
        }
    }

    private void endGame() throws Exception{
        toServer.println("END");
    }
    /*****************************************************************************
     * Function name: constructor                                                *
     ****************************************************************************/
    public TcpReversiClient(String address, int port, String name) throws Exception{
        waitingForServer = quitGame = false;
        serverAddress = address;
        serverPort = port;
        gameName = name;
        clientEnd = new Socket(serverAddress, serverPort);
        toServer = new PrintWriter(clientEnd.getOutputStream(), true);
        fromServer = new BufferedReader(new InputStreamReader(clientEnd.getInputStream()));

    }

    /*****************************************************************************
     * Function name: readMove                                                   *
     * Operation: read move from server                                          *
     ****************************************************************************/
    public int[] readMove() throws Exception{
        // mark waiting for server
        waitingForServer = true;
        int[] request = new int[2];


        String requestFromServer = fromServer.readLine();
        waitingForServer = false;

        // check online player didn't end game
        if ( requestFromServer.charAt(0) == 'E'){
            endGame();
            request[0] = GAME_ENDED;
            return request;
        }
        // split string to ints
        String splitRequest[] = requestFromServer.split(",");
        int cell[] = new int[2];
        cell[0] = Integer.parseInt(splitRequest[0]);
        cell[1] = Integer.parseInt(splitRequest[1]);
        // if user ended game inform server
        if(quitGame)
            closeGame();
        return cell;
    }

    /*****************************************************************************
     * Function name: writeMove                                                  *
     * Operation: write move to server (other player)                            *
     ****************************************************************************/
    public void writeMove(int i, int j) throws Exception{
        String requestToServer = "play " + i + "," + j + " ";
        toServer.println(requestToServer);
    }

    /*****************************************************************************
     * Function name: openNewGame                                                *
     * Operation: open new game on server                                        *
     ****************************************************************************/
    public Boolean openNewGame(String gameNameIn) throws Exception{
        toServer.println("start " + gameNameIn + " ");
        // read ans from server
        int n = Integer.parseInt(fromServer.readLine());
        if (n == -1)
            return false;

        gameName = gameNameIn;
        return true;
    }

    /*****************************************************************************
     * Function name: joinGame                                                   *
     * Operation: try join online game                                           *
     ****************************************************************************/
    public Boolean joinGame(String gameNameIn) throws Exception{
        toServer.println("join " + gameNameIn + " ");
        // read ans from server
        int n = Integer.parseInt(fromServer.readLine());
        if (n == -1)
            return false;

        gameName = gameNameIn;
        return true;

    }

    /*****************************************************************************
     * Function name: getOpenGaes                                                *
     * Operation: get open games list from server                                *
     ****************************************************************************/
    public String[] getOpenGaes() throws Exception{
        toServer.println("list_games");
        String[] list = fromServer.readLine().split(" ");
        return list;
    }
}
