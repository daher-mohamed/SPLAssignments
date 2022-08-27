package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.bidi.EncoderDecoderMessage;
import bgu.spl.net.api.bidi.ImplementsBidiMessagingProtocal;
import bgu.spl.net.api.bidi.MessageBus;
import bgu.spl.net.srv.BaseServerImpl;

import java.util.Scanner;

public class TPCMain {
    public static  void main(String[] args){
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
        int port=Integer.parseInt(args[0]);
        BaseServerImpl<String> server=new BaseServerImpl<>(port,()->new ImplementsBidiMessagingProtocal<>(),()->new EncoderDecoderMessage<>());
        server.serve();
    }
}
