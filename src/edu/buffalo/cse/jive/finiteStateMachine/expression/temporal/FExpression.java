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

	private boolean result;

	public FExpression() {
		super();
	}

	public FExpression(Expression expression) {
		super(expression);
	}

	@Override
	public Boolean evaluate(Context context) {
		boolean totalResult = true;
		State root = context.getCurrentState();
		for (State state : context.getStates().get(root)) {
			result = false;
			evaluate(root, null, state, new HashSet<Pair<State, State>>(), getExpression(), context.getStates());
			totalResult = result && totalResult;
			if (totalResult == false)
				return false;
			result = false;
		}
		return totalResult;
	}

	private State evaluate(State root, State prev, State curr, Set<Pair<State, State>> visited, Expression expression,
			Map<State, Set<State>> states) {
		for (State next : states.get(curr)) {
			if (visited.add(new Pair<State, State>(curr, next)) && !result && !curr.equals(root))
				result = expression.evaluate(
						new Context(curr, evaluate(root, curr, next, visited, expression, states), states)) || result;
		}
		return curr;
	}
}
