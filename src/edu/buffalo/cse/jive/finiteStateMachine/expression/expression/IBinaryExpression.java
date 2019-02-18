package edu.buffalo.cse.jive.finiteStateMachine.expression.expression;

public interface IBinaryExpression<T extends Expression, V extends Expression> {

	public T getExpressionA();

	public T getExpressionB();

	public void setExpressionA(T expression);

	public void setExpressionB(T expression);
}
