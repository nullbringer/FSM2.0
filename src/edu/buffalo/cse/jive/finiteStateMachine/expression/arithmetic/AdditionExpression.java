package edu.buffalo.cse.jive.finiteStateMachine.expression.arithmetic;

import edu.buffalo.cse.jive.finiteStateMachine.expression.value.ValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.models.Context;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public class AdditionExpression extends ArithmeticExpression {

	public AdditionExpression() {
		super();
	}

	public AdditionExpression(ValueExpression expression, ValueExpression expression2) {
		super(expression, expression2);
	}

	@Override
	public Boolean evaluate(Context context) {
		getExpressionA().evaluate(context);
		getExpressionB().evaluate(context);
		return true;
	}

	@Override
	public Object getValue() {
		return getExpressionA().add(getExpressionB());
	}
}
