package src.main.java.bgu.spl.mics.application.objects;

import java.util.LinkedList;
import java.util.List;

/**
 * Passive object representing information on a conference. Add fields and
 * methods to this class as you see fit (including public methods and
 * constructors).
 */
public class ConfrenceInformation {
	private String name;
	private int date;
	public List<Model> publications;

	public ConfrenceInformation(String name, int date) {
		this.publications = new LinkedList<Model>();
		this.name = name;
		this.date = date;
	}

	public List<Model> getModels() {
		return publications;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}
}
