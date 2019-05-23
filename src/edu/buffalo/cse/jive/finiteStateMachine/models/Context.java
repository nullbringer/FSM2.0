package edu.buffalo.cse.jive.finiteStateMachine.models;

import java.util.Map;
import java.util.Set;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
/**
 * Context of the state. Pass in current state, next state and the adjacency
 * list
 *
 */
public class Context {

	private State currentState;
	private State nextState;
	private Map<State, Set<State>> states;
	private boolean useMarker;

	public Context(State currentState, State nextState, Map<State, Set<State>> states) {
		this.currentState = currentState;
		this.nextState = nextState;
		this.states = states;
		this.useMarker = false;
	}

	public Context(State currentState, State nextState, Map<State, Set<State>> states, boolean useMarker) {
		super();
		this.currentState = currentState;
		this.nextState = nextState;
		this.states = states;
		this.useMarker = useMarker;
	}



	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public State getNextState() {
		return nextState;
	}

	public void setNextState(State nextState) {
		this.nextState = nextState;
	}

	public Map<State, Set<State>> getStates() {
		return states;
	}

	public void setStates(Map<State, Set<State>> states) {
		this.states = states;
	}
	

	public boolean isUseMarker() {
		return useMarker;
	}

	public void setUseMarker(boolean useMarker) {
		this.useMarker = useMarker;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(currentState.toString());
		builder.append("\n");
		builder.append(nextState.toString());
		return builder.toString();
	}
}
