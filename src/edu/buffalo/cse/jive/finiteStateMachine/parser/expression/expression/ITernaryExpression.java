package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression;
/**
 * @author Amlan Gupta
 * @email amlangup@buffalo.edu
 *
 */
public interface ITernaryExpression<T extends Expression, V extends Expression, K extends Expression> {

	public T getExpressionA();

	public T getExpressionB();
	
	public T getExpressionC();

	public void setExpressionA(T expression);

	public void setExpressionB(T expression);
	
	public void setExpressionC(T expression);
}
