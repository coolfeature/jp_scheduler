package scheduler;

import java.util.concurrent.ConcurrentHashMap;

import contracts.ISchedulerState;
import data.Group;

/**
 * This is used to keep track of how many messages were processed and in what 
 * order the last batch of messages arrived. This class essentailly keeps a
 * state that other threads may query via sychnronised methods. 
 * @author sczaja
 *
 */
public class SchedulerState implements ISchedulerState {
	
	 ConcurrentHashMap<Integer,Group> groups;
	
	public SchedulerState() {
		groups = new ConcurrentHashMap<Integer,Group>();
	}

	@Override
	public void add(int groupId) {
		Group g = groups.get(groupId);
		if (g==null) {
			groups.put(groupId, new Group(groupId));
		} else {
			g.setMsgsSoFar(g.getMsgsSoFar()+1);
			groups.put(groupId,g);
		}
	}
	
	@Override
	public void cancel(int groupId) {
		Group g = groups.get(groupId);
		if (g==null) {
			g = new Group(groupId);
			g.setCancelled(true);
			groups.put(groupId,g);
		} else {
			g.setCancelled(true);
			groups.put(groupId,g);
		}
	}

	@Override
	public boolean isCancelled(int groupId) {
		Group g = groups.get(groupId);
		if (g==null) {
			return false;
		} else {
			return g.isCancelled();
		}
	}

	@Override
	public void terminate(int groupId) {
		Group g = groups.get(groupId);
		if (g==null) {
			g = new Group(groupId);
			g.setTerminated(true);
			groups.put(groupId,g);
		} else {
			g.setTerminated(true);
			groups.put(groupId,g);
		}
	}

	@Override
	public boolean isTerminated(int groupId) {
		Group g = groups.get(groupId);
		if (g==null) {
			return false;
		} else {
			return g.isTerminated();
		}
	}


	@Override
	public Group getGroup(int groupId) {
		return groups.get(groupId); 
	}
	
	@Override
	public Group[] getGroups() {
		return groups.values().toArray(new Group[groups.values().size()]);
	}

	@Override
	public String toString() {
		return "State [" + groups + "]";
	}
}
