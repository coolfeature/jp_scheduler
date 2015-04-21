package contracts;

import data.Message;

public interface IPrioritiser {
	
	public abstract Message[] prioritise(Message[] msgs, ISchedulerState state);

}
