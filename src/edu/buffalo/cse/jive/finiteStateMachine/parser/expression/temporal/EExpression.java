package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.temporal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

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
		return evaluate(null, context.getCurrentState(), context.getStates());
	}
	
	/**
	 * EF Expression Implementation - Traverses the whole graph and returns the shortest path to the valid state as true.
	 * marks others as invalid
	 * Marks root state as invalid if all child states are invalid
	 * 
	 * @param prev
	 * @param curr
	 * @param visited
	 * @param states
	 * @return
	 */
	private Boolean evaluate(State prev, State rootState, Map<State, Set<State>> states) {
		boolean currentResult = false;
		Queue<State> toBeVisited = new LinkedList<State>();
		Stack<State> visited = new Stack<State>();
		toBeVisited.add(rootState);
		visited.add(rootState);
		State targetState = new State();
		while(!toBeVisited.isEmpty()){
			State curr = toBeVisited.poll();
			for (State next : states.get(curr)){
				if(!visited.contains(next)) {
					boolean isMatch = getExpression().evaluate(new Context(next, null, states));
					if(isMatch) {
						targetState = next;
						currentResult = true;
						break;	
					}
					visited.push(next);
					toBeVisited.add(next);
				}
			}	
			if(currentResult)break;
		}
		
		if(!currentResult) return currentResult;	
		List<State> shortestPathList = constuctShortestPath(targetState, visited, states);
		markStates(shortestPathList, states);
		
		return currentResult;
	}
	
	private List<State> constuctShortestPath(State targetState, Stack<State> visited, Map<State, Set<State>> states) {
		List<State> shortestPathList = new ArrayList<State>();
		shortestPathList.add(targetState);
		
		while(!visited.isEmpty())
		{
			State sourceNode = visited.pop();
			for (State sourceChild : states.get(sourceNode)){
				if(sourceChild.equals(targetState)) {
					shortestPathList.add(sourceNode);
					targetState = sourceNode;
					break;	
				}
			}
		}
		return shortestPathList;
	}
	private void markStates(List<State> shortestPathList, Map<State, Set<State>> states) {
		for (Set<State> childList : states.values()) {
		    for(State state:childList) {
		    	if(shortestPathList.contains(state))state.setValid(true);
		    	else state.setValid(false);
		    }
		}
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
	@SuppressWarnings("unused")
	private Boolean evaluate2(State prev, State curr, Set<State> visited, Map<State, Set<State>> states) {
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
				if ((childResult = evaluate2(curr, next, visited, states)))
					break;
			currentResult = childResult;
		}
		return currentResult;
	}
}
