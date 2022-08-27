package src.main.java.bgu.spl.mics.example.messages;

import src.main.java.bgu.spl.mics.Broadcast;

public class ExampleBroadcast implements Broadcast {

    private String senderId;

    public ExampleBroadcast(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderId() {
        return senderId;
    }

}
