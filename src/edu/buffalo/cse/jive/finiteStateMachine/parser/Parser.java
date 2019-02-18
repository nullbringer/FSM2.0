/**
 * 
 */
package edu.buffalo.cse.jive.finiteStateMachine.parser;

import java.io.IOException;
import java.util.List;

import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.Expression;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public interface Parser {

	public List<Expression> parse(String[] inputs) throws IOException;
}
