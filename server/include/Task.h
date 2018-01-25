/*****************************************************************************
 * Student Name:    Oved Nagar                                               *
 * Id:              302824875                                                *
 * Student Name:    Orly Paknahad                                            *
 * Id:              315444646                                                *
 * Exercise name:   Ex7                                                      *
 ****************************************************************************/


#ifndef SERVER_TASK_H
#define SERVER_TASK_H

/*****************************************************************************
* Class name: Task
* function: save a pointer to function and the args,
* and execute the function when needed
****************************************************************************/
class Task {

    public:
        Task(void *(*func)(void *arg), void* arg) : func(func), arg(arg) {}
        void execute() {
            func(arg);
        }
        virtual ~Task() {}
    private:
        void * (*func)(void *arg);
        void *arg;

};


#endif //SERVER_TASK_H
