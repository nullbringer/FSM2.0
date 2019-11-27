package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.temporal;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import edu.buffalo.cse.jive.finiteStateMachine.models.Context;
import edu.buffalo.cse.jive.finiteStateMachine.models.State;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.Expression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.TernaryExpression;

/**
 * 
 *@author Amlan Gupta
 *@email amlangup@buffalo.edu
 *
 */
public class PathExpression extends TernaryExpression<Expression, Expression, Expression> {

	public PathExpression(Expression expressionA, Expression expressionB, Expression expressionC) {
		super(expressionA, expressionB, expressionC);
	}




	@Override
	public Boolean evaluate(Context context) {
		
//		return getExpressionA().evaluate(context) && getExpressionB().evaluate(context);
		
		Result result =  evaluate(context.getCurrentState(), context.getStates());
		
		if(result.equals(Result.TRUE_PATH))return true;
		
		return false;
		
	}

	enum Result{
		NOT_FOUND,
		TRUE_PATH,
		WRONG_PATH
	}

	private Result evaluate(State rootState, Map<State, Set<State>> states) {
		
		// find first from match
		

		boolean currentResult = false;
		
		Queue<State> toBeVisited = new LinkedList<State>();
		Stack<State> visited = new Stack<State>();
		
		toBeVisited.add(rootState);
		visited.add(rootState);
		
		
		State sourceState = new State();
		
		while(!toBeVisited.isEmpty()){
			State curr = toBeVisited.poll();
			
			for (State next : states.get(curr)){
				if(!visited.contains(next)) {
					boolean isMatch = getExpressionA().evaluate(new Context(next, null, states));
					if(isMatch) {
						sourceState = next;
						currentResult = true;
						break;	
					}
					visited.push(next);
					toBeVisited.add(next);
				}
			}	
			
			
			if(getExpressionA().evaluate(new Context(curr, null, states))) {
				sourceState = curr;
				currentResult = true;	
			}
			
			if(currentResult)break;
		}
		
		if(!currentResult) return Result.NOT_FOUND;	
		
		return findTargetThroughMedium(sourceState, states, false, new HashSet<State>());
	
	}




	private Result findTargetThroughMedium(State curr, Map<State, Set<State>> states, 
			boolean foundMid, HashSet<State> visited) {
		Result result = Result.NOT_FOUND;
	
		boolean isTarget = getExpressionC().evaluate(new Context(curr, null, states));
		if(isTarget) {
			if(foundMid)return Result.TRUE_PATH;
			return Result.WRONG_PATH;
		}
		
		if(!foundMid)foundMid = getExpressionB().evaluate(new Context(curr, null, states)); 
		
		visited.add(curr);
		
		if(!states.get(curr).isEmpty()) {
			
			for (State next : states.get(curr)){
				if(!visited.contains(next)) {

					Result tempResult = findTargetThroughMedium(next, states, foundMid, visited);
					
					if(tempResult.equals(Result.WRONG_PATH))return Result.WRONG_PATH;
					if(tempResult.equals(Result.TRUE_PATH))result = tempResult;
					
				} 
			}
		}
		
		visited.remove(curr);
		
		return result;
	}
	
	




}
