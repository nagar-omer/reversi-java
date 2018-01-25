/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex7                                                      *
 ****************************************************************************/

#ifndef SERVER_COMMANDSMANAGER_H
#define SERVER_COMMANDSMANAGER_H
#include "StartCommand.h"
#include "ListCommand.h"
#include "JoinCommand.h"
#include "PlayCommand.h"
#include "CloseCommand.h"
#include <map>
#include "Command.h"
#include "Game.h"
#include <list>
#include <vector>

/*****************************************************************************
* Class name: CommandsManager
*****************************************************************************/

class CommandsManager {
public:
    CommandsManager();
    ~CommandsManager();
    int executeCommand(string command, vector<string> args, int clientAdddress);
private:
    map<string, Command *> commandsMap;

};

#endif //SERVER_COMMANDSMANAGER_H
