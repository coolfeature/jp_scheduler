package scheduler;
import tests.Tests;
import data.Message;


public class Task implements Runnable {
	
	Message msg;
	
	public Task(Message msg) {
		this.msg = msg;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(Tests.TASK_TIME);
			msg.completed();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}			
	}
}
