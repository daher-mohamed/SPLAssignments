//
// Created by spl211 on 01/01/2021.
//

#ifndef BOOST_ECHO_CLIENT_READFROMSOCKET_H
#define BOOST_ECHO_CLIENT_READFROMSOCKET_H
using namespace std;

#include <mutex>
#include "connectionHandler.h"

class readFromSocket {
private:
    ConnectionHandler& connectionHandler;
    bool connected;

public:

    readFromSocket(ConnectionHandler& connectionHandler);
    void run();

};
#endif //BOOST_ECHO_CLIENT_READFROMSOCKET_H
