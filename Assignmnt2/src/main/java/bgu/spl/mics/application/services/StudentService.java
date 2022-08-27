package src.main.java.bgu.spl.mics.application.services;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import src.main.java.bgu.spl.mics.Future;
import src.main.java.bgu.spl.mics.MicroService;
import src.main.java.bgu.spl.mics.application.messages.PublishConferenceBroadcast;
import src.main.java.bgu.spl.mics.application.messages.PublishResultsEvent;
import src.main.java.bgu.spl.mics.application.messages.TerminateBrodcast;
import src.main.java.bgu.spl.mics.application.messages.TestModelEvent;
import src.main.java.bgu.spl.mics.application.messages.TrainModelEvent;
import src.main.java.bgu.spl.mics.application.objects.Data;
import src.main.java.bgu.spl.mics.application.objects.Model;
import src.main.java.bgu.spl.mics.application.objects.Student;

/**
 * Student is responsible for sending the {@link TrainModelEvent},
 * {@link TestModelEvent} and {@link PublishResultsEvent}. In addition, it must
 * sign up for the conference publication broadcasts. This class may not hold
 * references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class. You MAY change
 * constructor signatures and even add new public constructors.
 */
public class StudentService extends MicroService {
	private Student me;
	CountDownLatch count;
	CountDownLatch countfor;

	public void setCount(CountDownLatch count, CountDownLatch count2) {
		this.count = count;
		this.countfor = count2;
	}

	public StudentService(String name) {
		super(name);
		// TODO Implement this
	}

	@Override
	protected void initialize() {
		try {
			countfor.await();
		} catch (Exception e) {

		}
		subscribeBroadcast(PublishConferenceBroadcast.class, (p) -> {
         
			if (!p.getModel().getStudent().getName().equals(me.getName())) {
				me.setPapersRead(me.getPapersRead() + 1);
			} else {
				me.setPublications(me.getPublications() + 1);
			}

		});
///---------SEND THE MODELS-///////////////////////////////////////////////////////
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				for (Data data : me.models) {
					Model m = new Model(data.name, data, me, "PreTrained", "None");
					m.setResult("None");
					Future<Model> f = sendEvent(new TrainModelEvent(m));
					if (f != null)// Check if there a Micro that interesting in this Event
					{
						Model result = f.get();
						if (result != null) // The Event has been finished.

						{
							me.trainedModels.add(m);

							Future<Model> f1 = sendEvent(new TestModelEvent(result));
							Model result2 = f1.get();
							if (result2 != null && result2.getResult() == "Good") // The result of the Trainer is Good
							{
								Future<Model> f2 = sendEvent(new PublishResultsEvent(result2));
							}
						}
					}
				}

			}
		});
		subscribeBroadcast(TerminateBrodcast.class,t -> {
			terminate();
			t1.interrupt();
		});
		t1.start();
		count.countDown();
	}

	public void setStudent(Student student) {
		me = new Student(student.getName(), student.getDepartment(), student.getStatus(), student.models);
	}

	public Student getMe() {
		return me;
	}
}
