package bgu.spl.net.impl.echo;


import bgu.spl.net.api.bidi.EncoderDecoderMessage;
import bgu.spl.net.api.bidi.ImplementsBidiMessagingProtocal;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.api.bidi.MessageBus;
import bgu.spl.net.srv.BaseServerImpl;

public class MAinServer {
    public static  void main(String[] args){
        MessageBus<String> message=MessageBus.getInstance();
        message.addWord_filter("war");

        BaseServerImpl<String> server=new BaseServerImpl<>(1232,()->new ImplementsBidiMessagingProtocal<>(),()->new EncoderDecoderMessage<>());
        server.serve();
    }
}
