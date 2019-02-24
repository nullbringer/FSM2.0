package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.arithmetic;

import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.ValueExpression;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public class AdditionExpression extends ArithmeticExpression {

	public AdditionExpression(ValueExpression expression, ValueExpression expression2) {
		super(expression, expression2);
	}

	@Override
	public Object getValue() {
		return getExpressionA().add(getExpressionB());
	}
}
