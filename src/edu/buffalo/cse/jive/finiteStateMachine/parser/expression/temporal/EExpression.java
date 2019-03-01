package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.temporal;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.buffalo.cse.jive.finiteStateMachine.models.Context;
import edu.buffalo.cse.jive.finiteStateMachine.models.State;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.Expression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.UnaryExpression;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public class EExpression extends UnaryExpression<Expression> {

	public EExpression(Expression expression) {
		super(expression);
	}

	@Override
	public Boolean evaluate(Context context) {
		return evaluate(null, context.getCurrentState(), new HashSet<State>(), context.getStates());
	}

	/**
	 * EF Expression Implementation - Traverses the whole graph and returns if any
	 * state is true. Marks state as invalid if all child states are invalid
	 * 
	 * @param prev
	 * @param curr
	 * @param visited
	 * @param states
	 * @return
	 */
	private Boolean evaluate(State prev, State curr, Set<State> visited, Map<State, Set<State>> states) {
		boolean currentResult = true;
		if (!states.get(curr).isEmpty()) {
			for (State next : states.get(curr)) {
				currentResult = getExpression().evaluate(new Context(curr, next, states)) && currentResult;
			}
		} else {
			currentResult = getExpression().evaluate(new Context(curr, null, states)) && currentResult;
		}
		if (!currentResult && visited.add(curr)) {
			boolean childResult = currentResult;
			for (State next : states.get(curr))
				if ((childResult = evaluate(curr, next, visited, states)))
					break;
			currentResult = childResult;
		}
		return currentResult;
	}
}
