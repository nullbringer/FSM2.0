package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.ListOperations;

import edu.buffalo.cse.jive.finiteStateMachine.models.Context;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.ListValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.ValueExpression;

public class InExpression  extends ListValueExpression {
	
	private ValueExpression expressionA;
	private ListValueExpression expressionB;

	public InExpression(ValueExpression expressionA, ListValueExpression expressionB) {
		super();
		this.expressionA = expressionA;
		this.expressionB = expressionB;
	}

	
	public ValueExpression getExpressionA() {
		return expressionA;
	}


	public void setExpressionA(ValueExpression expressionA) {
		this.expressionA = expressionA;
	}


	public ListValueExpression getExpressionB() {
		return expressionB;
	}


	public void setExpressionB(ListValueExpression expressionB) {
		this.expressionB = expressionB;
	}


	@Override
	public Boolean evaluate(Context context) {
		getExpressionA().evaluate(context);
		getExpressionB().evaluate(context);
		return getExpressionB().in(getExpressionA());
	}

}
