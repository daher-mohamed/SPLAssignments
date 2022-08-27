package src.main.java.bgu.spl.mics.example;

import src.main.java.bgu.spl.mics.MicroService;

public interface ServiceCreator {
    MicroService create(String name, String[] args);
}
