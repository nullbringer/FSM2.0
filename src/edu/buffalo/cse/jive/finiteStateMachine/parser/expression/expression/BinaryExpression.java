package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression;
/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public abstract class BinaryExpression<T extends Expression, V extends Expression> extends Expression
		implements IBinaryExpression<T, V> {

	private T expressionA;
	private T expressionB;

	public BinaryExpression(T expressionA, T expressionB) {
		super();
		this.expressionA = expressionA;
		this.expressionB = expressionB;
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

}
