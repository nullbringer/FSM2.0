package edu.buffalo.cse.jive.finiteStateMachine.models;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.buffalo.cse.jive.finiteStateMachine.models.State.Status;
import edu.buffalo.cse.jive.finiteStateMachine.util.Pair;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
/**
 * Given the adjacency list and the root state, builds the String required for
 * SVG generator.
 *
 */
public class TransitionBuilder {

	private StringBuilder transitions;
	private State rootState;
	private Map<State, Set<State>> states;

	public TransitionBuilder(State rootState, Map<State, Set<State>> states) {
		transitions = new StringBuilder();
		transitions.append("@startuml\n");
		this.rootState = rootState;
		this.states = states;
	}

	private void addInitialState(State state, Status status) {
		if (status.equals(Status.VALID))
			this.transitions.append("(*) --> " + "\"" + state.toString() + "\"");
		else
			this.transitions.append("(*) --> " + "\"" + state.toString() + "\"" + " #red");
		addNewLine();
	}

	public String getTransitions() {
		return new StringBuilder(transitions).append("@enduml\n").toString();
	}

	private void addTransition(State state1, State state2, Status status) {
		if (status.equals(Status.INVALID)) {
			addColorTransition(state1, state2, "red");
		} else {
			addNoColorTransition(state1, state2);
		}
	}

	private void addColorTransition(State state1, State state2, String color) {
		String s = "\"" + state1.toString() + "\"" + " --> " + "\"" + state2.toString() + "\"" + " #" + color;
		this.transitions.append(s);
		addNewLine();
	}
	
	private void addNoColorTransition(State state1, State state2) {
		String s = "\"" + state1.toString() + "\"" + " --> " + "\"" + state2.toString() + "\"";
		this.transitions.append(s);
		addNewLine();
	}
	
	private void addColorTransitionWithArrowBetweenSameStates(State state1, State state2, String backgroundColor, String arrowColor) {
		String s = "\"" + state1.toString() + "\"";
		if(state1.getStatus().equals(state2.getStatus()) && !state2.equals(rootState)) s+= " -[#" + arrowColor + "]-> ";
		else s += " --> ";
		s += "\"" + state2.toString() + "\"" + " #" + backgroundColor;
		this.transitions.append(s);
		addNewLine();
	}

	private void addNewLine() {
		this.transitions.append("\n");
	}

	public void build() {
		addInitialState(rootState, rootState.getStatus());
		buildTransitions(null, rootState, new HashSet<Pair<State, State>>());
	}

	private void buildTransitions(State prev, State curr, Set<Pair<State, State>> visited) {
		for (State next : states.get(curr))
			if (visited.add(new Pair<State, State>(curr, next)))
				buildTransitions(curr, next, visited);
		if (prev != null) {
			if(curr.getStatus().equals(Status.MARKED))addColorTransitionWithArrowBetweenSameStates(prev, curr, "LimeGreen","green");
			else addTransition(prev, curr, curr.getStatus());
		}
	}

}
