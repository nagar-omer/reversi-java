/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex7                                                      *
 ****************************************************************************/

#ifndef SERVER_GAME_H
#define SERVER_GAME_H
#include <iostream>
#include <string.h>
using namespace std;
/*****************************************************************************
* Class name: Game
*****************************************************************************/

class Game {
private:
    string name;
    int player1;
    int player2;
public:
    Game(string &Name, int Player1){
        this->name=Name;
        this->player1=Player1;
        this->player2=0;
    }

    bool Has2Players() {
        if(this->player2 == 0) return false;
        else return true;
    }

    //get and set methods
    string getName(){return this->name;}
    void setName(string Name){this->name= Name;}
    int getPlayer1(){return this->player1;}
    void setPlayer1(int Player1){this->player1=Player1;}
    int getPlayer2(){return this->player2;}
    void setPlayer2(int Player2){this->player2=Player2;}
};


#endif //SERVER_GAME_H
