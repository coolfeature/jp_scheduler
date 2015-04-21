package contracts;

import data.Group;

public interface ISchedulerState {
	
	public Group[] getGroups();
	public Group getGroup(int groupId);
	public void add(int groupId);
	public void cancel(int groupId);
	public boolean isCancelled(int groupId);
	public void terminate(int groupId);
	public boolean isTerminated(int groupId);

}
