package src.main.java.bgu.spl.mics.application.objects;

import java.util.LinkedList;
import java.util.List;

/**
 * Passive object representing single student. Add fields and methods to this
 * class as you see fit (including public methods and constructors).
 */
public class Student {
	/**
	 * Enum representing the Degree the student is studying for.
	 */
	enum Degree {
		MSc, PhD
	}

	private String name;
	private String department;
	private String status;
	private Degree s_status;
	public Data[] models;
	private int publications;
	private int papersRead;
	public List<Model> trainedModels;// IS Work

	public Student(String name, String department, String status, Data[] models) {
		trainedModels = new LinkedList<>();
		this.status = status;
		this.name = name;
		this.department = department;
		this.publications = 0;
		this.papersRead = 0;
		switch (status) {
		case "MSc":
			this.s_status = Degree.MSc;
		case "PhD":
			this.s_status = Degree.PhD;
		}
		this.models = models;
	}

	public void setstatus() {
		s_status = null;
		models = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		switch (status) {
		case "MSc":
			this.s_status = Degree.MSc;
		case "PhD":
			this.s_status = Degree.PhD;
		}
	}

	public int getPublications() {
		return publications;
	}

	public void setPublications(int publications) {
		this.publications = publications;
	}

	public int getPapersRead() {
		return papersRead;
	}

	public void setPapersRead(int papersRead) {
		this.papersRead = papersRead;
	}
}
