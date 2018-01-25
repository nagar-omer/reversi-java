
a.out: Server.o CommandsManager.o GameManager.o ThreadPool.o main.o
	g++ Server.o CommandsManager.o GameManager.o ThreadPool.o main.o -std=c++0x -pthread

Server.o: src/Server.cpp include/Server.h include/Task.h include/ThreadPool.h include/CommandsManager.h
	g++ -c src/Server.cpp -std=c++0x -pthread

CommandsManager.o: src/CommandsManager.cpp include/CommandsManager.h include/Game.h include/StartCommand.h include/ListCommand.h  include/JoinCommand.h include/PlayCommand.h include/CloseCommand.h include/Command.h
	g++ -c src/CommandsManager.cpp -std=c++0x -pthread

GameManager.o: src/GameManager.cpp include/GameManager.h
	g++ -c src/GameManager.cpp -std=c++0x -pthread

ThreadPool.o: src/ThreadPool.cpp include/ThreadPool.h include/Task.h
	g++ -c src/ThreadPool.cpp -std=c++0x -pthread

main.o: src/main.cpp include/Server.h      
	g++ -c src/main.cpp -std=c++0x -pthread
