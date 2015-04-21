package scheduler;
import java.util.concurrent.RejectedExecutionException;

import contracts.IPrioritiser;
import contracts.IGateway;
import contracts.IScheduler;
import contracts.ISchedulerState;
import data.Group;
import data.Message;

/**
 * This is the main class that receives data messages and feeds ResourceManager
 * with them via a Gateway. The ResurceManager internally uses a BlockingQueue.
 * 
 * @author sczaja
 *
 */
public class Scheduler implements IGateway, IScheduler {
	
	ResourceManager resourceManager;
	IPrioritiser prioritiser;
	Message[] buffer;
	
	ISchedulerState state = new SchedulerState();
	
	/**
	 * 
	 * This class extracts data from a database and uses it to create Runnable
	 * tasks that are submitted to the Executor.
	 * 
	 * @param executor
	 */
	public Scheduler(ResourceManager executor, IPrioritiser prioritiser) {
		this.resourceManager = executor;
		this.prioritiser = prioritiser;
	}

	public Scheduler(ResourceManager executor) {
		this.resourceManager = executor;
		this.prioritiser = new Prioritiser();
	}
	/**
	 * The execute() method of the resource manager will place the message 
	 * on the internal queue therefore submitting it for execution.
	 * As messages are completed, if there are queued messages, they will be processed **
	 */
	@Override
	public void send(Message msg) {
		try {
			if (!state.isCancelled(msg.getGroup())) {
				Task task = new Task(msg);
				resourceManager.execute(task);
				state.add(msg.getGroup());
			} else {
				System.out.println(msg.getGroup() + " is cancelled.");
			}	
		} catch (RejectedExecutionException ree) {
			try {
				//When no resources are available, messages should not be sent to the Gateway, retry after 1s
				Thread.sleep(1000);
				send(msg);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void receive(Message msg) {
		if (!msg.isTerminate()) {
			send(msg);	
		} else {
			System.err.println("Group " + msg.getGroup() + " is terminated.");
			state.terminate(msg.getGroup());
		}
	}

	@Override
	public void receive(Message[] msgs) {
		Message[] prioritised = prioritiser.prioritise(msgs, state);
		for (int i=0;i<prioritised.length;i++) {
			Group g = state.getGroup(prioritised[i].getGroup());
			if (g!=null) {
				g.setOrderReceived(i+1);
			}
			receive(prioritised[i]);
		}
	}

	@Override
	public void cancelGroup(int groupId) {
		state.cancel(groupId);
	}

	@Override
	public ISchedulerState getState() {
		return this.state;
	}
	
	
}
