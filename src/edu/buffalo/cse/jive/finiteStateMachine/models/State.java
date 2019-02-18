package edu.buffalo.cse.jive.finiteStateMachine.models;

import java.util.LinkedHashMap;
import java.util.Map;

import edu.buffalo.cse.jive.finiteStateMachine.expression.value.ValueExpression;

public class State {

	private Map<String, ValueExpression> map;
	private boolean valid;

	public State() {
		this.valid = true;
		this.map = new LinkedHashMap<>();
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (String key : map.keySet()) {
			stringBuilder.append(map.get(key));
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
		for (String s : map.keySet()) {
			hash ^= s.hashCode() ^ map.get(s).hashCode();
		}
		return hash;
	}

	public Map<String, ValueExpression> getMap() {
		return map;
	}

	public void setMap(Map<String, ValueExpression> map) {
		this.map = map;
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
		state.setMap(new LinkedHashMap<String, ValueExpression>(map));
		return state;
	}
}
