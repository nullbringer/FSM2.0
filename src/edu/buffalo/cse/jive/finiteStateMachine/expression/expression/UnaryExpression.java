package edu.buffalo.cse.jive.finiteStateMachine.expression.expression;
/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public abstract class UnaryExpression<T extends Expression> extends Expression implements IUnaryExpression<T> {

	private T expression;

	public UnaryExpression() {
	}

	public UnaryExpression(T expression) {
		super();
		this.expression = expression;
	}

	public T getExpression() {
		return expression;
	}

	public void setExpression(T expression) {
		this.expression = expression;
	}

}
