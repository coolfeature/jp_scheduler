package contracts;

import data.Message;

public interface IScheduler {
	public void receive(Message msg);
	public void receive(Message[] msgs);
	public void cancelGroup(int groupId);
	public ISchedulerState getState();
}
