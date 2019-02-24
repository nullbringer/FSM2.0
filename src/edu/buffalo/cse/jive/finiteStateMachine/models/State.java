package edu.buffalo.cse.jive.finiteStateMachine.models;

import java.util.LinkedHashMap;
import java.util.Map;

import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.ValueExpression;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
/**
 * The heart of the application.
 *
 */
public class State {

	private Map<String, ValueExpression> vector;
	private boolean valid;

	public State() {
		this.valid = true;
		this.vector = new LinkedHashMap<>();
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (String key : vector.keySet()) {
			stringBuilder.append(vector.get(key));
			stringBuilder.append(",");
		}
		return stringBuilder.substring(0, stringBuilder.length() - 1).toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof State))
			return false;
		State newState = (State) obj;
		return toString().equals(newState.toString());
	}

	@Override
	public int hashCode() {
		int hash = 0;
		for (String s : vector.keySet()) {
			hash ^= s.hashCode() ^ vector.get(s).hashCode();
		}
		return hash;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		if (this.valid)
			this.valid = valid;
	}

	public void reset() {
		this.valid = true;
	}

	public State copy() {
		State state = new State();
		state.setVector(new LinkedHashMap<String, ValueExpression>(vector));
		return state;
	}

	public Map<String, ValueExpression> getVector() {
		return vector;
	}

	public void setVector(Map<String, ValueExpression> vector) {
		this.vector = vector;
	}

}
