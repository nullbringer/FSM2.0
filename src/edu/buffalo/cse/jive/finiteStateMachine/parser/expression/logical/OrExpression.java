package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.logical;

import edu.buffalo.cse.jive.finiteStateMachine.models.Context;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.BinaryExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.Expression;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public class OrExpression extends BinaryExpression<Expression, Expression> {

	public OrExpression(Expression expressionA, Expression expressionB) {
		super(expressionA, expressionB);
	}

	public Boolean evaluate(Context context) {
		return getExpressionA().evaluate(context) || getExpressionB().evaluate(context);
	}

}
