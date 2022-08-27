package src.main.java.bgu.spl.mics.application.services;
import java.util.concurrent.CountDownLatch;

import src.main.java.bgu.spl.mics.MicroService;
import src.main.java.bgu.spl.mics.application.messages.PublishConferenceBroadcast;
import src.main.java.bgu.spl.mics.application.messages.PublishResultsEvent;
import src.main.java.bgu.spl.mics.application.messages.TerminateBrodcast;
import src.main.java.bgu.spl.mics.application.messages.TickBroadcast;
import src.main.java.bgu.spl.mics.application.objects.ConfrenceInformation;
import src.main.java.bgu.spl.mics.application.objects.Model;

public class ConferenceService extends MicroService {
	 public ConfrenceInformation me;
	private  CountDownLatch count;
		
	
    public ConferenceService(String name, int date, CountDownLatch count) {
        super(name);
        this.count=count;
        me=new ConfrenceInformation(name, date);
        // TODO Implement this
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TerminateBrodcast.class, t->{
        	terminate();
            Thread.currentThread().interrupt();
        });
        
        subscribeEvent(PublishResultsEvent.class,(r)->{
        	me.publications.add(r.getModel());
            complete(r,r.getModel());

        });
        
        subscribeBroadcast(TickBroadcast.class,(t)->{
        	if(me.getDate()==t.getCurrtick().get()) {
        		for(Model model:me.publications) {
        			sendBroadcast(new PublishConferenceBroadcast(model));
        		}
                terminate();
        	}

        });
        count.countDown();
    }
}
