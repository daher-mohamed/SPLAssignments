package src.main.java.bgu.spl.mics.application.messages;

import src.main.java.bgu.spl.mics.Broadcast;

public class TerminateBrodcast implements Broadcast {
	private boolean terminate;

    public TerminateBrodcast(){
        terminate=false;
    }

    public void Terminate() {
        this.terminate = true;
    }
    
}