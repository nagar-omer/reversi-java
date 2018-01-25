/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex7                                                      *
 ****************************************************************************/

#ifndef SERVER_SERVER_H
#define SERVER_SERVER_H

#include "CommandsManager.h"
#include "Task.h"
#include "ThreadPool.h"
#include <algorithm>
#include <string>
#include <vector>
#include <cstring>
#include <sys/socket.h>
#include <netinet/in.h>
#include "Server.h"
#include <unistd.h>

#define MAX_CONNECTED_CLIENTS 50
#define THREADS_NUM 5
#define TASKS_NUM 1000
using namespace std;

struct paramsToThread {
    vector<string> args;
    int clientSocket1;
    CommandsManager *commandsManager;
};

struct paramsToExitThread {
    ThreadPool *pool;
    Task *tasks;
    int numOfTasks;
    paramsToThread * paramArray;
};

/*****************************************************************************
 * Class name: Server
 * Function: connect between two clients
 ****************************************************************************/
class Server {
public:
    Server(int port);
    void start();

private:
    int port;
    int serverSocket; // the socket's file descriptor
    int numOfTasks;
    ThreadPool *pool;
    Task *tasks[TASKS_NUM];
    paramsToThread * paramArray[TASKS_NUM];

    CommandsManager commandsManager;
};


#endif //SERVER_SERVER_H
