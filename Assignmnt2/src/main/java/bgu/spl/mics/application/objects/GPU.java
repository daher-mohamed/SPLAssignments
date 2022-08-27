package src.main.java.bgu.spl.mics.application.objects;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import src.main.java.bgu.spl.mics.Event;
import src.main.java.bgu.spl.mics.application.messages.TestModelEvent;
import src.main.java.bgu.spl.mics.application.messages.TrainModelEvent;

/**
 * Passive object representing a single GPU. Add all the fields described in the
 * assignment as private fields. Add fields and methods to this class as you see
 * fit (including public methods and constructors).
 */
public class GPU {
	public static AtomicInteger GPUTIMES = new AtomicInteger(0);

	public void processTest() {
		Double d = Math.random();
		if (model.getStudent().getStatus() == "MSc") {
			if (d <= 0.6) {
				model.setResult("Good");

			} else {
				model.setResult("Bad");
			}
		} else {
			if (d <= 0.8) {
				model.setResult("Good");
			} else {
				model.setResult("Bad");
			}
		}

	}

	/**
	 * Enum representing the type of the GPU.
	 */

	enum Type {
		RTX3090, RTX2080, GTX1080
	}

	public String TypeEvents = "";
	private Type type;
	private Model model;
	private int myfast;
	private DataBatch dataBatch;
	public Queue<Event<Model>> events;
	public Queue<Event<Model>> eventsTestMOdel;
	private int VRAM;// The ram of this GPU.
	private Queue<DataBatch> procced;
	private Queue<DataBatch> unprocced;
	private Cluster cluster;
	public int sizeData = 0;
	public AtomicInteger current_tick = new AtomicInteger(1);
	AtomicInteger start_tick = new AtomicInteger(1);

/////////////////////////////////////
	public void UpdateTick() {

		if (model == null && sizeData == 0)
			process();
		isDone();
	}

	public boolean isDone() {
		if (dataBatch == null && model != null) {
			dataBatch = getData();
			start_tick = new AtomicInteger(current_tick.get());
		}
		if (current_tick.get() - start_tick.get() >= myfast && model != null) {
			sizeData -= 1000;
			GPUTIMES.addAndGet(myfast);
			dataBatch = getData();
			start_tick = new AtomicInteger(current_tick.get());
		}
		return true;
	}

	private DataBatch getData() {
		cluster.getDataGPU(this, VRAM - procced.size());
		if (procced.isEmpty())
			return null;
		DataBatch d = procced.remove();
		return d;
	}

	public void AddData(DataBatch data) {
		procced.add(data);
	}

	public void process() {
		if (model == null && !eventsTestMOdel.isEmpty()) {
			model = ((TestModelEvent) (eventsTestMOdel.peek())).getModel();
			processTest();
			TypeEvents = "TestModel";
			return;
		} else if (model == null && !events.isEmpty()) {
			TypeEvents = "TrainModel";
			model = ((TrainModelEvent) (events.peek())).getModel();
			sizeData = model.getData().getSize();
			unprocced = new LinkedList<DataBatch>();
			procced = new LinkedList<DataBatch>();
			for (int i = 0; i < model.getData().getSize(); i = 1000 + i) {
				unprocced.add(new DataBatch(model.getData(), i));
			}
			cluster.send_forProcess(unprocced, this, VRAM);

		}
	}

	/////////////////////////////////////////
	public GPU(String type) {
		cluster = Cluster.getInstance();

		switch (type) {
		case "RTX3090": {
			this.VRAM = 32;
			myfast = 1;
			this.type = Type.RTX3090;
			break;
		}
		case "RTX2080": {
			VRAM = 16;
			myfast = 2;
			this.type = Type.RTX2080;
			break;

		}
		case "GTX1080": {
			VRAM = 8;
			myfast = 4;
			this.type = Type.GTX1080;
			break;

		}
		}

		procced = new LinkedList<DataBatch>();
		unprocced = new LinkedList<DataBatch>();
		model = null;
		cluster.AddCGPU(this);
		events = new LinkedList<>();
		eventsTestMOdel = new LinkedList<>();
		dataBatch = null;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
		unprocced = new LinkedList<DataBatch>();
		if (model != null) {
			Data data = model.getData();
			for (int i = 0; i < data.getSize(); i = i + 1000) {
				unprocced.add(new DataBatch(data, i));
			}
		}
	}

	public int getVRAM() {
		return VRAM;
	}

	public void setVRAM(int vRAM) {
		VRAM = vRAM;
	}

	public Queue<DataBatch> getProcced() {
		return procced;
	}

	public void setProcced(Queue<DataBatch> procced) {
		this.procced = procced;
	}

	public Queue<DataBatch> getUnprocced() {
		return unprocced;
	}

	public void setUnprocced(Queue<DataBatch> unprocced) {
		this.unprocced = unprocced;
	}

	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

}
