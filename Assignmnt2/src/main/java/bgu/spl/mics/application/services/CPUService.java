package src.main.java.bgu.spl.mics.application.services;
import java.util.concurrent.CountDownLatch;

import src.main.java.bgu.spl.mics.MicroService;
import src.main.java.bgu.spl.mics.application.messages.TerminateBrodcast;
import src.main.java.bgu.spl.mics.application.messages.TickBroadcast;
import src.main.java.bgu.spl.mics.application.objects.CPU;

/**
 * CPU service is responsible for handling the {@link }.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class CPUService extends MicroService {
	private CPU me;
    CountDownLatch count;
    CountDownLatch fCoun;
    public CPUService(String name,int core,CountDownLatch count,CountDownLatch forstudent) {
        super(name);
        fCoun=forstudent;
       this.count=count;
      me=new CPU(core);
    }

    @Override
    protected void initialize() {
       subscribeBroadcast(TerminateBrodcast.class,(tm)->{
    	   terminate();
           Thread.currentThread().interrupt();
       });
       
       subscribeBroadcast(TickBroadcast.class,(t)->{
    	   me.current_tick.set(t.getCurrtick().get());
    	   me.Update_tick();
       });
       fCoun.countDown();
        count.countDown();
    }
}
