package test.java.bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;


import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;


import static org.junit.jupiter.api.Assertions.*;

import main.java.bgu.spl.mics.Future;

public class FutureTest {
    private Future<String> future;
    @BeforeEach
    public void Definition() {
    	future=new Future<String>();
    }
    @Test
    public void getTest() {
    	assertFalse(future.isDone());//the false condition 
    	future.resolve("resolving");
    	assertNotEquals("is resolve", future.get()); // when the future class is done enable
    }
    @Test
    public void resolveTest() {	
    	future.resolve("resolving");
    	String TheSame="resolving";
    	assertTrue(future.isDone());
    	assertEquals(TheSame, future.get());
    }
    @Test
    public void isDoneTest() {
    	String newResolve="new Resolve";
    	assertFalse(future.isDone());
    	future.resolve(newResolve);
    	assertTrue(future.isDone());
    }
    @Test
    public void timeOutTest() {
       	String newResolve="new Resolve";
   
       	assertEquals(null, future.get(400, TimeUnit.MILLISECONDS));//before we update and put a new resolve
    	
    	future.resolve(newResolve);
    	assertEquals(newResolve, future.get(400, TimeUnit.MILLISECONDS));//after we apdate
    }
}
