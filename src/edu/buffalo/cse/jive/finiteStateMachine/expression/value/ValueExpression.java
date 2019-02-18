package edu.buffalo.cse.jive.finiteStateMachine.expression.value;

import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.Expression;
import edu.buffalo.cse.jive.finiteStateMachine.models.Context;

public abstract class ValueExpression extends Expression implements Comparable<ValueExpression> {

	private Object value;

	public ValueExpression() {
		this.hashCode();
	}

	@Override
	public Boolean evaluate(Context context) {
		return true;
	}

	public ValueExpression(Object value) {
		this.value = value;
		this.hashCode();
	}

	public Object getValue() {
		return this.value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public int compareTo(ValueExpression valueExpression) {
		Object value1 = this.getValue();
		Object value2 = valueExpression.getValue();
		switch (value1.getClass().getSimpleName()) {
		case "Double":
			return new Double((Double) value1).compareTo(new Double((Double) value2));
		case "String":
			return new String((String) value1).compareTo(new String((String) value2));
		case "Integer":
			return new Integer((Integer) value1).compareTo(new Integer((Integer) value2));
		}
		throw new IllegalArgumentException();
	}

	public Object add(ValueExpression valueExpression) {
		Object value1 = this.getValue();
		Object value2 = valueExpression.getValue();
		switch (value1.getClass().getSimpleName()) {
		case "Double":
			return new Double((Double) value1) + new Double((Double) value2);
		case "String":
			return new String((String) value1) + new String((String) value2);
		case "Integer":
			return new Integer((Integer) value1) + new Integer((Integer) value2);

		}
		throw new IllegalArgumentException();
	}

	public Object subtract(ValueExpression valueExpression) {
		Object value1 = this.getValue();
		Object value2 = valueExpression.getValue();
		switch (value1.getClass().getSimpleName()) {
		case "Double":
			return new Double((Double) value1) - new Double((Double) value2);
		case "Integer":
			return new Integer((Integer) value1) - new Integer((Integer) value2);
		}
		throw new IllegalArgumentException();
	}

	public Object divide(ValueExpression valueExpression) {
		Object value1 = this.getValue();
		Object value2 = valueExpression.getValue();
		switch (value1.getClass().getSimpleName()) {
		case "Double":
			return new Double((Double) value1) / new Double((Double) value2);
		case "Integer":
			return new Integer((Integer) value1) / new Integer((Integer) value2);
		}
		throw new IllegalArgumentException();
	}

	public Object multiply(ValueExpression valueExpression) {
		Object value1 = this.getValue();
		Object value2 = valueExpression.getValue();
		switch (value1.getClass().getSimpleName()) {
		case "Double":
			return new Double((Double) value1) * new Double((Double) value2);
		case "Integer":
			return new Integer((Integer) value1) * new Integer((Integer) value2);
		}
		throw new IllegalArgumentException();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ValueExpression) {
			ValueExpression val = (ValueExpression) obj;
			return this.getValue().equals(val.getValue());
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (value == null)
			return 0;
		return value.hashCode();
	}

	@Override
	public String toString() {
		return String.valueOf(this.getValue());
	}
}
