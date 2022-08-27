package src.main.java.bgu.spl.mics.application.messages;

import java.util.concurrent.atomic.AtomicInteger;

import src.main.java.bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {
	AtomicInteger currtick;
	 private int time;

	    public TickBroadcast(AtomicInteger currtick , int time) {
	        this.time = time;
	        this.currtick = currtick;
	    }

	    public int getTime() {
	        return time;
	    }

	    public AtomicInteger getCurrtick() {
	        return currtick;
	    }
}
