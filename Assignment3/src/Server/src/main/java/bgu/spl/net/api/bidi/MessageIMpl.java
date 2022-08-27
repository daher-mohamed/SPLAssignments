package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.User;

public class MessageIMpl implements Message{
      MessageBus<String> data=MessageBus.getInstance();
      String myName=null;
      int counter=0;
      int proID;
      boolean register=false;

    @Override
    public String Register(short opcode, String username, String password, String birthday,int proID)//There is need to add parameters for ConnectionHandler.
    {
        this.proID=proID;

        synchronized (data.getEachClientAndHisPassword())
        {
            if(myName!=null ||!data.isRegistered(username)||register)
                return this.Error(opcode);
            else {
                data.registerClient(username, password,birthday);
                register=true;
                return this.ACK(opcode, "");

            }
        }

    }

    @Override
    public String login(short opcode, String username, String password, String Captcha,int ID) {
        synchronized (data.getEachClientAndHisPassword())
        {
            if (Captcha.equals("0")||data.isRegistered(username)
                    || data.isLoggedIn(username)
                    || counter !=0
                    || !password.equals(data.getPassword(username))) {
                System.out.println("I will return error");
                return this.Error(opcode);
            } else {
                if(!register){
                    proID=ID;
                }
                System.out.println("I will return NOT -------error");
                System.out.println(myName+" from login");
                System.out.println(username+" from login");
                counter = counter + 1;
                this.myName = username;
                data.login(username, password, ID);
                register=true;
                this.ACK(opcode, "");
                data.sendNotification(username,ID);
            }
        }
        return "";
    }

    @Override
    public String logout(short opcode,int ClientID) {
        if ( counter <= 0||!data.isLoggedIn(this.myName)) {
            Error(opcode);
        }
        else {
            counter--;
            data.logout(myName);
            myName = null;
            ACK(opcode, "");
            data.connections.disconnect(ClientID);
            return "ACK 3";
        }


        return"";
    }

    @Override
    public String FollowMessages(short opcode, String Follow_Unfollow, String UserName) {
        if ( Follow_Unfollow.equals("0")&&(counter <= 0||!data.isLoggedIn(this.myName)||data.isRegistered(UserName)||data.I_Follow(this.myName,UserName))||data.Block_me(myName,UserName)){

            return Error(opcode);
        }
        else if(Follow_Unfollow.equals("1")&&(counter <= 0||!data.isLoggedIn(this.myName)||data.isRegistered(UserName)||!data.I_Follow(this.myName,UserName)||data.Block_me(myName,UserName)))
        {
            return Error(opcode);
        }
        else if(Follow_Unfollow.equals("0")) {
            data.Add_NewFollow(this.myName,UserName);
            return ACK(opcode,UserName);
        }
        else{
            data.RemoveFollow(this.myName,UserName);
            return ACK(opcode,UserName);
        }

    }


    @Override
    public String POSTMessages(short opcode, String Content) {
        if ( counter <= 0||!data.isLoggedIn(this.myName)) {
            return Error(opcode);
        }

      else{
          data.PostMessage(this.myName,Content);
          return ACK(opcode,null);
        }
    }

    @Override
    public String PMMessages(short opcode, String UserName, String Content, String SendingDate) {
        System.out.println("user name: ---------"+UserName);

        if(UserName==null||data.isRegistered(UserName)|| data.Block_me(UserName,myName)){
            Error2(UserName);


        }
        else if(!data.isLoggedIn(myName)||!data.I_Follow(myName,UserName)){
            Error(opcode);
        }
        else{
        data.PMMessages(opcode,UserName,Content,SendingDate,myName);
           ACK((short) opcode,"");
       }
        return "";
    }



    @Override
    public String LogState(short opcode, String username) {



         if(data.isRegistered(myName)||!data.isLoggedIn(myName)){
             Error(opcode);
             System.out.println("I am here");
         }
         else
        data.LogState(opcode,myName);
        return "";


    }

    @Override
    public String STATMessages(short opcode, String ListOfUserNames) {
        if(data.isRegistered(myName)||!data.isLoggedIn(myName)){
            Error(opcode);
        }
        else{
            String result=data.State_Message(opcode,myName,ListOfUserNames);
            if(result.equals("error"))
                Error(opcode);
        }
       return null;
    }

    @Override
    public String NotificationMessages(short opcode, String TypeMessage, String postingUSer, String Content) {
        return null;
    }

    @Override
    public String BlockMessage(String username, String to_Block) {
        if(data.isRegistered(myName)||!data.isLoggedIn(myName)||data.isRegistered(to_Block)){
            Error((short)12);
        }
        else{
            data.BlockMessage(myName,to_Block);
            ACK((short) 12,"");
        }
        return null;
    }


    @Override
    public String ACK(short opcode, String outputMessage) {
        data.connections.send(proID,"10 "+opcode+" "+outputMessage);
    return"";
    }
    @Override
    public String Error(short opcode) {
         System.out.println("this opcode "+opcode);
        data.connections.send(proID,"11 "+opcode);
        return "";

    }
    private void Error2(String userName) {
        System.out.println("Task 1 saker ");
        data.connections.send(proID,"10 22 "+userName+" isn't applicable for private messages");
    }
}
