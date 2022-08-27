package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.ConnectionHandler;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageBus<T> {
    private ConcurrentLinkedQueue<String> filtered=new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<String> Word_filtered=new ConcurrentLinkedQueue<>();

    private ConcurrentHashMap<String,Integer> eachClientAndHisID=new ConcurrentHashMap<>() ;//Using for check if the client is login
    private ConcurrentHashMap<String, Pair<String,Boolean>> eachClientAndHisPassword=new ConcurrentHashMap<>();//the boolean is false if not logged in//--Use here another map because pair didn't work.
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> eachClientAndHisFollowers=new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> eachClientAndHisPosts=new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> eachClientAndHisPM=new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,String> eachClientAndHisBirthday=new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,ConcurrentLinkedQueue<String>> eachClientAndHisNotifications=new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> eachClientAndHeFollowed=new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> eachClientAndHeBlocked=new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> eachClientAndBlockedfrom=new ConcurrentHashMap<>();


    Connections<T> connections;

    public ConcurrentHashMap<String, Integer> getEachClientAndHisID() {
        return eachClientAndHisID;
    }

    public ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> getEachClientAndHisFollowers() {
        return eachClientAndHisFollowers;
    }

    public ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> getEachClientAndHisPosts() {
        return eachClientAndHisPosts;
    }

    public ConcurrentHashMap<String, String> getEachClientAndHisBirthday() {
        return eachClientAndHisBirthday;
    }

    public ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> getEachClientAndHisNotifications() {
        return eachClientAndHisNotifications;
    }

    public Connections<T> getConnections() {
        return connections;
    }

    //This for login-----------------------------------------------
    public boolean isRegistered(String username) {
        if(username!=null)
        return this.eachClientAndHisPassword.get(username) == null;
        return true;

    }

    public void registerClient(String username,String password,String Birthday) {
        //For do:/*
        // connections
        // eachClientAndHisUserName
        // */
        eachClientAndHisPassword.putIfAbsent((String)username,new Pair<>((String)password,false));
        eachClientAndHisFollowers.putIfAbsent((String)username,new ConcurrentLinkedQueue<String>());
        eachClientAndHeFollowed.putIfAbsent((String)username,new ConcurrentLinkedQueue<String>());
        eachClientAndHisPosts.putIfAbsent((String)username,new ConcurrentLinkedQueue<String>());
        eachClientAndHisPM.putIfAbsent((String)username,new ConcurrentLinkedQueue<String>());
        eachClientAndHisBirthday.putIfAbsent(username,Birthday);
        eachClientAndHisNotifications.putIfAbsent(username,new ConcurrentLinkedQueue<>());
        eachClientAndHeBlocked.putIfAbsent(username,new ConcurrentLinkedQueue<>());
        eachClientAndBlockedfrom.putIfAbsent(username,new ConcurrentLinkedQueue<>());




    }

    public void setConnections(Connections<T> connections) {
        this.connections = connections;
    }

    public boolean isLoggedIn(String username) {
        if (username == null)
            return false;
        if(eachClientAndHisPassword.get(username)!=null)
            return eachClientAndHisPassword.get(username).getValue();
        return false;
    }

    public String getPassword(String username) {
        if(eachClientAndHisPassword.get(username)!=null)
            return eachClientAndHisPassword.get(username).getKey();
        return null;
    }

    public void login(String username, String password,int ID) {
        eachClientAndHisPassword.get(username).setValue(true);
        eachClientAndHisID.putIfAbsent(username,ID);


    }
    public void sendNotification(String username,int ID) {
  if( eachClientAndHisNotifications.get(username)!=null){
            eachClientAndHisNotifications.get(username).forEach((Notification)->{
        connections.send(eachClientAndHisID.get(username),(T)Notification);
    });
        eachClientAndHisNotifications.remove(username);
eachClientAndHisNotifications.putIfAbsent(username,new ConcurrentLinkedQueue<>());

  }
    }
    public void logout(String myName) {
        if(eachClientAndHisPassword.get(myName)!=null)
        eachClientAndHisPassword.get(myName).setValue(false);
        eachClientAndHisID.remove(myName);

    }

    public boolean Follow_me(String me,String userName) {
        synchronized (eachClientAndHisFollowers.get(me)){
            return eachClientAndHisFollowers.get(me).contains(userName);
        }
    }
    public boolean I_Follow(String me,String userName) {
        synchronized (eachClientAndHeFollowed.get(me)){
            return eachClientAndHeFollowed.get(me).contains(userName);
        }
    }

    public void Add_NewFollow(String myName, String userName){
        if(userName!=null){
            eachClientAndHisFollowers.get(userName).add(myName);
            eachClientAndHeFollowed.get(myName).add(userName);
        }

    }

    public void RemoveFollow(String myName, String userName) {
        if(userName!=null){
            eachClientAndHeFollowed.get(myName).remove(userName);
            eachClientAndHisFollowers.get(userName).remove(myName);

        }
    }

    public void PostMessage(String myName, String content) {
       String content3=filter(content);
        List<String> users=new LinkedList<>();
        String currentuser="";
        boolean start=false;
        for(int i=0;i<content.length();i=i+1)
        {

            if(start&&(content.charAt(i)==' '||content.charAt(i)=='@')) {
                start=false;
                if(!isRegistered(currentuser)) {
                    users.add(currentuser);
                    System.out.println("is "+currentuser);
                }
                currentuser="";
            }
            if(content.charAt(i)=='@'){
                start=true;
                i+=1;
            }


            if(start)
                currentuser=currentuser+content.charAt(i);
            System.out.println(currentuser);

        }
        //know users have all the users in the message



        eachClientAndHisFollowers.get(myName).forEach((follower)->{
            if(isLoggedIn(follower)){
                int Id=eachClientAndHisID.get(follower);
                connections.send(Id,(T)("9 1 "+myName+" "+content3));
            }
            else
            {
                eachClientAndHisNotifications.get(follower).add("9 1 "+myName+" "+content3);

            }

        });
        users.forEach((follower)->{

            if(!Block_me(myName,follower)){
              if(isLoggedIn(follower)){
                int Id=eachClientAndHisID.get(follower);
                  connections.send(Id,(T)("9 1 "+myName+" "+content3));
            }
            else
            {
                eachClientAndHisNotifications.get(follower).add("9 1 "+myName+" "+content3);

            }
            }
        });

        eachClientAndHisPosts.get(myName).add(content3);


    }


    public void LogState(short opcode, String username)
    {

        eachClientAndHisPassword.forEach((key,value)->{
            if(value.getValue()==true)
            {

               if(!key.equals(username)&&!Block_me(username,key)) {
                   short c=Age(key);
                   String data = "10 7 " +c+" "+ eachClientAndHisPosts.get(key).size()+
                           " " + eachClientAndHisFollowers.get(key).size() + " "
                           + eachClientAndHeFollowed.get(key).size();
                   connections.send(eachClientAndHisID.get(username), (T) data);
               }
            }
        });


    }

    public void PMMessages(short opcode, String userName, String content, String sendingDate, String myName) {

        String content2=filter(content);
        eachClientAndHisPM.get(myName).add(content+" "+sendingDate);
        if(isLoggedIn(userName))
        {
            int Id=eachClientAndHisID.get(userName);
         connections.send(Id,(T)("9 0 "+myName+" "+content2+" "+sendingDate));
        }
        else {

            eachClientAndHisNotifications.get(userName).add("9 0 "+myName+" "+content2+" "+sendingDate);
        }

    }

   public String filter(String content){
        String[] strArray=content.split(" ");
        for(int i=0;i<strArray.length;i=i+1){
            String word=strArray[i];
            if(word.charAt(word.length()-1)=='?'||word.charAt(word.length()-1)=='.'||word.charAt(word.length()-1)==','
                    ||word.charAt(word.length()-1)=='!'){
                word=word.substring(0,word.length()-1);
                if(Word_filtered.contains(word)){
                    strArray[i]="<filtered>"+word.charAt(word.length()-1);
                }

            }
            else{
                if(Word_filtered.contains(word)){
                    strArray[i]="<filtered>";
                }
            }
        }
        String content2="";
        for(int i=0;i<strArray.length;i=i+1){
            content2=content2+strArray[i]+" ";
        }
        filtered.add(content2);
return content2;
   }

    public void addWord_filter(String content)
    {
        Word_filtered.add(content);
    }
    public void print_filter(String content){
        filtered.forEach((message)->{
            System.out.println(message);
        });
    }


    public short Age(String user){
        String birthday=eachClientAndHisBirthday.get(user);
        System.out.println(birthday);
        String[] strArray=birthday.split("-");
        LocalDate birthDate = LocalDate.of(Integer.parseInt(strArray[2]), Integer.parseInt(strArray[1]), Integer.parseInt(strArray[0]));
       LocalDate myObj = LocalDate.now();
        long years = ChronoUnit.YEARS.between(birthDate, myObj);
        return (short) years;
    }

    public String State_Message(short opcode, String myName, String listOfUserNames) {
        String content=listOfUserNames;
        List<String> users=new LinkedList<>();
        String currentuser="";
        boolean start=false;
        for(int i=0;i<content.length();i=i+1)
        {

            if(start&&content.charAt(i)=='|'||i==content.length()-1) {
                if(i==content.length()-1)
                    currentuser=currentuser+content.charAt(i);
                start = false;
                    users.add(currentuser);
                    System.out.println("is " + currentuser);

                currentuser = "";
            }
            else{
                start=true;
                currentuser=currentuser+content.charAt(i);
                 System.out.println(currentuser);}

        }
        boolean send=true;
        for(String user:users){
            if(isRegistered(user)||Block_me(user,myName)) {
                send = false;
                break;
            }
        }
        if(!send){
            return"error";
        }
        users.forEach((user)->{
            short c=Age(user);
            String data = "10 7 " +c+" "+ eachClientAndHisPosts.get(user).size()+
                            " " + eachClientAndHisFollowers.get(user).size() + " "
                            + eachClientAndHeFollowed.get(user).size();
                    connections.send(eachClientAndHisID.get(myName), (T) data);


        });

        return "null";
    }

    public boolean Block_me(String me,String username){
        return eachClientAndBlockedfrom.get(me).contains(username)||eachClientAndHeBlocked.get(me).contains(username);
    }
    public void BlockMessage(String myName, String to_block) {

        if(I_Follow(myName,to_block)){
            eachClientAndHisFollowers.get(to_block).remove(myName);
            eachClientAndHeFollowed.get(myName).remove(to_block);
        }
        if(I_Follow(to_block,myName)){
            eachClientAndHisFollowers.get(myName).remove(to_block);
            eachClientAndHeFollowed.get(to_block).remove(myName);
        }

        eachClientAndHeBlocked.get(myName).add(to_block);
        eachClientAndBlockedfrom.get(to_block).add(myName);
    }

    private static class MessageBusholder {
        private static MessageBus singleton = new MessageBus();
    }

    /**
     * Retrieves the single instance of this class.
     */


    public static MessageBus getInstance() {
        return MessageBusholder.singleton;
    }


    public void Register(String UserName,String password){
        eachClientAndHisPassword.putIfAbsent(UserName,new Pair<>(password,false));
    }




    public ConcurrentHashMap<String, Pair<String, Boolean>> getEachClientAndHisPassword() {
        return eachClientAndHisPassword;
    }

















    //For implement Connections:------------------------------------------------------------------------------------



}
