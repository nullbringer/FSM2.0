package edu.buffalo.cse.jive.finiteStateMachine.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public class Tokenizer {

	public static Set<String> idSet = new HashSet<>();

	public static List<String> tokenize(String input) throws IOException {
		idSet.clear();
		ArrayList<String> output = new ArrayList<>();
		Lexer lexer = new Lexer(new StringReader(input + ";"));
		lexer.lex();
		int nextToken = -1;
		while ((nextToken = lexer.getNextToken()) != 0) {
			if (nextToken == Token.ID) {
				output.add(lexer.getIdent());
				idSet.add(lexer.getIdent());
			} else if (nextToken == Token.INT_LIT)
				output.add(lexer.getIdent());
			else if (nextToken == Token.N_INT_LIT) {
				output.add("-" + lexer.getIdent());
			} else if (nextToken == Token.STRING_LIT)
				output.add(lexer.getIdent());
			else
				output.add(Operators.getOperator(nextToken));
			lexer.lex();
		}
		return output;
	}
}
