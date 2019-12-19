package edu.buffalo.cse.jive.finiteStateMachine.models;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import edu.buffalo.cse.jive.finiteStateMachine.models.State.Status;
import edu.buffalo.cse.jive.finiteStateMachine.util.Pair;
import edu.buffalo.cse.jive.finiteStateMachine.util.TemporaryDataTransporter;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 * 
 *@author Amlan Gupta
 *@email amlangup@buffalo.edu
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
		else if(status.equals(Status.MARKED))
		this.transitions.append("(*) --> " + "\"" + state.toString() + "\"" + " #LimeGreen");
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
		
		Pair<State, State> pair = new Pair<State, State>(state1, state2);
		
		String s = "\"" + state1.toString() + "\"";
		if(TemporaryDataTransporter.getPath().contains(pair)) 
			s+= " -[#" + arrowColor + "]-> ";
		else s += " --> ";
		s += "\"" + state2.toString() + "\"";
		if(backgroundColor.length()>0)s+= " #" + backgroundColor;
		this.transitions.append(s);
		addNewLine();
	}


	private void addNewLine() {
		this.transitions.append("\n");
	}

	public void build() {
		addInitialState(rootState, rootState.getStatus());
//		buildTransitions(null, rootState, new HashSet<Pair<State, State>>());
		buildTransitions(rootState);
	}
	
	private void buildTransitions(State rootState) {	
		Queue<State> toBeVisited = new LinkedList<State>();
		Set<Pair<State, State>> traversedPath =  new HashSet<Pair<State, State>>();
		Set<State> visited = new HashSet<State>();
		toBeVisited.add(rootState);
		while(!toBeVisited.isEmpty()){
			State curr = toBeVisited.poll();
			for (State next : states.get(curr)){
				if (traversedPath.add(new Pair<State, State>(curr, next))) {
					if(next.getStatus().equals(Status.MARKED) && TemporaryDataTransporter.shouldHighlight)
							addColorTransitionWithArrowBetweenSameStates(curr,next, "LimeGreen","green");
					else 
						addTransition(curr, next, next.getStatus());
					if(visited.add(next))toBeVisited.add(next);
				}	
			}	
		}
	}

}
