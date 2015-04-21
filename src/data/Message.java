package data;

import tests.Tests;
import contracts.IMessage;

public class Message implements IMessage {
	
	private int id;
	private int group;
	private boolean terminate;
	
	private Tests test;
	
	public Message(Tests test) {
		this.test = test;
	}
	
	public Message(int id, int group, Tests test) {
		this.id = id;
		this.group = group;
		this.test = test;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getGroup() {
		return group;
	}
	
	public void setGroup(int group) {
		this.group = group;
	}

	public boolean isTerminate() {
		return terminate;
	}

	public void setTerminate(boolean terminate) {
		this.terminate = terminate;
	}

	/**
	 * This method is called when the ResourceMlanager finishes executing a 
	 * Task this class was a part of. The references to Test and a call
	 * to test.printCompleted() is for testing purposes only. Normally the
	 * method would probably save the result to some internal thread safe
	 * queue.
	 */
	@Override
	public void completed() {
		System.out.println(this.toString() + " is completed");
		test.printCompleted();
	}
	
	@Override
	public String toString() {
		return "Message [id=" + id + ", group=" + group + ", terminate="
				+ terminate + "]";
	}
}
