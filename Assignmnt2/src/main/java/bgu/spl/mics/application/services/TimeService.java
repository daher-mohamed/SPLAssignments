package src.main.java.bgu.spl.mics.application.services;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import src.main.java.bgu.spl.mics.MicroService;
import src.main.java.bgu.spl.mics.application.messages.TerminateBrodcast;
import src.main.java.bgu.spl.mics.application.messages.TickBroadcast;

/**
 * TimeService is the global system timer There is only one instance of this
 * micro-service. It keeps track of the amount of ticks passed since
 * initialization and notifies all other micro-services about the current time
 * tick using {@link TickBroadcast}. This class may not hold references for
 * objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class. You MAY change
 * constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService {
	private AtomicInteger time;
	private AtomicInteger ticks;
	private CountDownLatch latch;
	int speed;

	public TimeService(int duration, int speed, CountDownLatch latch) {
		super("TimeService");
		this.speed = speed;
		this.latch = latch;
		time = new AtomicInteger(duration);
		ticks = new AtomicInteger(1);
	}

	protected void initialize() {
		try {
			latch.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (ticks.get() <= time.get()) {
			TickBroadcast broadcast = new TickBroadcast(ticks, time.get());
			sendBroadcast(broadcast);
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ticks.incrementAndGet();
		}
		sendBroadcast(new TerminateBrodcast());
		terminate();
	}
}
