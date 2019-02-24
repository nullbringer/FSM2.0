package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.arithmetic;

import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.ValueExpression;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public class SubtractionExpression extends ArithmeticExpression {

	public SubtractionExpression(ValueExpression expressionA, ValueExpression expressionB) {
		super(expressionA, expressionB);
	}

	@Override
	public Object getValue() {
		return getExpressionA().subtract(getExpressionB());
	}
}
