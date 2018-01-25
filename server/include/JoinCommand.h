/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex7                                                      *
 ****************************************************************************/
#ifndef SERVER_JOINCOMMAND_H
#define SERVER_JOINCOMMAND_H
#include <algorithm>
#include <iostream>
#include <sstream>
#include <unistd.h>
#include <string>
#include "Command.h"
using namespace std;
/*****************************************************************************
* Class name: JoinCommand inherite from Command
* function name: execute
*               params: vector of string, player socket
*               operation: call the func JoinGame, send to client their turns
****************************************************************************/

class JoinCommand : public Command{
public:
    virtual void execute(vector<string> args, int player) {

        //remove "\n" from name of game
        args[1].erase(std::remove(args[1].begin(), args[1].end(), '\n'), args[1].end());

        char *msg = "-1\n";
        //check if the game exist- send the game name and player
        int otherPlayer = GameManager::getGameManager()->joinGame(args[1], player);
        if (otherPlayer > 0) {
            cout << "start a game - send to both players their colors" << endl;
            char *msg1 = "1\n";
            char *msg2 = "2\n";

            write(otherPlayer, "1\n", strlen("1\n"));
            write(player, "2\n", strlen("2\n"));
            cout << "game name: "<< args[1] << " has started " <<endl;
        } else {
            //the game is not exist- send to player -1
            write(player, "-1\n", strlen("-1\n"));
        }
    }
};

#endif //SERVER_JOINCOMMAND_H