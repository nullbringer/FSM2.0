package edu.buffalo.cse.jive.finiteStateMachine.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import edu.buffalo.cse.jive.finiteStateMachine.expression.core.ExpressionFactory;
import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.Expression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.IBinaryExpression;
import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.IUnaryExpression;
import edu.buffalo.cse.jive.finiteStateMachine.models.Node;
import edu.buffalo.cse.jive.finiteStateMachine.util.Operators;
import edu.buffalo.cse.jive.finiteStateMachine.util.Tokenizer;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 * @author Sandeep Kumar
 * @email skumar28@buffalo.edu
 */
public class ParserImpl implements Parser {

	private Expression expression;

	@Override
	public List<Expression> parse(String[] inputs) throws IOException {
		List<Expression> expressions = new ArrayList<>();
		for (String input : inputs) {
			Node<String> tree = buildPrecedenceTree(convertToPostfix(Tokenizer.tokenize(input)));
			expressions.add(parsePreOrder(tree, expression));
			expression = null;
		}
		return expressions;
	}

	public Node<String> buildPrecedenceTree(List<String> inputs) {
		Stack<Node<String>> stack = new Stack<>();
		int i = 0;
		while (i < inputs.size()) {
			String in = inputs.get(i);
			if (Operators.isOperator(in)) {
				if (Operators.isUnaryOperator(in)) {
					Node<String> node = new Node<>();
					node.setData(in);
					node.setLeft(stack.pop());
					node.setRight(new Node<String>("NULL"));
					stack.push(node);
				} else {
					Node<String> node = new Node<>();
					node.setData(in);
					node.setRight(stack.pop());
					node.setLeft(stack.pop());
					stack.push(node);
				}
			} else {
				stack.push(new Node<>(in));
			}
			i++;
		}

		return stack.pop();
	}

	public List<String> convertToPostfix(List<String> input) {
		// initializing empty String for result
		List<String> result = new ArrayList<>();
		Set<String> bracesSet = new HashSet<>();
		bracesSet.add("[");
		bracesSet.add("]");
		bracesSet.add("(");
		bracesSet.add(")");
		// initializing empty stack
		Stack<String> stack = new Stack<>();

		for (int i = 0; i < input.size(); ++i) {
			String token = input.get(i);
			if (token.isEmpty())
				continue;

			// If the scanned character is an operand, add it to output.
			if (!Operators.OPERATOR_PRECEDENCE.containsKey(token) && !bracesSet.contains(token)) {
				result.add(token);
				continue;
			}

			// if the token is belonging to Edge component then we need to take the whole
			// vector between < >
			if (token.equals("<") && input.lastIndexOf(">") >= i) {
				int j = 0;
				StringBuilder edgeBilder = new StringBuilder();
				if (input.contains("=>")) {
					edgeBilder.append(token);
					for (j = i + 1; j < input.size(); j++) {
						String edgeVec = input.get(j);
						edgeBilder.append(edgeVec);
						i++;
						if (edgeVec.equals(">"))
							break;
					}

				}
				result.add(edgeBilder.toString());
				continue;
			}

			// If the scanned character is an '[', push it to the stack.
			else if (token.equals("["))
				stack.push(token);

			// If the scanned character is an ']', pop and output from the stack
			// until an '(' is encountered.
			else if (token.equals("]")) {
				while (!stack.isEmpty() && !stack.peek().equals("[")) {
					result.add(stack.pop());
				}

				if (!stack.isEmpty() && !stack.peek().equals("["))
					return null;// "Invalid Expression"; // invalid expression
				else
					stack.pop();
			} else if (token.equals("("))
				stack.push(token);

			// If the scanned character is an ')', pop and output from the stack
			// until an '(' is encountered.
			else if (token.equals(")")) {
				while (!stack.isEmpty() && !stack.peek().equals("(")) {
					result.add(stack.pop());
				}

				if (!stack.isEmpty() && !stack.peek().equals("("))
					return null;// "Invalid Expression"; // invalid expression
				else
					stack.pop();
			} else // an operator is encountered
			{
				while (!stack.isEmpty()) {
					int precOp = Operators.OPERATOR_PRECEDENCE.get(token);
					// pop operator till precedence is high at the top of stack
					int stackPeekPrec = Operators.OPERATOR_PRECEDENCE.get(stack.peek()) == null ? -1
							: Operators.OPERATOR_PRECEDENCE.get(stack.peek());
					if (precOp < stackPeekPrec) {
						result.add(stack.pop());

					} else {
						break;
					}
				}
				stack.push(token);

			}

		}

		// pop all the operators from the stack
		while (!stack.isEmpty()) {
			result.add(stack.pop());
		}

		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Expression parsePreOrder(Node<String> root, Expression expression) {
		if (root == null) {
			return null;
		}
		String data = root.getData();
		expression = ExpressionFactory.getExpression(data);
		if (expression instanceof IBinaryExpression) {
			((IBinaryExpression) expression)
					.setExpressionA(parsePreOrder(root.getLeft(), ((IBinaryExpression) expression).getExpressionA()));
			((IBinaryExpression) expression)
					.setExpressionB(parsePreOrder(root.getRight(), ((IBinaryExpression) expression).getExpressionB()));
		} else if (expression instanceof IUnaryExpression) {
			((IUnaryExpression) expression)
					.setExpression(parsePreOrder(root.getLeft(), ((IUnaryExpression) expression).getExpression()));
		}
		return expression;
	}
}
