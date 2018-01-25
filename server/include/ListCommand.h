/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex7                                                      *
 ****************************************************************************/

#ifndef SERVER_LISTCOMMAND_H
#define SERVER_LISTCOMMAND_H
#include <algorithm>
#include <iostream>
#include <sstream>
#include <unistd.h>
#include <string>

#include "Command.h"

using namespace std;
/*****************************************************************************
* Class name: ListCommand inherite from Command
* function name: execute
*               params: vector of string, player socket
*               operation: call the func getListOfOnePlayer, send to client
*               the list
****************************************************************************/

class ListCommand : public Command {
public:
    virtual void execute(vector<string> args, int player) {
        cout << "execute command list ! :)" << endl;
        //get the list from manager
        string str = GameManager::getGameManager()->getListOfOnePlayer();
        str+="\n";

        //send msg to client
        int n = write(player, str.c_str(), strlen(str.c_str()));
        close(player);
    }
};

#endif //SERVER_LISTCOMMAND_H
