package src.main.java.bgu.spl.mics.example.messages;

import src.main.java.bgu.spl.mics.Event;

public class ExampleEvent implements Event<String>{

    private String senderName;

    public ExampleEvent(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }
}