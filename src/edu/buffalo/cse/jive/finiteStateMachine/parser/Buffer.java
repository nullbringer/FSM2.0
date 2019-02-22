package edu.buffalo.cse.jive.finiteStateMachine.parser;

/**
 * @author Bharat Jayaraman
 * @email bharat@buffalo.edu
 * 
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 */
public class Buffer {

	private String line = "";
	private int position = 0;

	public Buffer(String s) {
		line = s;
		line = line + "\n";
	}

	public char getChar() throws Exception {
		if (position == line.length()) {
			if (line == null)
				throw new IllegalArgumentException("Syntax error in properties");
			position = 0;
			line = line + "\n";
		}
		position++;
		return line.charAt(position - 1);
	}

}
