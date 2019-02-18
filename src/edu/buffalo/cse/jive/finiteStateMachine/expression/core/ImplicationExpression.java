package edu.buffalo.cse.jive.finiteStateMachine.expression.core;

import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.BinaryExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.Expression;
import edu.buffalo.cse.jive.finiteStateMachine.models.Context;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public class ImplicationExpression extends BinaryExpression<Expression, Expression> {

	
	public ImplicationExpression() {
		super();
	}

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
