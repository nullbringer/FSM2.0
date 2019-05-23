package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.ListOperations;

import edu.buffalo.cse.jive.finiteStateMachine.models.Context;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.ListValueExpression;

public class ListEqualityExpression extends ListValueExpression {

	private ListValueExpression expressionA;
	private ListValueExpression expressionB;

	public ListEqualityExpression(ListValueExpression expressionA, ListValueExpression expressionB) {
		super();
		this.expressionA = expressionA;
		this.expressionB = expressionB;
	}

	public ListValueExpression getExpressionA() {
		return expressionA;
	}

	public void setExpressionA(ListValueExpression expressionA) {
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
		return getExpressionA().compareTo(getExpressionB()) == 0;
	}

}
