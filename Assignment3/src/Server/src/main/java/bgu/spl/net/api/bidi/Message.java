package bgu.spl.net.api.bidi;

public interface Message {
    String Register(short opcode,String username,String password,String birthday,int proID);
    String login(short opcode,String username,String password,String Captcha,int ID);
    String logout(short opcode,int ClientID);//Check if we need to enter The clientID.
    String FollowMessages(short opcode,String Follow_Unfollow,String UserName);
    String POSTMessages(short opcode , String Content);
    String PMMessages(short opcode,String UserName,String Content,String SendingDate);
    String LogState(short opcode,String username);//Check if we need to enter The clientID.
    String STATMessages(short opcode , String ListOfUserNames);
    String NotificationMessages(short opcode , String TypeMessage,String postingUSer,String Content);
    String BlockMessage(String username,String to_Block);
    String ACK(short opcode , String outputMessage);
    String Error (short opcode);
}
