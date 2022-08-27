package src.main.java.bgu.spl.mics.application.objects;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton. Add all the
 * fields described in the assignment as private fields. Add fields and methods
 * to this class as you see fit (including public methods and constructors).
 */
public class Cluster {
	private static Cluster instance = null;
	///////////////////////////////////////////////
	ConcurrentLinkedQueue<GPU> RoundRobin;// GPUS that still need to process data but they have enoght dataBatch to work
											// in.
	ConcurrentLinkedQueue<GPU> RoundRobin2;
	ConcurrentHashMap<GPU, ConcurrentLinkedQueue<DataBatch>> processedData;
	ConcurrentHashMap<GPU, ConcurrentLinkedQueue<DataBatch>> UnprocessData;
	ConcurrentHashMap<GPU, ConcurrentLinkedQueue<DataBatch>> UnprocessData2;
	ConcurrentLinkedQueue<CPU> cpus;
	ConcurrentHashMap<CPU, GPU> witch_One;
	////////////////////////////////////////////// \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

	// Implement Functions that the CPUs Are
	// using:---------------------------------------
	// send processed data from the CPU to cluster.
	public void AddCPUS(CPU cpu) {
		cpus.add(cpu);

	}

	public void AddCGPU(GPU gpu) {
		processedData.put(gpu, new ConcurrentLinkedQueue<>());

	}

	public void send_Data(DataBatch data, CPU cpu) {
		GPU g = witch_One.remove(cpu);
		processedData.get(g).add(data);

	}

	public DataBatch get_Data(CPU cpu, int j) {
		GPU g;
		synchronized (RoundRobin) {
			g = RoundRobin.poll();
			if (g != null) {
				RoundRobin.add(g);
			}
		}
		DataBatch d = null;

		if (g != null) {

			if (!UnprocessData.get(g).isEmpty()) {
				d = UnprocessData.get(g).poll();
				witch_One.put(cpu, g);
			} else {
				RoundRobin.remove(g);
				UnprocessData.remove(g);
			}
		}
		return d;
	}

	public void getDataGPU(GPU gpu, int VRAM) {
		ConcurrentLinkedQueue<DataBatch> l1 = processedData.get(gpu);
		int size = l1.size();
		for (int i = 0; i < size && i < VRAM && l1.peek() != null; i = i + 1) {
			gpu.AddData(l1.poll());
		}

	}

	public void send_forProcess(Queue<DataBatch> unprocced, GPU g, int VRAM) {
		RoundRobin.add(g);
		UnprocessData.put(g, new ConcurrentLinkedQueue<>());
		int size = unprocced.size();
		for (int i = 0; i < size; i = i + 1) {
			DataBatch p = unprocced.poll();
			if (p != null) {
				UnprocessData.get(g).add(p);
			}

		}

	}

	///////////////////////////////////////////
	private static class SingeltonHolder {
		private static Cluster instance = new Cluster();
	}

	public static Cluster getInstance() {
		return SingeltonHolder.instance;
	}

	private Cluster() {
		RoundRobin = new ConcurrentLinkedQueue<>();
		RoundRobin2 = new ConcurrentLinkedQueue<>();
		cpus = new ConcurrentLinkedQueue<>();
		witch_One = new ConcurrentHashMap<>();
		processedData = new ConcurrentHashMap<>();
		UnprocessData = new ConcurrentHashMap<>();
		UnprocessData2 = new ConcurrentHashMap<>();

	}
}
