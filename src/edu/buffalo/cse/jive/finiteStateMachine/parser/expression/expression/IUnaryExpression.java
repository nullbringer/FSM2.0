package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression;
/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public interface IUnaryExpression<T extends Expression> {

	public T getExpression();

	public void setExpression(T expression);

}
