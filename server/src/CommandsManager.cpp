/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex7                                                      *
 ****************************************************************************/

#include "../include/CommandsManager.h"
/*****************************************************************************
* function name: CommandsManager()
* operation: constructor
*****************************************************************************/
CommandsManager::CommandsManager() {
    this->commandsMap["start"] = new StartCommand();
    this->commandsMap["list_games"] = new ListCommand();
    this->commandsMap["join"] = new JoinCommand();
    this->commandsMap["play"] = new PlayCommand();
    this->commandsMap["close"] = new CloseCommand();
}

/*****************************************************************************
* function name: executeCommand
* params: command, args, client socket
* operation: execute spesific command
*****************************************************************************/
int CommandsManager::executeCommand(string command, vector<string> args, int clientAddress) {
    Command *commandObj = this->commandsMap[command];
    if(commandObj != NULL){
        commandObj->execute(args, clientAddress);
    } else{
        cout << "dont have client address" << endl;
        return -1;
    }
    return 0;
}


/*****************************************************************************
* function name: ~CommandsManager()
* operation: destructor
*****************************************************************************/
CommandsManager::~CommandsManager() {
    map<string, Command *>::iterator it;
    for (it = commandsMap.begin(); it != commandsMap.end(); it++) {
        delete it->second;
    }
}
