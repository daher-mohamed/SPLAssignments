////////////////////////////////////*********Run the Server*******\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
 1-Download the file:211886783#_212180772.zip
 2-Extract the file.
 3-Enter the server file and open it in the Terminal
  *-Enter this in the Terminal 
     mvn clean
     mvn compile
     mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.BGSServer.ReactorMain" -Dexec.args="4535 1"
    
 4-Now Enter the Number of the Word that will be use to filter in the server.
 5-Enter the words.
/////////////////////////////////**********Run the Client********\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
  
 1-Enter The Client file.
 2-Open in the Terminal.
  *-Enter this in the Terminal
    make clean all
    ./bin/echoClient 127.0.0.1 4535

And Now the program is starting.

    










Example of Inputs:
REGISTER DAHER 2nbe 3-2-1999
LOGIN DAHER 2nbe 1

REGISTER ALI 3ZC4 2-2-2001
LOGIN ALI 2nbe 1

REGISTER SABER 2 22-2-2001
LOGIN SABER 2nbe 1

REGISTER ADAM 2 3-3-2001
LOGIN ADAM 2nbe 1

LOGSTAT 

STAT DAHER|SABER|ALI 

LOGOUT

