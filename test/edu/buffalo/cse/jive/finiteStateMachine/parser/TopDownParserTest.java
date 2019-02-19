package edu.buffalo.cse.jive.finiteStateMachine.parser;

import java.util.List;

import edu.buffalo.cse.jive.finiteStateMachine.expression.expression.Expression;

class TopDownParserTest {

	public static void main(String[] args) {
		TopDownParser parser = new TopDownParser();
		
		try {
			List<Expression> expressions = parser.parse(new String[] { "G[ w == w' + \"5\" -> r == 1 ];", "G[ w == w' + \"5\" -> r == 1 ];" });
			expressions.get(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
