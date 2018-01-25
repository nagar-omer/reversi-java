/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex7                                                      *
 ****************************************************************************/
#include "../include/GameManager.h"
GameManager *GameManager::instance = 0;

/*****************************************************************************
* function name: addNewGame
* params: name, client socket
* operation: if game not exist push ito vector
*****************************************************************************/
int GameManager::addNewGame(string name, int clientAdddress)
{
    for (list<Game*>::iterator it= games->begin(); it != games->end(); ++it)
    {
        if (name.compare((*it)->getName()) == 0)
        {
            //game is exist
            return -1;
        }
    }

    //add the game
    games->push_back(new Game(name, clientAdddress));
    cout << " game has added!" << endl;
    return 1;
}

/*****************************************************************************
* function name: joinGame
* params: name of game, client socket
* operation: if game not exist or has alredy player2 return -1
* return: the address of first player
*****************************************************************************/
int GameManager::joinGame(string name ,int player2){
    //find game
    for (list<Game*>::iterator it= games->begin(); it != games->end(); ++it)
    {
        if (name.compare((*it)->getName()) == 0)
        {
            //game is exist
            if(!(*it)->Has2Players())
            {
                //there is no player2
                (*it)->setPlayer2(player2);
                return (*it)->getPlayer1();
            }
        }
    }
    return -1; //error connect to game
}

/*****************************************************************************
* function name: getListOfOnePlayer
* params: none
* operation: get all over the list
* return: string of the plays
*****************************************************************************/
string GameManager::getListOfOnePlayer(){
    string str="";
    for (list<Game*>::iterator it= this->games->begin(); it != this->games->end(); ++it)
    {
        if ((*it)->getPlayer2() == 0)
        {
            str += (*it)->getName();
            str += " ";
        }
    }
    cout << "this is string of list of game:" << str << endl;
    return str;
}

/*****************************************************************************
* function name: closeGame
* params: name of game, client socket
* operation: erase the game
* return: other player
*****************************************************************************/
int GameManager::closeGame(string name, int player){
    list<Game*>* temp = this->getListOfGames();
    for (list<Game*>::iterator it= temp->begin(); it != temp->end(); ++it)
    {
        if (name.compare((*it)->getName()) == 0)
        {
            //delete game from origin list
            this->games->erase(it);

            //return other player
            if((*it)->getPlayer1()== player)
                return (*it)->getPlayer2();
            else
                return (*it)->getPlayer1();
        }
    }
}

/*****************************************************************************
* function name: getOtherPlayer
* params: client socket
* operation: find his game and retrun the other player
* return: other player
*****************************************************************************/
int GameManager::getOtherPlayer(int player){
    for (list<Game*>::iterator it= this->games->begin(); it != this->games->end(); ++it)
    {
        if ((*it)->getPlayer1() == player) {
            return (*it)->getPlayer2();
        }
        else if((*it)->getPlayer2()== player){
            return (*it)->getPlayer1();
        }
    }
}

/*****************************************************************************
* function name: getGame
* params: name
* return: a Game if exist
*****************************************************************************/
Game *GameManager::getGame(string name) {
    for (list<Game*>::iterator it = this->games->begin(); it != this->games->end(); ++it) {
        if ((*it)->getName().compare(name)==0) {
            return (*it);
        }
    }
    return  NULL;
}
