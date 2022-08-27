package test.java.bgu.spl.mics;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import main.java.bgu.spl.mics.Callback;
import main.java.bgu.spl.mics.Event;
import main.java.bgu.spl.mics.application.objects.Cluster;
import main.java.bgu.spl.mics.application.objects.Data;
import main.java.bgu.spl.mics.application.objects.DataBatch;
import main.java.bgu.spl.mics.application.services.GPUService;

public class GPUTest {
	private GPUService gpu;
	private Callback callback;
	private Event event;
	@BeforeEach
	 public void setUp() {}
	@Test
	public void processetest(){
		Cluster clust=Cluster.getInstance();
		int beforesize=600000;//the number that i do for the test
		Data data=new Data(beforesize);
		gpu.processe(data);
		ArrayList<DataBatch> array=clust.getDataBatch();
		int size=array.size();//what the size 
		assertEquals((beforesize/1000), size);
		
	}
	@Test
	public void Traintest() {
		Cluster clust=Cluster.getInstance();//the type of gpu is 1080
		ArrayList<DataBatch> array=clust.getDataBatch();//return the array
		gpu.train(array);
		int time =gpu.time();//the time was return after inter the types
		assertEquals(time, 4);
	}
	
}
