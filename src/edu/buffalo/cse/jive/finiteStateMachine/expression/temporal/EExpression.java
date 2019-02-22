package edu.buffalo.cse.jive.finiteStateMachine.expression.temporal;

import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.Expression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.UnaryExpression;
import edu.buffalo.cse.jive.finiteStateMachine.models.Context;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public class EExpression extends UnaryExpression<Expression> {

	public EExpression() {
		super();
	}

	public EExpression(Expression expression) {
		super(expression);
	}

	@Override
	public Boolean evaluate(Context context) {
		return null;
	}
}
