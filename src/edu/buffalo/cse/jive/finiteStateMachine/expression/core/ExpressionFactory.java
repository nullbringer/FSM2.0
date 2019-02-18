package edu.buffalo.cse.jive.finiteStateMachine.expression.core;

import edu.buffalo.cse.jive.finiteStateMachine.expression.arithmetic.AdditionExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.arithmetic.DivisionExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.arithmetic.MultiplicationExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.arithmetic.SubtractionExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.edge.EdgeExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.edge.VectorExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.Expression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.logical.AndExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.logical.OrExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.relational.EqualityExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.relational.GreaterThanEqualToExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.relational.GreaterThanExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.relational.LessThanEqualToExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.relational.LessThanExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.relational.NotEqualityExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.value.DoubleValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.value.IntegerValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.value.StringValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.models.Event;
import edu.buffalo.cse.jive.finiteStateMachine.util.Tokenizer;

public class ExpressionFactory {

	public ExpressionFactory() {
		// TODO Auto-generated constructor stub
	}

	public static Expression getExpression(String input) {
		switch (input) {
		case "G":
			return new GExpression();
		case "F":
			return new FExpression();
		case "+":
			return new AdditionExpression();
		case "-":
			return new SubtractionExpression();
		case "/":
			return new DivisionExpression();
		case "*":
			return new MultiplicationExpression();
		case "->":
			return new ImplicationExpression();
		case "==":
			return new EqualityExpression();
		case "!=":
			return new NotEqualityExpression();
		case "&&":
			return new AndExpression();
		case "||":
			return new OrExpression();
		case "=":
			return new EqualityExpression();
		case ">":
			return new GreaterThanExpression();
		case "<":
			return new LessThanExpression();
		case "<=":
			return new LessThanEqualToExpression();
		case ">=":
			return new GreaterThanEqualToExpression();
		case "=>":
			return new EdgeExpression();
		default:
			try {
				return new IntegerValueExpression(Integer.parseInt(input));
			} catch (Exception e) {
				try {
					return new DoubleValueExpression(Double.parseDouble(input));
				} catch (Exception exception) {
					if (Tokenizer.idSet.contains(input)) {
						if (Event.map.containsKey(input))
							return new VariableExpression(Event.map.get(input), null);
						return new VariableExpression(input, null);
					} else if (input.startsWith("<")) {
						return new VectorExpression(input);
					} else {
						return new StringValueExpression(input.substring(1, input.length() - 1));
					}
				}
			}
		}
	}

}
