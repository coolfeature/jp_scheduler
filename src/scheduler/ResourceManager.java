package scheduler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ResourceManager extends ThreadPoolExecutor {

	private boolean isPaused;
	private ReentrantLock pauseLock = new ReentrantLock();
	private Condition unpaused = pauseLock.newCondition();

	/**
	 * 
	 * This is an implementation of a ThreadPoolExecutor.
	 * 
	 * @param coreSize
	 * @param maxSize
	 * @param keepAlive
	 * @param qSize
	 */
	public ResourceManager(int coreSize, int maxSize, int keepAlive, int qSize) {
		// The last argument is the internal queue used by the thread pool.
		super(coreSize, maxSize, keepAlive, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(qSize));
		allowCoreThreadTimeOut(true);
		prestartAllCoreThreads();
	}
	
	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);
		pauseLock.lock();
		try {
			while (isPaused) {
				unpaused.await();
			}
		} catch (InterruptedException ie) {
			t.interrupt();
		} finally {
			pauseLock.unlock();
		}
	}

	public void pause() {
		pauseLock.lock();
		try {
			isPaused = true;
		} finally {
			pauseLock.unlock();
		}
	}

	public void resume() {
		pauseLock.lock();
		try {
			isPaused = false;
			unpaused.signalAll();
		} finally {
			pauseLock.unlock();
		}
	}

	@Override
	public void shutdown() {
		super.shutdown();
	}
	
	
}
