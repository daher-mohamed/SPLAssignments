package test.java.bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import main.java.bgu.spl.mics.MessageBusImpl;
import main.java.bgu.spl.mics.MicroService;
import main.java.bgu.spl.mics.application.services.TimeService;
import main.java.bgu.spl.mics.example.messages.ExampleBroadcast;
import main.java.bgu.spl.mics.example.messages.ExampleEvent;
import main.java.bgu.spl.mics.Future;
import main.java.bgu.spl.mics.Message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class MessageBusImplTest {
	MessageBusImpl message;
    MicroService m;
    @BeforeEach
    public void setUp() {
    	
    	message=new MessageBusImpl();
    	m=	new TimeService();
    	
    }
	@AfterEach
    public void clean() {
    	message=null;
    }
	@Test
	public void testSubscribeEvent(){
		 try{
			 message.subscribeEvent(ExampleEvent.class, m);//for update
			 message.subscribeEvent(ExampleEvent.class, m);//to chore we dint change

         } catch (Exception e) {
             assertTrue(true);
         }
	}
	@Test
	public void testSubscribeBroadcast(){
		try {
			message.subscribeBroadcast(ExampleBroadcast.class, m);//for update
			message.subscribeBroadcast(ExampleBroadcast.class, m);//to chore we dint change
		}
		catch(Exception e) {
			assertTrue(true);
		}
	}
	@Test
	public void testComplete(){
		message.register(m);
		ExampleEvent ex=new ExampleEvent("new");
		message.subscribeBroadcast(ExampleBroadcast.class, m);//put the example event to use it
		Future<String> ef=message.sendEvent(ex);//get the event 
		message.complete(ex, "new");//cheke if it complete
		assertTrue(ef.isDone());
		
	}
	@Test
	public void testSendBroadcast() throws InterruptedException{
        MicroService micro1 = new TimeService(); //create microServices
        MicroService micro2 = new TimeService(); //create microServices
  
        message.register(micro1);  //registering the microServices
        message.register(micro2);  //registering the microServices
        
        ExampleBroadcast ex=new ExampleBroadcast("id");//make a exampleBrodcast
        
        message.subscribeBroadcast(ExampleBroadcast.class, micro1);//subscribe the microServices to the broadcast
        message.subscribeBroadcast(ExampleBroadcast.class, micro2);//subscribe the microServices to the broadcast
      
        message.sendBroadcast(ex);  //sending the broadcast
        Message msg1 = message.awaitMessage(micro1);
        Message msg2 = message.awaitMessage(micro2);
        
        assertEquals(ex, msg1);//Assert that all subscribed microServies
        assertEquals(ex, msg2);//Assert that all subscribed microServies
	}
	@Test
	public  void testRegister(){
		  try{
              message.register(m);
          } catch (Exception e){
              assertFalse(true);
          }
	}
	@Test
	public  void testAwaitMessageAndSendEvent() throws InterruptedException {
		message.register(m);
		ExampleEvent ex=new ExampleEvent("new");
		 message.subscribeEvent(ExampleEvent.class, m);
         message.sendEvent(ex);
         Message msg = message.awaitMessage(m);
         assertEquals(ex, msg);
	}
}
