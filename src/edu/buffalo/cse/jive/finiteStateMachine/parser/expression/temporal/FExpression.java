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
 * @author Amlan Gupta
 * @email amlangup@buffalo.edu
 */
public class FExpression extends UnaryExpression<Expression> {

	public FExpression(Expression expression) {
		super(expression);
	}

	@Override
	public Boolean evaluate(Context context) {
		
		int res = evaluate(context.getCurrentState(), context.getStates(),  new HashSet<State>());
		return res==2?true:false;
	}
	
	
	
	/**
	 * AF Expression implementation - Performs backtracking on the graph and marks
	 * the state as invalid if any path in future leads to a leaf without going through the valid state.
	 * 
	 * @param curr
	 * @param states
	 * @param visited
	 * @return int leaf: 0, cycle:1, valid: 2 
	 */

	
	
	private int evaluate(State curr, Map<State, Set<State>> states, Set<State> visited) {
		
		
		int currentResult = 1;
		
		boolean isMatch = getExpression().evaluate(new Context(curr, null, states));
		if(isMatch)return 2;
		
		if(!states.get(curr).isEmpty()) {
			
			for (State next : states.get(curr)){
				if(!visited.contains(next)) {
					visited.add(next);
					int result = evaluate(next, states, visited);
					
					if(result == 0)return 0;
					if(result == 2)currentResult = result;
					
				} 
			}
		}else {
			return 0;
		}
		
		return currentResult;
		
	
	}
	

	/**
	 * AF Expression implementation - Performs backtracking on the graph and marks
	 * the state as invalid if any path future is invalid
	 * 
	 * @param prev
	 * @param curr
	 * @param visited
	 * @param states
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean evaluate2(State prev, State curr, Set<State> visited, Map<State, Set<State>> states) {
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
				if (!(childResult = evaluate2(curr, next, visited, states)))
					break;
			currentResult = childResult;
			visited.remove(curr);
		}
		return currentResult;
	}
}