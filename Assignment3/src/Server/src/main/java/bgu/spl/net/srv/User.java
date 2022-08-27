package bgu.spl.net.srv;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class User {
    private int connectionId;
    private String UserName;
    private String password;
    private AtomicBoolean connected;
    private ConcurrentHashMap<String, String> subscriptionsId = new ConcurrentHashMap<>(); // id and the topic

    public User(int CHid, String name, String password)
    {
        this.connectionId = CHid;
        this.UserName = name;
        this.password = password;
        connected.set(true);
    }

    public int getConnectionId() {
        return connectionId;
    }

    public String getUserName() {
        return UserName;
    }

    public String getPassword() {
        return password;
    }

    public boolean isConnected() {
        return connected.get();
    }

    public ConcurrentHashMap<String, String> getSubscriptionsId() {
        return subscriptionsId;
    }

}
