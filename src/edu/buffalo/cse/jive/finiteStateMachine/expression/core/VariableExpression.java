package edu.buffalo.cse.jive.finiteStateMachine.expression.core;

import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.IUnaryExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.value.ValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.models.Context;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public class VariableExpression extends ValueExpression
		implements Comparable<ValueExpression>, IUnaryExpression<ValueExpression> {

	private String name;
	private ValueExpression expression;

	public VariableExpression(String name, ValueExpression expression) {
		super(expression);
		this.name = name;
		this.expression = expression;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Boolean evaluate(Context context) {
		if (name.contains("'")) {
			String n = name.substring(0, name.length() - 1);
			setExpression(context.getNextState() == null ? null : context.getNextState().getMap().get(n));
		} else {
			setExpression(context.getCurrentState().getMap().get(name));
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
