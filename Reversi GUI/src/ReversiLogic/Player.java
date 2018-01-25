package ReversiLogic;
/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex3                                                      *
 ****************************************************************************/


public class Player {
    private static int serialAssign = 1;

    public enum playerType { PC , ONLINE_PLAYER, LOCAL_PLAYER};

    private String nickName;
    private int serialNumber, totalwin, totalLoss;
    private playerType type;

    /*****************************************************************************
     * Function name: constructor                                                *
     * Input: player nick name, boolean val for Comp user initialized to false   *
     * Output: initiate name, wins losses to 0, giving the player an Id num      *
     ****************************************************************************/
    public Player(String nick, playerType type){
        nickName = nick;
        serialNumber = serialAssign++;
        totalwin = 0;
        totalLoss = 0;
        this.type = type;
    }

    /*****************************************************************************
     * Function name: getSerial                                                  *
     * Output: returns player serial ID                                          *
     ****************************************************************************/
    public int getSerial() {
        return serialNumber;
    }

    /*****************************************************************************
     * Function name: getWins                                                    *
     * Output: returns total number of wins                                      *
     ****************************************************************************/
    public int getWins() {
        return totalwin;
    }

    /*****************************************************************************
     * Function name: getLoss                                                    *
     * Output: returns total number of losses                                    *
     ****************************************************************************/
    public int getLoss() {
        return totalLoss;
    }

    /*****************************************************************************
     * Function name: nick                                                       *
     * Output: returns player nick name                                          *
     ****************************************************************************/
    public String nick() {
        return nickName;
    }

    /*****************************************************************************
     * Function name: isComputer                                                 *
     * Output: returns boolean value to indicate if the player is representing   *
     *           a compute                                                       *
     ****************************************************************************/
    public playerType getPlayerType(){
        return type;
    }


    /*****************************************************************************
     * Function name: operator ++Player                                          *
     * Operation: increases the total number of wins by 1                        *
     ****************************************************************************/
    public int win(Player player){
        (player.totalwin)++;
        return player.totalwin;
    }

    /*****************************************************************************
     * Function name: operator --Player                                          *
     * Operation: increases the total number of losses by 1                      *
     ****************************************************************************/
    public int loose(Player player){
        (player.totalLoss)++;
        return player.totalLoss;
    }

}



