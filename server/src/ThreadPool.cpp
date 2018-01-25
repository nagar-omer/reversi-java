/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex7                                                      *
 ****************************************************************************/

#include "../include/ThreadPool.h"
#include <unistd.h>

/*****************************************************************************
* function name: ThreadPool
* params: number of threads and a flag
* operation: Constructor
*****************************************************************************/
ThreadPool::ThreadPool(int threadsNum) : stopped(false) {
    threads = new pthread_t[threadsNum];
    for (int i = 0; i < threadsNum; i++)
    {
        pthread_create(threads + i, NULL, execute, this);
    }
    pthread_mutex_init(&lock, NULL);
}

/*****************************************************************************
* function name: execute
* params: arg to a func
* return: none
* operation: call executeTasks
*****************************************************************************/
void* ThreadPool::execute(void *arg) {
    ThreadPool *pool = (ThreadPool *)arg;
    pool->executeTasks();
}

/*****************************************************************************
* function name: addTask
* params: pointer to task
* return: none
* operation: add the task to the queue
*****************************************************************************/
void ThreadPool::addTask(Task *task) {
    tasksQueue.push(task);
}

/*****************************************************************************
* function name: executeTasks
* params: none
* return: none
* operation: execute the tasks in the queue and fill it when its empty
*****************************************************************************/
void ThreadPool::executeTasks() {
    while (!stopped) {
        pthread_mutex_lock(&lock);
        if (!tasksQueue.empty()) {
            Task* task = tasksQueue.front();
            tasksQueue.pop();
            pthread_mutex_unlock(&lock);
            task->execute();
        }
        else {
            pthread_mutex_unlock(&lock);
            sleep(1);
        }
    }
}

/*****************************************************************************
* function name: terminate
* return: none
* operation: make stop to be true
*****************************************************************************/
void ThreadPool::terminate() {
    pthread_mutex_destroy(&lock);
    stopped = true;
}

/*****************************************************************************
* function name: ~ThreadPool
* operation: destructor
*****************************************************************************/
ThreadPool::~ThreadPool() {
    delete[] threads;
}
