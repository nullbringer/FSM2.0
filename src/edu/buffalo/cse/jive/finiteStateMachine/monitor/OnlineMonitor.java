package edu.buffalo.cse.jive.finiteStateMachine.monitor;

import java.util.Set;
import java.util.concurrent.BlockingQueue;

import edu.buffalo.cse.jive.finiteStateMachine.models.Event;

public class OnlineMonitor extends Monitor {

	public OnlineMonitor(Set<String> keyFields, BlockingQueue<Event> source) {
		super(keyFields, source);
	}

	@Override
	public void run() {
		while (true) {
			try {
				buildStates(getSource().take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
