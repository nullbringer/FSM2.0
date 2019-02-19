package edu.buffalo.cse.jive.finiteStateMachine.expression.temporal;

import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.Expression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.UnaryExpression;
import edu.buffalo.cse.jive.finiteStateMachine.models.Context;

public class UExpression extends UnaryExpression<Expression> {

	public UExpression() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UExpression(Expression expression) {
		super(expression);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Boolean evaluate(Context context) {
		return getExpression().evaluate(context);
	}

}
