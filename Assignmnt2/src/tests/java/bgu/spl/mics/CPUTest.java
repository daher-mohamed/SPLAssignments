package test.java.bgu.spl.mics;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.java.bgu.spl.mics.Callback;
import main.java.bgu.spl.mics.Event;
import main.java.bgu.spl.mics.application.services.CPUService;



public class CPUTest {

	private CPUService cpu;
	private Callback callback;
	private Event event;
	
	@BeforeEach
	public void SetUp() {
//		cpu=new CPUService();
	}
	
	@Test
	public void checkTime(){
		
		int BeforeTime =  (int) (new Date().getTime() /1000);//the time was before
    	cpu.updateTime();//i updat the time
    	int AfterTime =  (int) (new Date().getTime() /1000);//the time was after
    	
    	boolean before = BeforeTime < cpu.time() ;//the before time is smaller then the time that we update
    	boolean after  = cpu.time() < AfterTime;//the before time is smaller then the time that we update
    	
    	assertTrue(before);
    	assertTrue(after);
	}
}
