package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.ConnectionHandler;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnnectionImplement<T> implements Connections<T>{
   MessageBus<T> messageBus=MessageBus.getInstance();
    AtomicInteger countID=new AtomicInteger(0);
    public  ConnnectionImplement(){
    }

    private ConcurrentHashMap<Integer, ConnectionHandler<T>> connections=new ConcurrentHashMap<>();
    public int addConnections(ConnectionHandler<T> connections){
       synchronized (this){
        this.connections.put(countID.get(),connections);
        int id=countID.get();
        //Check if this nescceary.

            countID.incrementAndGet();
            System.out.println("I add this to connections");
            return id;
        }

    }


    public boolean send(int connectionId, T msg) {
        if(this.connections.get(connectionId)!=null){
            synchronized (connections.get(connectionId))//Because not to client write in the same time
            {

            this.connections.get(connectionId).send(msg);

            }
            return true;
        }
        return false;
    }


    public void broadcast(T msg) {
        this.connections.forEach((key,value)->{
            synchronized (value){
            value.send(msg);
            }
        });
    }


    public void disconnect(int connectionId) {
        this.connections.remove(connectionId);

    }


}
