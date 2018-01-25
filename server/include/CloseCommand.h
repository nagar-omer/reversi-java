/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex7                                                      *
 ****************************************************************************/

#ifndef SERVER_CLOSECOMMAND_H
#define SERVER_CLOSECOMMAND_H

#include <algorithm>
#include <iostream>
#include <sstream>
#include <unistd.h>
#include <string>
#include "Command.h"
using namespace std;
/*****************************************************************************
* Class name: CloseCommand inherite from Command
* function name: execute
*               params: vector of string, player socket
*               operation: call the func closeGame, close socket
****************************************************************************/

class CloseCommand : public Command{
public:
    virtual void execute(vector<string> args, int player) {
        //remove "\n" from name of game
        args[1].erase(std::remove(args[1].begin(), args[1].end(), '\n'), args[1].end());

        //get the list from manager
        int otherPlayer = GameManager::getGameManager()->closeGame(args[1], player);
        if (otherPlayer > 0) {
            char * msg = "END\n";
            //send msg to other client
            int n = write(otherPlayer, "END\n", strlen("END\n"));
            close(player);
            cout << "game " << args[1]  << " has ended" << endl;
        }
    }
};


#endif //SERVER_CLOSECOMMAND_H