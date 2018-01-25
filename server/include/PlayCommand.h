/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex7                                                      *
 ****************************************************************************/
#ifndef SERVER_PLAYCOMMAND_H
#define SERVER_PLAYCOMMAND_H
#include <algorithm>
#include <iostream>
#include <sstream>
#include <unistd.h>
#include <string>
#include "Command.h"
using namespace std;
/*****************************************************************************
* Class name: PlayCommand inherite from Command
* function name: execute
*               params: vector of string, player socket
*               operation: send to other player the move
****************************************************************************/
class PlayCommand : public Command{
public:
    virtual void execute(vector<string> args, int player) {
        //remove "\n" from name of game
        args[1].erase(std::remove(args[1].begin(), args[1].end(), '\n'), args[1].end());

        char * msg = "-1\n";
        //check if the game exist- send the game name and player
        int otherPlayer = GameManager::getGameManager()->getOtherPlayer(player);
        if (otherPlayer > 0) {
            string str= args[1].c_str();
            str+='\n';
            write(otherPlayer, str.c_str(), strlen(str.c_str()));
        } else {
            //the game is not exist- send to player -1
            write(player, msg, strlen(msg));
        }
    }
};

#endif //SERVER_PLAYCOMMAND_H