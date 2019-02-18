package edu.buffalo.cse.jive.finiteStateMachine.monitor;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import edu.buffalo.cse.jive.finiteStateMachine.models.Event;

public class OfflineMonitor extends Monitor {

	public OfflineMonitor(List<String> keyFields, BlockingQueue<Event> source) {
		super(keyFields, source);
	}

	@Override
	public void run() {
		for (Event event : getSource()) {
			buildStates(event);
		}
	}
}
