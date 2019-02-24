package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.logical;

import edu.buffalo.cse.jive.finiteStateMachine.models.Context;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.Expression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.UnaryExpression;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public class NotExpression extends UnaryExpression<Expression> {

	public NotExpression(Expression expression) {
		super(expression);
	}

	public Boolean evaluate(Context ct) {
		return !getExpression().evaluate(ct);
	}

}
