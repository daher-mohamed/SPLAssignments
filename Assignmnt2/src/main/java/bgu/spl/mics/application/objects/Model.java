package src.main.java.bgu.spl.mics.application.objects;

/**
* Passive object representing a Deep Learning model.
* Add all the fields described in the assignment as private fields.
* Add fields and methods to this class as you see fit (including public methods and constructors).
*/
public class Model {
	enum status {PreTrainerd, Training, Trained,Tested}
	
	enum results {None,Good,Bad}
	
private String name;
private Data data;
private Student student;
private status s_status;
private results s_result;
private String Status;
private String result;



public Model(String name, Data data, Student student,String status1, String result) {
	super();
	this.name = name;
	this.data = data;
	this.student = student;
	Status=status1;
	switch (status1)
	{   case "PreTrained":{
		this.s_status=status.PreTrainerd ;
	break;
	}
		case "Trained":{
			this.s_status=status.Trained;
			break;
		}
		case "Training":{
			this.s_status=status.Training;
			break;
		}

		case "Tested": {
			this.s_status = status.Tested;
			break;
		}
	}
	result=result;
	switch (result){
		case "None":
			this.s_result=results.None;
		case "Bad":
			this.s_result=results.Bad;

		case "Good":
			this.s_result=results.Good;
	}
}
public results getr(){
	return s_result;
}
public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public Data getData() {
	return data;
}

public void setData(Data data) {
	this.data = data;
}

public Student getStudent() {
	return student;
}

public void setStudent(Student student) {
	this.student = student;
}

public status getStatus1() {
	return s_status;
}

public void setStatus1(String status1) {
	switch (status1){
		case "Trained":
			this.s_status=status.Trained;
		case "Training":
			this.s_status=status.Training;

		case "Tested":
			this.s_status=status.Tested;

	}
	Status=status1;
}

public String getResult() {
	return result;
}

public void setResult(String result) {
	this.result=result;
	switch (result){
		case "None":{
			this.s_result=results.None;
			break;
		}
		case "Bad": {
			this.s_result =results.Bad;

			break;
		}
		case "Good": {
			this.s_result = results.Good;
			break;
		}
	}
}
public void forGson(){

	s_result=null;
	s_status=null;
	data.name=null;
}

}
