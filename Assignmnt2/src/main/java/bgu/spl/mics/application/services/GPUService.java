package src.main.java.bgu.spl.mics.application.services;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import src.main.java.bgu.spl.mics.MicroService;
import src.main.java.bgu.spl.mics.application.messages.TerminateBrodcast;
import src.main.java.bgu.spl.mics.application.messages.TestModelEvent;
import src.main.java.bgu.spl.mics.application.messages.TickBroadcast;
import src.main.java.bgu.spl.mics.application.messages.TrainModelEvent;
import src.main.java.bgu.spl.mics.application.objects.GPU;

public class GPUService extends MicroService {
    private GPU me;
	private CountDownLatch count;
	CountDownLatch fors;
    public GPUService(String name, String type, CountDownLatch lach,CountDownLatch forStudent) {
        super(name);
		count=lach;
		fors=forStudent;
      me=new GPU(type);
    }
    @Override
    protected void initialize() {
		// TODO Implement this
		subscribeBroadcast(TerminateBrodcast.class, (t) ->{
			terminate();
		});//Stop the Threads
		subscribeBroadcast(TickBroadcast.class, time-> {
			me.current_tick = new AtomicInteger(time.getCurrtick().get());
			me.UpdateTick();
			if(me.sizeData==0&&me.getModel()!=null){
				if (me.TypeEvents.equals("TrainModel")){
				me.getModel().setStatus1("Trained");
				complete(me.events.poll(), me.getModel());
				me.setModel(null);
				me.process();
			} else {
				me.getModel().setStatus1("Tested");
				complete(me.eventsTestMOdel.poll(), me.getModel());
				me.setModel(null);
				me.process();
				}
			} else if(me.getModel()!=null){
		}
			else;
		});

		subscribeEvent(TestModelEvent.class, e -> {
	     me.eventsTestMOdel.add(e);

		});
		subscribeEvent(TrainModelEvent.class, e -> {
			me.events.add(e);
		});
     fors.countDown();
	 count.countDown();
	}
}


