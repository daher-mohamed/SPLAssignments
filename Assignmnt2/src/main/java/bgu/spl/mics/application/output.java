package src.main.java.bgu.spl.mics.application;

import src.main.java.bgu.spl.mics.application.objects.ConfrenceInformation;
import src.main.java.bgu.spl.mics.application.objects.Student;

public class output {
    private Student[] students;
    private ConfrenceInformation[] conferences;
    private int cpuTimeUsed;
    private int gpuTimeUsed;
    private int batchesProcessed;
    public output(Student[] students,ConfrenceInformation[] conferences,int cpuTimeUsed,int gpuTimeUsed,int batchesProcessed){
        this.students=students;
        this.conferences=conferences;
        this.cpuTimeUsed = cpuTimeUsed;
        this.gpuTimeUsed=gpuTimeUsed;
        this.batchesProcessed=batchesProcessed;
    }
}