package bgu.spl.net.srv;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageBus<T> {
    private ConcurrentHashMap<Integer, ConnectionHandler<T>> connections;

}
