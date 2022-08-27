package src.main.java.bgu.spl.mics.application.objects;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing a single CPU. Add all the fields described in the
 * assignment as private fields. Add fields and methods to this class as you see
 * fit (including public methods and constructors).
 */
public class CPU {
	public static AtomicInteger CPUTIMES = new AtomicInteger(0);
	public static AtomicInteger Work = new AtomicInteger(0);
	int cores;
	public AtomicInteger current_tick = new AtomicInteger(1);
	AtomicInteger start_tick = new AtomicInteger(0);
	private Cluster cluster;
	DataBatch data;

	public CPU(int cores) {
		this.cores = cores;
		this.cluster = Cluster.getInstance();
		data = null;
		cluster.AddCPUS(this);

	}

	public void Update_tick() {
		isDone();

	}

	public boolean isDone() {
		if (data == null) {
			data = cluster.get_Data(this, 0);
			start_tick = new AtomicInteger(current_tick.get());
			return true;
		}
		if (data.getData().getType() == "Images") {
			if (current_tick.get() - start_tick.get() >= (32 / cores) * 4) {
				CPUTIMES.addAndGet((32 / cores) * 4);
				Work.addAndGet(1);
				cluster.send_Data(data, this);
				data = cluster.get_Data(this, 0);
				start_tick = new AtomicInteger(current_tick.get());
			}
		} else if (data.getData().getType() == "Text") {
			if (current_tick.get() - start_tick.get() >= (32 / cores) * 2) {
				CPUTIMES.addAndGet((32 / cores) * 2);

				Work.addAndGet(1);
				cluster.send_Data(data, this);
				data = cluster.get_Data(this, 0);
				start_tick = new AtomicInteger(current_tick.get());
			}
		} else {
			if (current_tick.get() - start_tick.get() >= (32 / cores) * 1) {
				CPUTIMES.addAndGet((32 / cores) * 1);
				Work.addAndGet(1);
				cluster.send_Data(data, this);
				data = cluster.get_Data(this, 0);
				start_tick = new AtomicInteger(current_tick.get());
			}
		}
		return true;
	}
}
