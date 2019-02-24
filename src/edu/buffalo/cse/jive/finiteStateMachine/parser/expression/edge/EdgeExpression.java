package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.edge;

import edu.buffalo.cse.jive.finiteStateMachine.models.Context;
import edu.buffalo.cse.jive.finiteStateMachine.models.State;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.BinaryExpression;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public class EdgeExpression extends BinaryExpression<VectorExpression, VectorExpression> {

	public EdgeExpression(VectorExpression expressionA, VectorExpression expressionB) {
		super(expressionA, expressionB);
	}

	@Override
	public Boolean evaluate(Context context) {
		State nextState = context.getNextState();
		State currentState = context.getCurrentState();
		if (nextState == null || currentState == null)
			return false;
		return currentState.getVector().values().equals(getExpressionA().getVectorValue())
				&& nextState.getVector().values().equals(getExpressionB().getVectorValue());
	}

}
