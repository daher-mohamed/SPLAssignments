package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.EncoderDecoderMessage;
import bgu.spl.net.api.bidi.ImplementsBidiMessagingProtocal;
import bgu.spl.net.api.bidi.MessageBus;
import bgu.spl.net.srv.Server;

import java.util.Scanner;


public class ReactorMain {
    public static void  main(String[] args){
        Scanner scanner=new Scanner(System.in);
        MessageBus<String> message=MessageBus.getInstance();
        System.out.println("Enter number of the Word");
        int x=scanner.nextInt();
        while(x!=0){
            System.out.println("Enter Word:");
             String b=scanner.next();
            message.addWord_filter(b);
            x-=1;
        }

       int port=Integer.valueOf(args[0]);
        int numberofThread=Integer.valueOf(args[1]);


        Server.reactor(numberofThread,port,()->new ImplementsBidiMessagingProtocal<>(), EncoderDecoderMessage::new).serve();

    }
}
