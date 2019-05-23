package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.core;

import java.util.ArrayList;
import java.util.List;

import edu.buffalo.cse.jive.finiteStateMachine.models.Context;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.IUnaryExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.IntegerValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.ValueExpression;

public class ListPrimeSizeExpression extends ValueExpression
		implements Comparable<ValueExpression>, IUnaryExpression<ValueExpression> {

	private String name;
	private ValueExpression expression;

	public ListPrimeSizeExpression(String name) {
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
			int size = list.size();

			setExpression(new IntegerValueExpression(size));

		} else {
			setExpression(new IntegerValueExpression(0));
		}
		return true;
	}

}
