package edu.buffalo.cse.jive.finiteStateMachine.expression.expression;

public interface IUnaryExpression<T extends Expression> {

	public T getExpression();

	public void setExpression(T expression);

}
