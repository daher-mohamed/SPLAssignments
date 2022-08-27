package src.main.java.bgu.spl.mics.application.messages;

import src.main.java.bgu.spl.mics.Event;
import src.main.java.bgu.spl.mics.application.objects.Model;

public class TrainModelEvent implements Event<Model>{
    private Model model;

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public TrainModelEvent(Model model) {
		super();
		this.model = model;
	}
    
    

}