package edu.buffalo.cse.jive.finiteStateMachine.models;

import java.util.HashMap;
import java.util.Map;

import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.DoubleValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.IntegerValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.StringValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.ValueExpression;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public class Event {

	private String field;
	private ValueExpression value;
	private String method;
	public static Map<String, String> abbreviations = new HashMap<>();

	/**
	 * Stores individual events. Parses String into Integer, Double or String value
	 * expression. If you want to add additional types of values, this would be the
	 * place
	 * 
	 * @param field
	 * @param value
	 */
	public Event(String field, String value) {
		this.field = field;
		try {
			this.value = new IntegerValueExpression(Integer.parseInt(value));
		} catch (NumberFormatException e) {
			try {
				this.value = new DoubleValueExpression(Double.parseDouble(value));
			} catch (NumberFormatException e2) {
				this.value = new StringValueExpression(value);
			}
		}
	}
	
	public Event(String field, String value, String method) {
		this.field = field;
		this.method = method;
		try {
			this.value = new IntegerValueExpression(Integer.parseInt(value));
		} catch (NumberFormatException e) {
			try {
				this.value = new DoubleValueExpression(Double.parseDouble(value));
			} catch (NumberFormatException e2) {
				this.value = new StringValueExpression(value);
			}
		}
	}

	public String getField() {
		if (abbreviations.containsKey(this.field)) {
			return abbreviations.get(this.field);
		}
		return this.field;
	}

	public ValueExpression getValue() {
		return this.value;
	}

	public String getMethod() {
		return method;
	}
	
	@Override
	public String toString() {
		return field + " " + value;
	}

}
