package edu.buffalo.cse.jive.finiteStateMachine.models;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.buffalo.cse.jive.finiteStateMachine.util.Pair;

public class TransitionBuilder {

	private StringBuilder transitions;
	private State rootState;
	private Map<State, Set<State>> states;
	private boolean checkValidity;

	public TransitionBuilder(State rootState, Map<State, Set<State>> states, boolean checkValidity) {
		transitions = new StringBuilder();
		transitions.append("@startuml\n");
		this.rootState = rootState;
		this.states = states;
		this.checkValidity = checkValidity;
	}

	public void addInitialState(State state, boolean result) {
		if (result)
			this.transitions.append("(*) --> " + "\"" + state.toString() + "\"");
		else
			this.transitions.append("(*) --> " + "\"" + state.toString() + "\"" + " #red");
		addNewLine();
	}

	public String getTransitions() {
		return new StringBuilder(transitions).append("@enduml\n").toString();
	}

	public void addTransition(State state1, State state2) {
		String s = "\"" + state1.toString() + "\"" + " --> " + "\"" + state2.toString() + "\"";
		this.transitions.append(s);
		addNewLine();
	}

	public void addTransition(State state1, State state2, boolean result) {
		if (!result) {
			addColorTransition(state1, state2, "red");
		} else {
			String s = "\"" + state1.toString() + "\"" + " --> " + "\"" + state2.toString() + "\"";
			this.transitions.append(s);
			addNewLine();
		}
	}

	public void addColorTransition(State state1, State state2, String color) {
		String s = "\"" + state1.toString() + "\"" + " --> " + "\"" + state2.toString() + "\"" + " #" + color;
		this.transitions.append(s);
		addNewLine();
	}

	private void addNewLine() {
		this.transitions.append("\n");
	}

	public void build() {
		if (checkValidity) {
			addInitialState(rootState, rootState.isValid());
		} else {
			addInitialState(rootState, true);
		}
		buildTransitions(null, rootState, new HashSet<Pair<State, State>>());
	}

	private void buildTransitions(State prev, State curr, Set<Pair<State, State>> visited) {
		for (State next : states.get(curr))
			if (visited.add(new Pair<State, State>(curr, next)))
				buildTransitions(curr, next, visited);
		if (checkValidity) {
			if (prev != null)
				addTransition(prev, curr, curr.isValid());
		} else {
			if (prev != null)
				addTransition(prev, curr, true);
		}
	}
}
