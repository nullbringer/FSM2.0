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
import edu.buffalo.cse.jive.finiteStateMachine.util.Pair;

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
		return evaluate(null, context.getCurrentState(), new HashSet<Pair<State, State>>(), getExpression(),
				context.getStates());
	}

	private boolean evaluate(State prev, State curr, Set<Pair<State, State>> visited, Expression expression,
			Map<State, Set<State>> states) {
		boolean currentResult = true;
		for (State next : states.get(curr)) {
			currentResult = expression.evaluate(new Context(curr, next, states)) && currentResult;
		}
		if (!currentResult) {
			boolean childResult = true;
			int size = visited.size();
			for (State next : states.get(curr)) {
				if (visited.add(new Pair<State, State>(curr, next)) && childResult) {
					boolean temp = evaluate(curr, next, visited, expression, states);
					childResult = temp && childResult;
				}
			}
			if (visited.size() > size)
				currentResult = childResult;
		}
		return currentResult;
	}
}