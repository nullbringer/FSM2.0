package edu.buffalo.cse.jive.finiteStateMachine.util;

import java.util.List;

import edu.buffalo.cse.jive.finiteStateMachine.models.State;

public class ShortestPathHolderForEExpression {
	
	public static List<Pair<State, State>> path;

	public static List<Pair<State, State>> getPath() {
		return path;
	}

	public static void setPath(List<Pair<State, State>> path) {
		ShortestPathHolderForEExpression.path = path;
	}
	

	
	

}
