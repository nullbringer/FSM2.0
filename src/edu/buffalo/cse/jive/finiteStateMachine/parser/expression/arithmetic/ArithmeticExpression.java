package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.arithmetic;

import edu.buffalo.cse.jive.finiteStateMachine.models.Context;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.IBinaryExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.ValueExpression;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public abstract class ArithmeticExpression extends ValueExpression
		implements IBinaryExpression<ValueExpression, ValueExpression> {
	private ValueExpression expressionA;
	private ValueExpression expressionB;
	{
		setEvaluatable(false);
	}

	public ArithmeticExpression(ValueExpression expressionA, ValueExpression expressionB) {
		this.expressionA = expressionA;
		this.expressionB = expressionB;
	}

	public ValueExpression getExpressionA() {
		return expressionA;
	}

	public void setExpressionA(ValueExpression expressionA) {
		this.expressionA = expressionA;
	}

	public ValueExpression getExpressionB() {
		return expressionB;
	}

	public void setExpressionB(ValueExpression expressionB) {
		this.expressionB = expressionB;
	}

	@Override
	public Boolean evaluate(Context context) {
		getExpressionA().evaluate(context);
		getExpressionB().evaluate(context);
		return true;
	}
}
