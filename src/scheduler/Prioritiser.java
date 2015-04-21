package scheduler;

import java.util.Arrays;
import java.util.Comparator;

import contracts.IPrioritiser;
import contracts.ISchedulerState;
import data.Group;
import data.Message;

public class Prioritiser implements IPrioritiser {

	/**
	 * The method of this Prioritiser works with an array of Message.
	 * The goal is to to have the array sorted starting with the first Message 
	 * belonging to a group for which a message with the same group was 
	 * reveived first last time this method was executed.  
	 */
	@Override
	public Message[] prioritise(Message[] msgs, ISchedulerState state) {
		Group[] groups = state.getGroups();
		
		// sort grups orderReceived asceding
		Arrays.sort(groups, new Comparator<Group>() {	
			@Override
			public int compare(Group g1, Group g2) {
				return g1.getOrderReceived() > g2.getOrderReceived() ? 1 : 
					g1.getOrderReceived() == g2.getOrderReceived() ? 0 : -1;
			}
		});

		/*System.out.println("GROUPS SORTED");
		for(int i=0;i<groups.length;i++) {
			System.out.println(groups[i]);
		}*/
		
		// Fill prioritised array with prioritised elements ascending 
		Message[] prioritised = new Message[msgs.length];
		int index = 0;
		for(int i=0;i<groups.length;i++) {
			for (int m=0;m<msgs.length;m++) {
				if (msgs[m] != null) {
					if (groups[i].getGroupId() == msgs[m].getGroup()) {
						prioritised[index] = msgs[m];
						msgs[m] = null;
						index++;
					};	
				}
			}
		}

		// Fill prioritised array with remiaing - least prioritised elements.
		for(int i=0;i<prioritised.length;i++) {
			if (prioritised[i] == null) {
				for (int m=0;m<msgs.length;m++) {
					if (msgs[m] != null) {
						prioritised[i] = msgs[m];
						msgs[m] = null;
						break;
					}
				}
			}
		}
		
		/*System.out.println("MSGS SORTED");
		for(int i=0;i<prioritised.length;i++) {
			System.out.println(prioritised[i]);
		}*/
		
		return prioritised;
	}

}
