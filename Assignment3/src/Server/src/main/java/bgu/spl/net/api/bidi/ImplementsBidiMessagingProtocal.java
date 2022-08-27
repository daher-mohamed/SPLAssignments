package bgu.spl.net.api.bidi;

public class ImplementsBidiMessagingProtocal<T> implements BidiMessagingProtocol<T>{

 private Message message;
 private boolean shouldTerminate = false;
 Connections<T> connections;
 int myID;

 public ImplementsBidiMessagingProtocal(){

     message = new MessageIMpl();
 }



    @Override
    public void start(int connectionId, Connections<T> connections) {
          this.connections=connections;
          myID=connectionId;
    }

    @Override
    public void process(T message) {
        String thisMessage = (String) message;
        String[] messageArray = thisMessage.split(" ");
//        System.out.println("messageArray lock like: "+messageArray.length+" this message "+message);
//        for(int i=0;i<messageArray.length;i=i+1){
//            System.out.println(messageArray[i]);
//        }
        String opcode = messageArray[0];
        String reply = "";
        if(opcode.equals("1")){
            System.out.println("the content of the message is"+messageArray[1]);
            reply = this.message.Register((short) 1,messageArray[1],messageArray[2],messageArray[3],myID);}
        else if(opcode.equals("2")){  reply = this.message.login((short) 2,messageArray[1],messageArray[2],messageArray[3],myID);}
        else if(opcode.equals("3")){
            reply = this.message.logout((short) 3,myID);
            if(reply.equals("ACK 3"))
                shouldTerminate = true;
        }
        else if(opcode.equals("4")){  reply = this.message.FollowMessages((short) 4,messageArray[1],messageArray[2]);}
        else if(opcode.equals("5")){
            String content="";
            for(int i=1;i<messageArray.length;i=i+1){
                content=content+messageArray[i]+" ";
            }
            System.out.println("the content of the message is "+content);
            reply = this.message.POSTMessages((short) 5,content);

        }
        else if(opcode.equals("6")){
            String content="";
            for(int i=2;i<messageArray.length-1;i=i+1){
                content=content+messageArray[i]+" ";
            }
            System.out.println("the content of the message is"+content);
            System.out.println("the content of the message is IS"+messageArray[1]);
            reply = this.message.PMMessages((short) 6,messageArray[1],content,messageArray[messageArray.length-1]);
        }
        else if(opcode.equals("7")){  reply = this.message.LogState((short) 7,"");}
        else if(opcode.equals("8")){  reply =this.message.STATMessages((short) 8,messageArray[1]);}
        else if(opcode.equals("12")){ reply = this.message.BlockMessage("",messageArray[1]);}
       else return;


    }

    @Override
    public boolean shouldTerminate() {
     return shouldTerminate;
    }
}
