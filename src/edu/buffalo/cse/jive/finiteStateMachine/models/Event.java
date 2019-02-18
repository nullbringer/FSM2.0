package edu.buffalo.cse.jive.finiteStateMachine.models;

import java.util.HashMap;
import java.util.Map;

import edu.buffalo.cse.jive.finiteStateMachine.expression.value.DoubleValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.value.IntegerValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.value.StringValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.value.ValueExpression;

public class Event {

	private String field;
	private ValueExpression value;
	public static Map<String, String> map = new HashMap<>();

	public Event(String field, String value) {
		this.field = field;
		try {
			this.value = new IntegerValueExpression(Integer.parseInt(value));
		} catch (Exception e) {
			try {
				this.value = new DoubleValueExpression(Double.parseDouble(value));
			} catch (Exception e2) {
				this.value = new StringValueExpression(value);
			}
		}
	}

	public String getField() {
		if (map.containsKey(this.field)) {
			return map.get(this.field);
		}
		return this.field;
	}

	public ValueExpression getValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return field + " " + value;
	}

}
