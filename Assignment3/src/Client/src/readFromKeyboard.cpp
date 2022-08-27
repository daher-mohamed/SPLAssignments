//
// Created by spl211 on 01/01/2021.
//

#include "readFromKeyboard.h"
#include <iostream>
using namespace std;
readFromKeyboard::readFromKeyboard(ConnectionHandler& connectionHandler)
    :connectionHandler(connectionHandler), connected(true)
{
}
void readFromKeyboard::run(){
    while (connected) {
        char* opcodeInBytes = new char[1024];
        const short bufSize = 1024;
        char buf[bufSize];
        std::cin.getline(buf, bufSize);
        std::string line(buf);
        connectionHandler.getOpcode(line, opcodeInBytes);
        delete[] opcodeInBytes;
    }
}