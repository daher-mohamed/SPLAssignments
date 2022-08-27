#include <stdlib.h>
#include "connectionHandler.h"
#include "readFromKeyboard.h"
#include "readFromSocket.h"
#include <boost/thread.hpp>
#include <iostream>
#include <string>
#include <stdio.h>
#include <time.h>
#include <thread>
/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int main(int argc, char* argv[]) {


    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
       return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);
    cout << "I ame here" << endl;
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }

    readFromKeyboard taskOfThreadNumber1(connectionHandler);
    readFromSocket taskOfThreadNumber2(connectionHandler);
    boost::thread th1(&readFromKeyboard::run,&taskOfThreadNumber1);
    taskOfThreadNumber2.run();
    th1.join();
    return 0;
}
// Get current date/time, format is YYYY-MM-DD.HH:mm:ss


