package edu.buffalo.cse.jive.finiteStateMachine.monitor;

import java.util.Set;
import java.util.concurrent.BlockingQueue;

import edu.buffalo.cse.jive.finiteStateMachine.models.Event;

public class OfflineMonitor extends Monitor {

	public OfflineMonitor(Set<String> keyFields, BlockingQueue<Event> source) {
		super(keyFields, source);
	}

	@Override
	public void run() {
		for (Event event : getSource()) {
			buildStates(event);
		}
	}
}
