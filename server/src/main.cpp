/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex7                                                      *
 ****************************************************************************/

#include <fstream>
#include <iostream>
#include <cstdlib>
#include "../include/Server.h"

/*****************************************************************************
 * Function name: main
 * Operation: get the pert from txt file, creat a server with the port.
 ****************************************************************************/

using namespace std;
int main() {
    ifstream input;
    input.open("input.txt");
    int port;
    input >> port;
	cout << port << endl;
    Server server(port);
    try {
        server.start();
    } catch (const char *msg) {
        cout << "Cannot start server. Reason: " << msg << endl;
        exit(-1);
    }
    return 0;
}
