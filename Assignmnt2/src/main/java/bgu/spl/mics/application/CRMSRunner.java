package src.main.java.bgu.spl.mics.application;

import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import src.main.java.bgu.spl.mics.application.objects.CPU;
import src.main.java.bgu.spl.mics.application.objects.ConfrenceInformation;
import src.main.java.bgu.spl.mics.application.objects.GPU;
import src.main.java.bgu.spl.mics.application.objects.Student;
import src.main.java.bgu.spl.mics.application.services.CPUService;
import src.main.java.bgu.spl.mics.application.services.ConferenceService;
import src.main.java.bgu.spl.mics.application.services.GPUService;
import src.main.java.bgu.spl.mics.application.services.StudentService;
import src.main.java.bgu.spl.mics.application.services.TimeService;

/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        try {
            // create Gson instance
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get(args[0]));

            // convert JSON string to Book object
            InputFile input = gson.fromJson(reader, InputFile.class);// this function get the input that get from the
            // json and reder read the file and we get a
            // costractor for the input from input class

            // close reader
            reader.close();
            int numberThreads=input.getGpus().length+input.getCpus().length+input.getStudents().length
                    +input.getConferences().length;
            CountDownLatch count=new CountDownLatch(numberThreads);
            CountDownLatch forStudents=new CountDownLatch(input.getGpus().length+input.getCpus().length);
            // starts threads
            List<Thread> threadsList=new LinkedList<>();
            GPUService[] gpus = new GPUService[input.getGpus().length];
            for (int i = 0; i < input.getGpus().length; i++) {
                gpus[i] = new GPUService("GPU "+i,input.getGpus()[i],count,forStudents);
               threadsList.add(new Thread(gpus[i]));

            }
            CPUService[] cpus = new CPUService[input.getCpus().length];
            for (int i = 0; i < input.getCpus().length; i++) {
                cpus[i] = new CPUService("CPU" + (i + 1),input.getCpus()[i],count,forStudents);
                threadsList.add(new Thread(cpus[i]));
            }
            StudentService[] students = new StudentService[input.getStudents().length];
            for (int i = 0; i < input.getStudents().length; i++) {
                students[i] = new StudentService(input.getStudents()[i].getName());
                students[i].setStudent(input.getStudents()[i]);
                students[i].setCount(count,forStudents);
                threadsList.add(new Thread(students[i]));

            }
            ConferenceService[] confrence = new ConferenceService[input.getConferences().length];
            for (int i = 0; i < input.getConferences().length; i++) {
                confrence[i] = new ConferenceService(input.getConferences()[i].getName(),input.getConferences()[i].getDate(),count);

                threadsList.add(new Thread(confrence[i]));
            }

            TimeService time = new TimeService(input.getDuration(),input.getTick(),count);
            Thread timeThread = new Thread(time);

            timeThread.start();
            for(int i=0;i<threadsList.size();i=i+1){
               threadsList.get(i).start();
           }
            try {
                timeThread.join();
            } catch (Exception e) {
            }
            // make output
            Student[] s=new Student[students.length];
            for(int i=0;i<students.length;i=i+1){
                s[i]=students[i].getMe();
            }

             for(int i=0;i<students.length;i=i+1){
                 s[i].setstatus();
                for(int j=0;j<s[i].trainedModels.size();j=j+1){
                    s[i].trainedModels.get(j).setStudent(null);
                    s[i].trainedModels.get(j).setStudent(null);
                    s[i].trainedModels.get(j).forGson();


                }
             }
             ConfrenceInformation[] confrenceInformations=new ConfrenceInformation[input.getConferences().length];
             for(int i=0;i<confrenceInformations.length;i=i+1){
                 confrenceInformations[i]=confrence[i].me;
             }
            output out=new output(s,confrenceInformations,CPU.CPUTIMES.get(),GPU.GPUTIMES.get(),CPU.Work.get());
            try (Writer writer = new FileWriter("./output.json")) {
              gson.toJson(out, writer);
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }

        System.out.println("Main is exiting");
       // System.exit(0);
    }
}
