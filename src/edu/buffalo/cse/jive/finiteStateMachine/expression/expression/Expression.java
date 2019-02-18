/**
 * 
 */
package edu.buffalo.cse.jive.finiteStateMachine.expression.expression;

import edu.buffalo.cse.jive.finiteStateMachine.models.Context;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public abstract class Expression {

	public abstract Boolean evaluate(Context context);

	public Expression() {
	}
}
