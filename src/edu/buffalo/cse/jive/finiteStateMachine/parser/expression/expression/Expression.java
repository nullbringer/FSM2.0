/**
 * 
 */
package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression;

import edu.buffalo.cse.jive.finiteStateMachine.models.Context;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public abstract class Expression {

	private boolean evaluatable = true;

	public abstract Boolean evaluate(Context context);

	public Expression() {
	}

	public boolean isEvaluatable() {
		return evaluatable;
	}

	public void setEvaluatable(boolean evaluatable) {
		this.evaluatable = evaluatable;
	}

}
