package edu.buffalo.cse.jive.finiteStateMachine.models;

import java.util.Map;
import java.util.Set;

public class Context {

	private State currentState;
	private State nextState;
	private Map<State, Set<State>> states;

	public Context(State currentState, State nextState, Map<State, Set<State>> states) {
		this.currentState = currentState;
		this.nextState = nextState;
		this.states = states;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(currentState.toString());
		builder.append("\n");
		builder.append(nextState.toString());
		return builder.toString();
	}
}
