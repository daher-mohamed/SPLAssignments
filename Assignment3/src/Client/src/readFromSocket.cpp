//
// Created by spl211 on 02/01/2021.
//
#include <boost/algorithm/string/split.hpp>
#include <boost/algorithm/string/classification.hpp>
#include <vector>
#include "readFromSocket.h"
#include <iostream>

readFromSocket::readFromSocket(ConnectionHandler& connectionHandler)
    :connectionHandler(connectionHandler), connected(true)
{

}

void readFromSocket::run() {
    while (connected) {

        const short bufSize = 2;
        char* buf = new char[2];
        bool didServ = connectionHandler.getBytes(buf, 2);
        string line = connectionHandler.ByteTostring(buf);
        std::vector<std::string> vector;
        split(vector, line, boost::is_any_of(" "));

        if (vector[0] == "NOTIFICATION") {
            char* c=new char[1];
            bool didServ = connectionHandler.getBytes(c, 1);
            didServ = connectionHandler.getLine(line);
            std::vector<std::string> vector2;
            split(vector2, line, boost::is_any_of(" "));
            if (c[0] ==0) {
                line = "NOTIFICATION PM ";
                for (int i = 1; i < vector2.size(); i = i + 1) {
                    line = line + vector2.at(i)+" ";
                }
            }
            else {
                line = "NOTIFICATION Public ";
                for (int i = 1; i < vector2.size(); i = i + 1) {
                    line = line + vector2.at(i) + " ";
                }
            }
            cout << endl;

            if (!didServ) {
                cout << "Disconnected ...byeeeeeeeeee" << endl;
                break;
            }
        }
        else {

            char* buf2 = new char[2];
            connectionHandler.getBytes(buf2, 2);
            short s = connectionHandler.bytesToShort(buf2);
            line = line + " " + to_string(s);
            if (vector[0] == "ACK") {
                if (line == "ACK 3") {
                    connected = false;
                    cout << "ACK 3" << endl;
                    connectionHandler.close();
                    std::exit(0);
                }
                else if (line == "ACK 22"){
                    string line3 = "";
                    didServ = connectionHandler.getLine(line3);
                    if (!didServ) {
                        cout << "Disconnected ...byeeeeeeeeee" << endl;
                        break;
                    }
                    else 
                    {
                        cout << line3 << endl;
                        continue;
                        
                    }
                }
                line = line + " ";
                if (to_string(s) == "7") {
                    for (int i = 0; i < 4; i = i + 1) {
                        char* buf3 = new char[2];
                        connectionHandler.getBytes(buf3, 2);
                        short b = connectionHandler.bytesToShort(buf3);
                        line = line + to_string(b)+" ";
                    }
                }

                 if (to_string(s) == "4" || to_string(s) == "8" || to_string(s) == "11") {
                    didServ = connectionHandler.getLine(line);
                }
                if (!didServ) {
                    cout << "Disconnected ...byeeeeeeeeee" << endl;
                    break;
                }
            }
        }
        cout <<line<< endl;
       
        delete[]buf;
    }
    
}
