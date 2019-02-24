/**
 * 
 */
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
public class GExpression extends UnaryExpression<Expression> {

	public GExpression(Expression expression) {
		super(expression);
	}

	@Override
	public Boolean evaluate(Context context) {
		return evaluate(null, context.getCurrentState(), new HashSet<State>(), context.getStates());
	}

	/**
	 * G Expression Convenience Implementation - Traverses the whole graph and marks
	 * all invalid states
	 * 
	 * @param prev
	 * @param curr
	 * @param visited
	 * @param expression
	 * @param states
	 * @return
	 */
	private Boolean evaluate(State prev, State curr, Set<State> visited, Map<State, Set<State>> states) {
		boolean currentResult = true;
		for (State next : states.get(curr)) {
			currentResult = getExpression().evaluate(new Context(curr, next, states)) && currentResult;
		}
		curr.setValid(currentResult);
		if (visited.add(curr)) {
			for (State next : states.get(curr)) {
				currentResult = evaluate(curr, next, visited, states) && currentResult;
			}
		}
		return currentResult;
	}

	/**
	 * G Expression Implementation - Traverses the whole graph and checks for
	 * invalid states. Returns on finding the first invalid state.
	 * 
	 * @param prev
	 * @param curr
	 * @param visited
	 * @param expression
	 * @param states
	 * @return
	 */
	@SuppressWarnings("unused")
	private Boolean evaluate2(State prev, State curr, Set<State> visited, Map<State, Set<State>> states) {
		boolean currentResult = true;
		for (State next : states.get(curr)) {
			currentResult = getExpression().evaluate(new Context(curr, next, states)) && currentResult;
		}
		if (currentResult && visited.add(curr)) {
			boolean childResult = currentResult;
			for (State next : states.get(curr))
				if (!(childResult = evaluate2(curr, next, visited, states)))
					break;
			currentResult = childResult;
		}
		return currentResult;
	}
}
