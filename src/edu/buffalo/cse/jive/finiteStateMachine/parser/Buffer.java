package edu.buffalo.cse.jive.finiteStateMachine.parser;

import java.util.Scanner;

public class Buffer {

	private String line = "";
	private int position = 0;
	private Scanner s;

	public Buffer(Scanner s) {
		this.s = s;
		line = s.nextLine();
		line = line + "\n";
	}

	public char getChar() {
		if (position == line.length()) {
			line = s.nextLine();
			if (line == null)
				System.exit(0);
			position = 0;
			line = line + "\n";
		}
		position++;
		return line.charAt(position - 1);
	}

}
