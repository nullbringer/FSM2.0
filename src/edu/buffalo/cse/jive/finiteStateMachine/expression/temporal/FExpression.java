/**
 * 
 */
package edu.buffalo.cse.jive.finiteStateMachine.expression.temporal;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.Expression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.UnaryExpression;
import edu.buffalo.cse.jive.finiteStateMachine.models.Context;
import edu.buffalo.cse.jive.finiteStateMachine.models.State;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public class FExpression extends UnaryExpression<Expression> {

	public FExpression() {
		super();
	}

	public FExpression(Expression expression) {
		super(expression);
	}

	@Override
	public Boolean evaluate(Context context) {
		return evaluate(context.getCurrentState(), null, context.getCurrentState(), new HashSet<State>(),
				getExpression(), context.getStates());
	}

	private boolean evaluate(State root, State prev, State curr, Set<State> visited, Expression expression,
			Map<State, Set<State>> states) {
		boolean currentResult = true;
		for (State next : states.get(curr)) {
			currentResult = expression.evaluate(new Context(curr, next, states)) && currentResult;
		}
		if (!currentResult) {
			boolean childResult = true;
			for (State next : states.get(curr)) {
				if (childResult) {
					if (visited.add(next)) {
						boolean temp = evaluate(root, curr, next, visited, expression, states);
						childResult = temp && childResult;
						visited.remove(next);
					} else {
						childResult = childResult && currentResult;
					}
				}
			}
			currentResult = childResult;
		}
		return currentResult;
	}
}