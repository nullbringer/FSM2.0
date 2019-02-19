package edu.buffalo.cse.jive.finiteStateMachine.expression.relational;

import edu.buffalo.cse.jive.finiteStateMachine.expression.value.ValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.models.Context;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public class LessThanExpression extends RelationalExpression {

	public LessThanExpression() {
		super();
	}

	public LessThanExpression(ValueExpression expressionA, ValueExpression expressionB) {
		super(expressionA, expressionB);
	}

	@Override
	public Boolean evaluate(Context context) {
		getExpressionA().evaluate(context);
		getExpressionB().evaluate(context);
		return getExpressionA().compareTo(getExpressionB()) < 0;
	}
}
