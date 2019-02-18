package edu.buffalo.cse.jive.finiteStateMachine.expression.arithmetic;

import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.IBinaryExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.value.ValueExpression;

public abstract class ArithmeticExpression extends ValueExpression
		implements IBinaryExpression<ValueExpression, ValueExpression> {

	private ValueExpression expressionA;
	private ValueExpression expressionB;

	public ArithmeticExpression() {
		super();
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

}
