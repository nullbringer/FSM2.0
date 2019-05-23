package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.buffalo.cse.jive.finiteStateMachine.models.Context;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.IUnaryExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.IntegerValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.StringValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.ValueExpression;

public class ListPrimeMinExpression extends ValueExpression
		implements Comparable<ValueExpression>, IUnaryExpression<ValueExpression> {

	private String name;
	private ValueExpression expression;

	public ListPrimeMinExpression(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		if (list.size() > 0) {
			int minIndex = list.indexOf(Collections.min(list));
			String minValue = list.get(minIndex);

			if (minValue.charAt(0) == '-' || minValue.charAt(0) == '0' || minValue.charAt(0) == '1'
					|| minValue.charAt(0) == '2' || minValue.charAt(0) == '3' || minValue.charAt(0) == '4'
					|| minValue.charAt(0) == '5' || minValue.charAt(0) == '6' || minValue.charAt(0) == '7'
					|| minValue.charAt(0) == '8' || minValue.charAt(0) == '9') {
				setExpression(new IntegerValueExpression(Integer.parseInt(minValue)));

			} else {
				setExpression(new StringValueExpression(minValue));
			}

		} else {
			setExpression(new IntegerValueExpression(Integer.MIN_VALUE));
		}
		return true;
	}

}
