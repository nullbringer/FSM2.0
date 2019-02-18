package edu.buffalo.cse.jive.finiteStateMachine.expression.edge;

import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.BinaryExpression;
import edu.buffalo.cse.jive.finiteStateMachine.models.Context;
import edu.buffalo.cse.jive.finiteStateMachine.models.State;

public class EdgeExpression extends BinaryExpression<VectorExpression, VectorExpression> {

	public EdgeExpression() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EdgeExpression(VectorExpression expressionA, VectorExpression expressionB) {
		super(expressionA, expressionB);
	}

	@Override
	public Boolean evaluate(Context context) {
		State nextState = context.getNextState();
		State currentState = context.getCurrentState();
		if (nextState == null || currentState == null)
			return false;
		return currentState.getMap().values().equals(getExpressionA().getVectorValue())
				&& nextState.getMap().values().equals(getExpressionB().getVectorValue());
	}

}
