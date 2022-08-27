CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -lboost_thread -pthread

all: bin/echoClient

bin/echoClient: bin/connectionHandler.o bin/echoClient.o bin/readFromSocket.o bin/readFromKeyboard.o
	g++ -o bin/echoClient bin/connectionHandler.o bin/readFromSocket.o bin/readFromKeyboard.o bin/echoClient.o $(LDFLAGS)
	
bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/echoClient.o: src/echoClient.cpp
	g++ $(CFLAGS) -o bin/echoClient.o src/echoClient.cpp
bin/readFromSocket.o: src/readFromSocket.cpp
	g++ $(CFLAGS) -o bin/readFromSocket.o src/readFromSocket.cpp
bin/readFromKeyboard.o: src/readFromKeyboard.cpp
	g++ $(CFLAGS) -o bin/readFromKeyboard.o src/readFromKeyboard.cpp
	
.PHONY: clean
clean:
	rm -f bin/*
