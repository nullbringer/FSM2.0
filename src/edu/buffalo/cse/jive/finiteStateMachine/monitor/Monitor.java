package edu.buffalo.cse.jive.finiteStateMachine.monitor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.Expression;
import edu.buffalo.cse.jive.finiteStateMachine.models.Context;
import edu.buffalo.cse.jive.finiteStateMachine.models.Event;
import edu.buffalo.cse.jive.finiteStateMachine.models.State;
import edu.buffalo.cse.jive.finiteStateMachine.models.TransitionBuilder;
import edu.buffalo.cse.jive.finiteStateMachine.util.Pair;

public abstract class Monitor implements Runnable {

	private Set<String> keyFields;
	private BlockingQueue<Event> source;
	private Map<State, Set<State>> states;
	private State rootState;
	private State previousState;

	public Monitor(Set<String> keyFields, BlockingQueue<Event> source) {
		this.keyFields = keyFields;
		this.source = source;
		this.states = new HashMap<State, Set<State>>();
		previousState = new State();
		for (String field : getKeyFields()) {
			if (Event.map.containsKey(field))
				previousState.getMap().put(Event.map.get(field), null);
			else
				previousState.getMap().put(field, null);
		}
	}

	protected boolean buildStates(Event event) {
		boolean result = false;
		if (keyFields.contains(event.getField()) || keyFields.contains(getEventKey(event.getField()))) {
			State newState = previousState.copy();
			newState.getMap().put(event.getField(), event.getValue());
			if (!newState.getMap().values().contains(null) && !previousState.getMap().values().contains(null)) {
				result = states.get(previousState).add(newState);
				if (!states.containsKey(newState))
					states.put(newState, new LinkedHashSet<State>());
			} else if (!newState.getMap().values().contains(null) && previousState.getMap().values().contains(null)) {
				states.put(newState, new LinkedHashSet<State>());
				rootState = newState;
			}
			previousState = newState;
		}
		return result;
	}

	private String getEventKey(String value) {
		if (Event.map == null)
			return null;
		for (Map.Entry<String, String> entry : Event.map.entrySet()) {
			if (entry.getValue().equals(value))
				return entry.getKey();
		}
		return null;
	}

	public void validate(List<Expression> expressions) {
		rootState.setValid(validate(rootState, expressions));
	}

	public void resetStates() {
		for (State key : states.keySet()) {
			for (State state : states.get(key))
				state.reset();
		}
	}

	private boolean validate(State root, List<Expression> expressions) {
		boolean valid = true;
		for (Expression expression : expressions) {
			valid = expression.evaluate(new Context(root, null, states)) && valid;
		}
		return valid;
	}

	public void buildTransitions(TransitionBuilder transitionBuilder) {
		transitionBuilder.addInitialState(rootState, rootState.isValid());
		buildTransitions(null, rootState, new HashSet<Pair<State, State>>(), transitionBuilder);
	}

	private void buildTransitions(State prev, State curr, Set<Pair<State, State>> visited,
			TransitionBuilder transitionBuilder) {
		for (State next : states.get(curr))
			if (visited.add(new Pair<State, State>(curr, next)))
				buildTransitions(curr, next, visited, transitionBuilder);

		if (prev != null)
			transitionBuilder.addTransition(prev, curr, curr.isValid());
	}

	protected void printStates() {
		for (State key : states.keySet()) {
			System.out.print(key + " : ");
			for (State state : states.get(key)) {
				System.out.print(state + " ");
			}
			System.out.println();
		}
	}

	public Set<String> getKeyFields() {
		return keyFields;
	}

	public void setKeyFields(Set<String> keyFields) {
		this.keyFields = keyFields;
	}

	public BlockingQueue<Event> getSource() {
		return source;
	}

	public void setSource(BlockingQueue<Event> source) {
		this.source = source;
	}

	public Map<State, Set<State>> getStates() {
		return states;
	}

	public void setStates(Map<State, Set<State>> states) {
		this.states = states;
	}

	public State getRootState() {
		return rootState;
	}

	public void setRootState(State rootState) {
		this.rootState = rootState;
	}
}
