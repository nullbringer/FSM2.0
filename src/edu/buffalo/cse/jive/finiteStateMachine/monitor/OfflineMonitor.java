package edu.buffalo.cse.jive.finiteStateMachine.monitor;

import java.util.Set;
import java.util.concurrent.BlockingQueue;

import edu.buffalo.cse.jive.finiteStateMachine.models.Event;
/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public class OfflineMonitor extends Monitor {

	public OfflineMonitor(Set<String> keyFields, BlockingQueue<Event> source, boolean shouldConsolidateByMethod) {
		super(keyFields, source, shouldConsolidateByMethod);
	}

	@Override
	public void run() {
		for (Event event : getSource()) {
			generateEventMap(event);
		}
		
		buildStates();
	}
}
