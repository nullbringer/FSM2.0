/**
 * 
 */
package edu.buffalo.cse.jive.finiteStateMachine.parser;

import java.util.List;

import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.Expression;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public interface Parser {

	public List<Expression> parse(String[] inputs) throws Exception;
}
