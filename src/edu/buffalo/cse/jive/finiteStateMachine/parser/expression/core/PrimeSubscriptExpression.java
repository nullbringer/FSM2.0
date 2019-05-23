package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.core;

import java.util.ArrayList;
import java.util.List;

import edu.buffalo.cse.jive.finiteStateMachine.models.Context;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.IUnaryExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.IntegerValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.StringValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.ValueExpression;

public class PrimeSubscriptExpression extends ValueExpression
		implements Comparable<ValueExpression>, IUnaryExpression<ValueExpression> {
	private String name;
	private ValueExpression expression;
	int index;

	public PrimeSubscriptExpression(String name, ValueExpression expression) {
		this.name = name;
		this.index = (Integer) expression.getValue();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Boolean evaluate(Context context) {
		List<String> list = new ArrayList<>();
		ValueExpression expr = context.getNextState().getVector().get(name);
		String listString = (String) expr.getValue();
		String[] listValue = listString.split(" |\\]|\\[");
		for (int i = 0; i < listValue.length; i++) {
			if (!(listValue[i].equals("") || listValue[i].equals("[") || listValue[i].equals("]"))) {
				list.add(listValue[i]);
			}
		}
		if (list.size() > index) {
			String subscriptValue = list.get(index);
			if (subscriptValue.charAt(0) == '-' || subscriptValue.charAt(0) == '0' || subscriptValue.charAt(0) == '1'
					|| subscriptValue.charAt(0) == '2' || subscriptValue.charAt(0) == '3'
					|| subscriptValue.charAt(0) == '4' || subscriptValue.charAt(0) == '5'
					|| subscriptValue.charAt(0) == '6' || subscriptValue.charAt(0) == '7'
					|| subscriptValue.charAt(0) == '8' || subscriptValue.charAt(0) == '9') {
				setExpression(new IntegerValueExpression(Integer.parseInt(subscriptValue)));

			} else {
				setExpression(new StringValueExpression(subscriptValue));
			}
		} else {
			setExpression(new IntegerValueExpression(Integer.MIN_VALUE));
		}
		return true;
	}

	@Override
	public ValueExpression getExpression() {
		return expression;
	}

	@Override
	public void setExpression(ValueExpression expression) {
		this.expression = expression;
	}

	@Override
	public Object getValue() {
		return getExpression().getValue();
	}
}
