package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.core;

import edu.buffalo.cse.jive.finiteStateMachine.models.Context;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.BinaryExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.Expression;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public class ImplicationExpression extends BinaryExpression<Expression, Expression> {

	public ImplicationExpression(Expression expressionA, Expression expressionB) {
		super(expressionA, expressionB);
	}

	public Boolean evaluate(Context context) {
		if (getExpressionA().evaluate(context)) {
			if (getExpressionB().evaluate(context)) {
				return true;
			}
			return false;
		}
		return true;
	}
}
