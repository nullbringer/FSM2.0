package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression;
/**
 * @author Amlan Gupta
 * @email amlangup@buffalo.edu
 *
 */
public abstract class TernaryExpression<T extends Expression, V extends Expression, K extends Expression> extends Expression
		implements ITernaryExpression<T, V, K> {

	private T expressionA;
	private T expressionB;
	private T expressionC;

	public TernaryExpression(T expressionA, T expressionB, T expressionC) {
		super();
		this.expressionA = expressionA;
		this.expressionB = expressionB;
		this.expressionC = expressionC;
	}

	public T getExpressionA() {
		return expressionA;
	}

	public void setExpressionA(T expressionA) {
		this.expressionA = expressionA;
	}

	public T getExpressionB() {
		return expressionB;
	}

	public void setExpressionB(T expressionB) {
		this.expressionB = expressionB;
	}

	public T getExpressionC() {
		return expressionC;
	}

	public void setExpressionC(T expressionC) {
		this.expressionC = expressionC;
	}
	
}
