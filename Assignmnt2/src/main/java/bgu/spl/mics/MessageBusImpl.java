package src.main.java.bgu.spl.mics;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus
 * interface. Write your implementation here! Only private fields and methods
 * can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	private static class SingeltonHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>> Microsevice_type;
	private ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> Micro_queues;
	private ConcurrentHashMap<Event<?>, Future> FutureMap;

	private MessageBusImpl() {
		Microsevice_type = new ConcurrentHashMap<>();
		Micro_queues = new ConcurrentHashMap<>();
		FutureMap = new ConcurrentHashMap<>();
	}

	public static MessageBusImpl getInstance() {
		return SingeltonHolder.instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		// TODO Auto-generated method stub
		Microsevice_type.putIfAbsent(type, new ConcurrentLinkedQueue<MicroService>());
		Microsevice_type.get(type).add(m);

	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		// TODO Auto-generated method stub
		Microsevice_type.putIfAbsent(type, new ConcurrentLinkedQueue<MicroService>());
		Microsevice_type.get(type).add(m);

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub
		FutureMap.get(e).resolve(result);
		FutureMap.remove(e);// Finish From Event e.

	}

	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub
		ConcurrentLinkedQueue<MicroService> q = Microsevice_type.get(b.getClass());
		if (q == null) {
			return;
		}
		q.forEach(MicroService -> {
			LinkedBlockingQueue<Message> q2 = Micro_queues.get(MicroService);
			if (q2 != null)
				q2.add(b);
		});
	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// TODO Auto-generated method stub
		Future<T> future = new Future<T>();
		FutureMap.put(e, future);
		ConcurrentLinkedQueue<MicroService> Eventqueue = Microsevice_type.get(e.getClass());
		if (Eventqueue == null) {
			return null;
		}
		if (Eventqueue.isEmpty()) {
			return null;
		}
		MicroService micro;
		synchronized (e.getClass()) // this is for save the round robin.
		{
			micro = Eventqueue.poll();
			Eventqueue.add(micro);
		}

		synchronized (micro)// Here because we won't The MicroService that send the Event Wait for a long
							// time.
		{
			LinkedBlockingQueue<Message> message = Micro_queues.get(micro);
			message.add(e);
		}
		return future;

	}

	@Override
	public void register(MicroService m) {
		// TODO Auto-generated method stub
		Micro_queues.put(m, new LinkedBlockingQueue<Message>());
	}

	@Override
	public void unregister(MicroService m) {
		Microsevice_type.forEach((key, value) -> {
			synchronized (key)// Because maby he not exist Not sureabout it.
			{
				value.remove(m);
			}
		});
		LinkedBlockingQueue<Message> q;
		synchronized (m) {
			q = Micro_queues.remove(m);
		}
		while (!q.isEmpty()) {
			Message message = q.poll();
			Future<?> future = FutureMap.get(message);
			if (future != null)
				future.resolve(null);

		}
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		// TODO Auto-generated method stub
		return Micro_queues.get(m).take();// there is wait() in the Implement the LinkedBlockingQueus using take()
											// because it wait until there is available message.
	}
}