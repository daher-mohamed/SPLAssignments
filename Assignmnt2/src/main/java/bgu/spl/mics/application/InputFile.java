package src.main.java.bgu.spl.mics.application;

import src.main.java.bgu.spl.mics.application.objects.ConfrenceInformation;
import src.main.java.bgu.spl.mics.application.objects.Student;

public class InputFile {
	private Student[] Students;
	private String[] GPUS;
	private int[] CPUS;
	private ConfrenceInformation[] Conferences;
	private int TickTime;
	private int Duration;

	public InputFile(Student[] Students, String[] gpus, int[] cpus, ConfrenceInformation[] Conferences, int tick,
			int duration) {
		this.Students = Students;
		this.GPUS = gpus;
		this.CPUS = cpus;
		this.Conferences = Conferences;
		this.TickTime = tick;
		this.Duration = duration;
	}

	public ConfrenceInformation[] getConferences() {
		return Conferences;
	}

	public int[] getCpus() {
		return CPUS;
	}

	public int getDuration() {
		return Duration;
	}

	public String[] getGpus() {
		return GPUS;
	}

	public Student[] getStudents() {
		return Students;
	}

	public int getTick() {
		return TickTime;
	}
}
