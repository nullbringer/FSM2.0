package edu.buffalo.cse.jive.finiteStateMachine.parser;

import java.util.List;

import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.Expression;

class TopDownParserTest {

	public static void main(String[] args) {
		TopDownParser parser = new TopDownParser();
		String exp = "G[(( r > 0 -> w == 0) && \n (w == 1 -> r == 0)) && \n ((w == 0 || w == 1) && \n (r > 0 && ww > 0 -> r' <= r))]";
		String exp2 = "G[ f == 1";
		try {
			List<Expression> expressions = parser.parse(new String[] { exp, exp2 });
			expressions.get(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
