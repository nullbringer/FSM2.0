package edu.buffalo.cse.jive.finiteStateMachine.expression.relational;

import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.BinaryExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.value.ValueExpression;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public abstract class RelationalExpression extends BinaryExpression<ValueExpression, ValueExpression> {

	public RelationalExpression() {
		super();
	}

	public RelationalExpression(ValueExpression expressionA, ValueExpression expressionB) {
		super(expressionA, expressionB);
	}
}
