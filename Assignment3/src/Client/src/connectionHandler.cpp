#include "connectionHandler.h"
#include <boost/algorithm/string/split.hpp>
#include <boost/algorithm/string/classification.hpp>
#include <vector>
#include <istream>
#include "readFromKeyboard.h"
#include<string>
using namespace std;

using boost::asio::ip::tcp;
using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;

ConnectionHandler::ConnectionHandler(string host, short port) : host_(host), port_(port), io_service_(), socket_(io_service_) {}

ConnectionHandler::ConnectionHandler() : host_(), port_(), io_service_(), socket_(io_service_) {
}
ConnectionHandler::~ConnectionHandler() {
    close();
}

bool ConnectionHandler::connect() {
    std::cout << "Starting connect to "
        << host_ << ":" << port_ << std::endl;
    try {
        tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
        boost::system::error_code error;
        socket_.connect(endpoint, error);
        if (error)
            throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
    boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp) {
            tmp += socket_.read_some(boost::asio::buffer(bytes + tmp, bytesToRead - tmp), error);
        }
        if (error)
            throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
 
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
    boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp) {
            tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
        if (error)
            throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::getLine(std::string& line) {
    return getFrameAscii(line, ';');
}

bool ConnectionHandler::sendLine(std::string& line) {
    return sendFrameAscii(line, ';');
}


bool ConnectionHandler::getFrameAscii(std::string& frame, char delimiter) {
    char ch;

    // Stop when we encounter the null character. 

    // Notice that the null character is not appended to the frame string.

    try {

        do {
            if (!getBytes(&ch, 1))
            {
                return false;
            }
            if (ch != ';'&&ch!='\0') {
                frame.append(1, ch);
               
            }
            if (ch == '\0') {
                frame.append(1, ' ');
            }

        } while (delimiter != ch);

    }
    catch (std::exception& e) {

        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;

        return false;

    }

    return true;

}


bool ConnectionHandler::sendFrameAscii(const std::string& frame, char delimiter) {
    bool result = sendBytes(frame.c_str(), frame.length());
    if (!result) return false;
    return sendBytes(&delimiter, 1);
}

// Close down the connection properly.
void ConnectionHandler::close() {
    try {
        socket_.close();
    }
    catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl; 
    }
}

void ConnectionHandler::shortToBytes(short num, char* bytesArray) {
    bytesArray[0] = ((num >> 8) & 0xFF);
    bytesArray[1] = (num & 0xFF);
}
short ConnectionHandler::bytesToShort(char* bytesArr)
{
    short result = (short)((bytesArr[0] & 0xff) << 8);
    result += (short)(bytesArr[1] & 0xff);
    return result;
}

void ConnectionHandler::getOpcode(std::string str, char* opcodeInBytes) {
    std::vector<std::string> vector;
    split(vector, str, boost::is_any_of(" "));
    std::string op = vector.at(0);
    std::string toEncode = "";
    char* buf1 = new char[1];
    char* buf2 = new char[1];

    if (op == "REGISTER") {
        sendOpcode(1, buf1, buf2, opcodeInBytes);
        toEncode = toEncode + vector.at(1) + '\0' + vector.at(2) + '\0' + vector.at(3);
    }
    else if (op == "LOGIN") {
        sendOpcode(2, buf1, buf2, opcodeInBytes);
        toEncode = toEncode + vector.at(1) + '\0' + vector.at(2) + '\0' + vector.at(3);
      
    }
    else if (op == "LOGOUT") {
        sendOpcode(3, buf1, buf2, opcodeInBytes);
        //toEncode = toEncode;
    }
    else if (op == "FOLLOW") {
        sendOpcode(4, buf1, buf2, opcodeInBytes);
        toEncode = toEncode + vector.at(1) + '\0' + vector.at(2);
    }
    else if (op == "POST") {
        sendOpcode(5, buf1, buf2, opcodeInBytes);
        for (int i = 1; i < vector.size(); i = i + 1) {
            toEncode = toEncode + vector.at(i) + '\0';
        }
    }
    else if (op == "PM") {
        sendOpcode(6, buf1, buf2, opcodeInBytes);
        for (int i = 1; i < vector.size(); i = i + 1)
        {
            toEncode = toEncode + vector.at(i) + '\0';
        }
        toEncode += " " + this->currentDateTime();
      
    }

    else if (op == "LOGSTAT") {
        sendOpcode(7, buf1, buf2, opcodeInBytes);
    }
    else if (op == "STAT") {
        sendOpcode(8, buf1, buf2, opcodeInBytes);
        toEncode = toEncode + vector.at(1);
    }
    else if (op == "BLOCK") {
        sendOpcode(12, buf1, buf2, opcodeInBytes);
        toEncode = toEncode + vector.at(1);
    }
    else return;
    sendLine(toEncode);
    delete[] buf1;
    delete[] buf2;
}

std::string ConnectionHandler::ByteTostring(char* opcode) {

    string str ="";
        
        char* op = new char[2];
        op[0] = opcode[0];
        op[1] = opcode[1];
        short opcodeNUmber = this->bytesToShort(op);
        delete[]op;
     
        if (opcodeNUmber == 10) {
           
            str = "ACK"; 
        }
        else if (opcodeNUmber == 9) {
        
            str = "NOTIFICATION ";
        }
        else {
            str = "ERROR"; 
        }
    return str;
}
std::string ConnectionHandler::ByteTostring2(char* opcode) {
        
}

void ConnectionHandler::sendOpcode(short num, char* buf1, char* buf2, char* opcodeInBytes) {
    this->shortToBytes(num, opcodeInBytes);
    buf1[0] = opcodeInBytes[0];
    buf2[0] = opcodeInBytes[1];
    sendBytes(buf1, 1);
    sendBytes(buf2, 1);
}

void ConnectionHandler::sendNumberCourseasShort(std::string courseNumber, char* opcodeInBytes) {
    int number;
    std::istringstream(courseNumber) >> number;
    short shortCourseNumber = (short)number;
    shortToBytes(shortCourseNumber, opcodeInBytes);
    sendBytes(opcodeInBytes, 2);
}

void ConnectionHandler::sendRemainningData(std::string toEncode) {
    const char* inUTF8 = toEncode.c_str();
    char* buf = new char[1];
    int length = strlen(inUTF8);
    for (int j = 0; j < length; j = j + 1) {
        if (inUTF8[j] == ' ') {
            buf[0] = '\0';
            sendBytes(buf, 1);
        }
        else {
            buf[0] = inUTF8[j];
            sendBytes(buf, 1);
            if (length == j + 1) {
                buf[0] = '\0';
                sendBytes(buf, 1);
            }
        }
    }
    buf[0] = ';';
    sendBytes(buf, 1);
    delete[] buf;
}
const std::string ConnectionHandler::currentDateTime(){
    time_t     now = time(0);
    struct tm  tstruct;
    char       buf[80];
    tstruct = *localtime(&now);
    // for more information about date/time format
    strftime(buf, sizeof(buf), "%Y-%m-%d.%X", &tstruct);

    return buf;
}

