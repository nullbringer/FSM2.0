/**
 * 
 */
package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.relational;

import edu.buffalo.cse.jive.finiteStateMachine.models.Context;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.ValueExpression;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 * 
 */
public class GreaterThanEqualToExpression extends RelationalExpression {
	
	public GreaterThanEqualToExpression(ValueExpression expressionA, ValueExpression expressionB) {
		super(expressionA, expressionB);
	}

	@Override
	public Boolean evaluate(Context context) {
		getExpressionA().evaluate(context);
		getExpressionB().evaluate(context);
		return getExpressionA().compareTo(getExpressionB()) >= 0;
	}
}
