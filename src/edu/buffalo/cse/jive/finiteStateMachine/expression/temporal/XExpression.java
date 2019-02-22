package edu.buffalo.cse.jive.finiteStateMachine.expression.temporal;

import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.Expression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.UnaryExpression;
import edu.buffalo.cse.jive.finiteStateMachine.models.Context;
/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public class XExpression extends UnaryExpression<Expression>{

	public XExpression() {
		super();
		// TODO Auto-generated constructor stub
	}

	public XExpression(Expression expression) {
		super(expression);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Boolean evaluate(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
