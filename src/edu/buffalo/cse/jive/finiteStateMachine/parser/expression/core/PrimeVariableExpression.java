package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.core;

import edu.buffalo.cse.jive.finiteStateMachine.models.Context;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.IUnaryExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.ValueExpression;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public class PrimeVariableExpression extends ValueExpression
		implements Comparable<ValueExpression>, IUnaryExpression<ValueExpression> {

	private String name;
	private ValueExpression expression;

	public PrimeVariableExpression(String name, ValueExpression expression) {
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
		setExpression(context.getNextState().getVector().get(name));
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
