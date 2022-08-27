package src.main.java.bgu.spl.mics;

import java.util.concurrent.TimeUnit;

/**
 * A Future object represents a promised result - an object that will eventually
 * be resolved to hold a result of some operation. The class allows Retrieving
 * the result once it is available.
 * 
 * Only private methods may be added to this class. No public constructor is
 * allowed except for the empty constructor.
 */
public class Future<T> {

	/**
	 * This should be the the only public constructor in this class.
	 */
	private T result;
	private boolean isDone = false;

	public Future() {
		// TODO: implement this
		result = null;
	}

	/**
	 * retrieves the result the Future object holds if it has been resolved. This is
	 * a blocking method! It waits for the computation in case it has not been
	 * completed.
	 * <p>
	 * 
	 * @return return the result of type T if it is available, if not wait until it
	 *         is available.
	 * 
	 */
	public T get() {
		// TODO: implement this.
		try {
			synchronized (this) {
				while (!isDone) {
					// Maby we will wait after its rsolve.
					wait();
				}

			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();

		}
		return result;

	}

	/**
	 * Resolves the result of this Future object.
	 */
	public void resolve(T result2) {
		// TODO: implement this.
		this.result = result2;
		this.isDone = true;
		synchronized (this) {
			notifyAll();
		}
	}

	/**
	 * @return true if this object has been resolved, false otherwise
	 */
	public boolean isDone() {
		// TODO: implement this.
		return this.isDone;
	}

	public T get(long timeout, TimeUnit unit) {
		// TODO: implement this.
		if (isDone)
			return result;
		else {
			try {
				synchronized (this) {
					if (!isDone)

						wait(unit.toMillis(timeout));

				}
			} catch (InterruptedException e) {
				// TODO: handle exception
				return null;
			}
		}
		return result;
	}
}
