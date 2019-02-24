package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.relational;

import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.BinaryExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.ValueExpression;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public abstract class RelationalExpression extends BinaryExpression<ValueExpression, ValueExpression> {

	public RelationalExpression(ValueExpression expressionA, ValueExpression expressionB) {
		super(expressionA, expressionB);
	}
}
