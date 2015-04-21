package contracts;

import data.Message;


public interface IGateway {
	
	/**
	 * Passes the messages over for processing.
	 * @param msg
	 */
	public void send(Message msg);
}
