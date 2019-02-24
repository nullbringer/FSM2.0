package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.logical;

import edu.buffalo.cse.jive.finiteStateMachine.models.Context;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.BinaryExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.Expression;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public class AndExpression extends BinaryExpression<Expression, Expression> {

	public AndExpression(Expression expressionA, Expression expressionB) {
		super(expressionA, expressionB);
	}

	public Boolean evaluate(Context context) {
		return getExpressionA().evaluate(context) && getExpressionB().evaluate(context);
	}

}
