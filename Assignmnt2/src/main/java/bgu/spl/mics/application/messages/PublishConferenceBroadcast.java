package src.main.java.bgu.spl.mics.application.messages;

import src.main.java.bgu.spl.mics.Broadcast;
import src.main.java.bgu.spl.mics.application.objects.Model;

public class PublishConferenceBroadcast implements Broadcast {
     private Model model;

	public PublishConferenceBroadcast(Model model) {
		super();
		this.model = model;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}
     
}

