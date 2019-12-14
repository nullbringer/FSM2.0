package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.temporal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import edu.buffalo.cse.jive.finiteStateMachine.models.Context;
import edu.buffalo.cse.jive.finiteStateMachine.models.State;
import edu.buffalo.cse.jive.finiteStateMachine.models.State.Status;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.Expression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.UnaryExpression;
import edu.buffalo.cse.jive.finiteStateMachine.util.Pair;
import edu.buffalo.cse.jive.finiteStateMachine.util.TemporaryDataTransporter;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 * 
 *@author Amlan Gupta
 *@email amlangup@buffalo.edu
 *
 */
public class EExpression extends UnaryExpression<Expression> {

	public EExpression(Expression expression) {
		super(expression);
	}

	@Override
	public Boolean evaluate(Context context) {
		
		if(context.isUseMarker())return evaluate(null, context.getCurrentState(), context.getStates());
		return evaluate2(null, context.getCurrentState(), new HashSet<State>(), context.getStates());
		
	}

	/**
	 * EF Expression Implementation - Traverses the whole graph and returns if any
	 * state is true. Marks state as invalid if all child states are invalid.
	 * Finds the shortest path to the target
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
		State targetState = new State();
		
		//check if rootstate is match
		boolean isMatch = getExpression().evaluate(new Context(rootState, null, states));
		if(isMatch) {
			targetState = rootState;
			currentResult = true;
		}
		
		if(!currentResult) {
			toBeVisited.add(rootState);
			visited.add(rootState);
			
			while(!toBeVisited.isEmpty()){
				State curr = toBeVisited.poll();
				for (State next : states.get(curr)){
					if(!visited.contains(next)) {
						isMatch = getExpression().evaluate(new Context(next, null, states));
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
		}
		
		if(!currentResult) return currentResult;	
		List<State> shortestPathList = constuctShortestPath(targetState, visited, states);
		
		
		markStatesInPath(shortestPathList, states);
		
		return currentResult;
	}
	
	private List<State> constuctShortestPath(State targetState, Stack<State> visited, Map<State, Set<State>> states) {
		List<State> shortestPathList = new ArrayList<State>();
		List<Pair<State, State>> pathPairs = new ArrayList<Pair<State,State>>();
		shortestPathList.add(targetState);
		
		while(!visited.isEmpty())
		{
			State sourceNode = visited.pop();
			for (State sourceChild : states.get(sourceNode)){
				if(sourceChild.equals(targetState)) {
					pathPairs.add(new Pair<State, State>(sourceNode, sourceChild));
					shortestPathList.add(sourceNode);
					targetState = sourceNode;
					break;	
				}
			}
		}
		
		TemporaryDataTransporter.setPath(pathPairs);
		
		return shortestPathList;
	}
	private void markStatesInPath(List<State> shortestPathList, Map<State, Set<State>> states) {
		for (Set<State> childList : states.values()) {
		    for(State state:childList) {
		    	if(shortestPathList.contains(state)) {
		    		if(TemporaryDataTransporter.shouldHighlight)
		    			state.setStatus(Status.MARKED);
		    		else 
		    			state.setStatus(Status.VALID);
		    	}
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
