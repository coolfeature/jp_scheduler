package tests;

import java.util.Random;

import contracts.IPrioritiser;
import contracts.IScheduler;
import data.Group;
import data.Message;
import scheduler.Prioritiser;
import scheduler.ResourceManager;
import scheduler.Scheduler;

public class Tests {
	
	// worker keep alive when idle
	public static int RES_KEEP_ALIVE = 20;
	// the min number of workers processing messages
	public static int RES_CORE_SIZE = 20;
	// the max number of workers processing messages
	public static int RES_MAX_SIZE = 100;
	// the size of internal resource queue
	public static int RES_Q_SIZE = 100;
	
	// min id of a group
	public static int MIN_GROUPS = 1;
	// max id of a group
	public static int MAX_GROUPS = 5;

	// min of messages submitted in one go to the scheduler
	public static int MIN_BURST = 1;
	// max of messages submitted in one go to the scheduler
	public static int MAX_BURST = 10;
	// set some default batch size
	public static int BATCH_SIZE = 150;
	
	// set task execution time
	public static int TASK_TIME = 5000;
	
	private int isTestCompleted = 0;
	
	ResourceManager resourceManager;
	IPrioritiser prioritiser;
	IScheduler scheduler;
	
	public Tests() {
		// Initilize Resurce Manager which is a Java executor and effectively a managed ThreadPool
		resourceManager = new ResourceManager(RES_CORE_SIZE, RES_MAX_SIZE, RES_KEEP_ALIVE, RES_Q_SIZE);
		// Instantiate a bespoke Prkioritiser
		IPrioritiser prioritiser = new Prioritiser();
		// Instantiate a scheduler which is our Gateway to the resource Manager
		scheduler = new Scheduler(resourceManager,prioritiser);
	}
	
	public void runTestSuite() {
		Tests.BATCH_SIZE = 50;
		runBatch();
		//runBatchWithTerminate();
		//runBatchWithCancel();
		this.stop();
	}
	
	public void runBatch() {
		int noOfMsgs = BATCH_SIZE;
		while (noOfMsgs>0) {
			int burstSize = getRandom(MIN_BURST,MAX_BURST);
			if (burstSize == 1) {
				int group = getRandom(MIN_GROUPS,MAX_GROUPS);
				System.out.println("Submitting: " + noOfMsgs + " " + group);
				scheduler.receive(new Message(noOfMsgs,group,this));
				noOfMsgs--;
			} else {
				if (burstSize > noOfMsgs) {
					burstSize = noOfMsgs;
				}
				Message[] burst = new Message[burstSize];
				for (int i=0;i<burst.length;i++) {
					int group = getRandom(MIN_GROUPS,MAX_GROUPS);
					burst[i] = new Message(noOfMsgs,group,this);
					System.out.println("Submitting: " + noOfMsgs + " " + group);
					noOfMsgs--;
				}
				scheduler.receive(burst);
			}
		}
	}

	public void runBatchWithTerminate() {
		int noOfMsgs = BATCH_SIZE;
		while (noOfMsgs>0) {
			int burstSize = getRandom(MIN_BURST,MAX_BURST);
			boolean terminateGroup = false;
			if (noOfMsgs > (BATCH_SIZE/2)-(burstSize*2) && noOfMsgs < (BATCH_SIZE/2)+(burstSize*2)) {
				if (terminateGroup) {
					terminateGroup = false;
				} else {
					terminateGroup = true;
				}
			}
			if (burstSize == 1) {
				int group = getRandom(MIN_GROUPS,MAX_GROUPS);
				System.out.println("Submitting: " + noOfMsgs + " " + group);
				Message msg = new Message(noOfMsgs,group,this);
				msg.setTerminate(terminateGroup);
				scheduler.receive(msg);
				noOfMsgs--;
			} else {
				if (burstSize > noOfMsgs) {
					burstSize = noOfMsgs;
				}
				Message[] burst = new Message[burstSize];
				for (int i=0;i<burst.length;i++) {
					int group = getRandom(MIN_GROUPS,MAX_GROUPS);
					burst[i] = new Message(noOfMsgs,group,this);
					burst[i].setTerminate(terminateGroup);
					System.out.println("Submitting: " + noOfMsgs + " " + group);
					noOfMsgs--;
				}
				scheduler.receive(burst);
			}
		}
	}

	public void runBatchWithCancel() {
		int noOfMsgs = BATCH_SIZE;
		boolean cancelled = false;
		while (noOfMsgs>0) {
			int burstSize = getRandom(MIN_BURST,MAX_BURST);
			if ((noOfMsgs > noOfMsgs/2) && !cancelled) {
				int group = getRandom(MIN_GROUPS,MAX_GROUPS);
				System.out.println("Cacelling group " + group);
				scheduler.cancelGroup(group);
				cancelled = true;
			}
			if (burstSize == 1) {
				int group = getRandom(MIN_GROUPS,MAX_GROUPS);
				System.out.println("Submitting: " + noOfMsgs + " " + group);
				Message msg = new Message(noOfMsgs,group,this);
				scheduler.receive(msg);
				noOfMsgs--;
			} else {
				if (burstSize > noOfMsgs) {
					burstSize = noOfMsgs;
				}
				Message[] burst = new Message[burstSize];
				for (int i=0;i<burst.length;i++) {
					int group = getRandom(MIN_GROUPS,MAX_GROUPS);
					burst[i] = new Message(noOfMsgs,group,this);
					System.out.println("Submitting: " + noOfMsgs + " " + group);
					noOfMsgs--;
				}
				scheduler.receive(burst);
			}
		}
	}
	
	public synchronized void printCompleted() {
		this.isTestCompleted++;
		if (this.isTestCompleted == BATCH_SIZE) {
			Group[] groups = scheduler.getState().getGroups();
			System.out.println("=========== Scheduler Final State: ========== ");
			for (Group g : groups) {
				System.out.println(g.toString());
			}
			System.out.println("============================================= ");
			isTestCompleted = 0;
		}
	}
	
	public void  stop() {
		resourceManager.shutdown();
	}
	
	/**
	 * Return a random int between start and end inclusive;
	 * Borrowed from StackOverflow
	 * @param start
	 * @param end
	 * @return
	 */
	
	private static int getRandom(int start, int end) {
		Random random = new Random();
		long range = (long) end - (long) start + 1;
		long fraction = (long) (range * random.nextDouble());
		int ret = (int) (fraction + start);
		return ret;
	}

}
