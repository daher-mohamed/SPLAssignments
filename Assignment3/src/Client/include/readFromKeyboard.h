//
// Created by spl211 on 01/01/2021.
//

#ifndef BOOST_ECHO_CLIENT_READFROMKEYBOARD_H
#define BOOST_ECHO_CLIENT_READFROMKEYBOARD_H

#include <iostream>
#include <mutex>
#include <thread>
#include "connectionHandler.h"

class readFromKeyboard {
private:

    ConnectionHandler& connectionHandler;
    bool connected;

public:
    readFromKeyboard(ConnectionHandler& connectionHandler);
    void run();

};
#endif //BOOST_ECHO_CLIENT_READFROMKEYBOARD_H
