package edu.buffalo.cse.jive.finiteStateMachine.expression.logical;

import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.BinaryExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.Expression;
import edu.buffalo.cse.jive.finiteStateMachine.models.Context;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public class OrExpression extends BinaryExpression<Expression, Expression> {

	public OrExpression() {
		super();
	}

	public Boolean evaluate(Context context) {
		return getExpressionA().evaluate(context) || getExpressionB().evaluate(context);
	}

}
