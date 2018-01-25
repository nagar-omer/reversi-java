/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex7                                                      *
 ****************************************************************************/


#ifndef SERVER_THREADPOOL_H
#define SERVER_THREADPOOL_H
#include "Task.h"
#include <queue>
#include <pthread.h>
using namespace std;

/*****************************************************************************
* Class name: ThreadPool
* function: save a queue of Tasks and activate them ordinary
****************************************************************************/
class ThreadPool {
public:
    ThreadPool(int threadsNum);
    void addTask(Task *task);
    void terminate();
    virtual ~ThreadPool();
private:
    queue<Task *> tasksQueue;
    pthread_t* threads;
    void executeTasks();
    bool stopped;
    pthread_mutex_t lock;
    static void *execute(void *arg);
};

#endif //UNTITLED1_THREADPOOL_H
