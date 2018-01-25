/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex7                                                      *
 ****************************************************************************/

#ifndef SERVER_COMMAND_H
#define SERVER_COMMAND_H
#include <string>
#include <list>
#include <vector>
#include "Game.h"
#include "GameManager.h"

using namespace std;
/*****************************************************************************
* Class name: interface Command
****************************************************************************/
class Command {
public:
    virtual void execute(vector<string> args, int clientAddress) = 0;
    virtual ~Command() {}
};

#endif //SERVER_COMMAND_H
