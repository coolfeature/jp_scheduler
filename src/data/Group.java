package data;


public class Group {
	
	int groupId;
	int msgsSoFar;
	int orderReceived;
	boolean terminated;
	boolean cancelled;
	
	public Group(int groupId) {
		this.groupId = groupId;
		this.msgsSoFar = 1;
		this.orderReceived = 0;
		this.terminated = false;
		this.cancelled = false;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getMsgsSoFar() {
		return msgsSoFar;
	}

	public void setMsgsSoFar(int msgsSoFar) {
		this.msgsSoFar = msgsSoFar;
	}

	public boolean isTerminated() {
		return terminated;
	}

	public void setTerminated(boolean terminated) {
		this.terminated = terminated;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public int getOrderReceived() {
		return orderReceived;
	}

	public void setOrderReceived(int orderReceived) {
		this.orderReceived = orderReceived;
	}

	@Override
	public String toString() {
		return "Group [groupId=" + groupId + ", msgsSoFar=" + msgsSoFar
				+ ", orderReceived=" + orderReceived + ", terminated="
				+ terminated + ", cancelled=" + cancelled + "]";
	}

}
