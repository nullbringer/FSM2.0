package edu.buffalo.cse.jive.finiteStateMachine.util;

import java.util.List;

import edu.buffalo.cse.jive.finiteStateMachine.models.State;

public class TemporaryDataTransporter {
	
	public static List<Pair<State, State>> path;
	public static boolean shouldHighlight;

	public static List<Pair<State, State>> getPath() {
		return path;
	}

	public static void setPath(List<Pair<State, State>> path) {
		TemporaryDataTransporter.path = path;
	}
	

	
	

}
