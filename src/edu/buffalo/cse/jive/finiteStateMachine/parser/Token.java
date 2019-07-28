package edu.buffalo.cse.jive.finiteStateMachine.parser;

/**
 * @author Bharat Jayaraman
 * @email bharat@buffalo.edu
 * 
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 */
class Token {
	public static final int SEMICOLON = 0;
	public static final int COMMA = 1;
	public static final int ADD_OP = 2;
	public static final int SUB_OP = 3;
	public static final int MULT_OP = 4;
	public static final int DIV_OP = 5;
	public static final int ASSIGN_OP = 6;
	public static final int GREATER_OP = 7;
	public static final int LESSER_OP = 8;
	public static final int LESSEQ_OP = 9;
	public static final int GREATEREQ_OP = 10;
	public static final int EQ_OP = 11;
	public static final int NOT_EQ = 12;
	public static final int LEFT_PAREN = 13;
	public static final int RIGHT_PAREN = 14;
	public static final int LEFT_BRACE = 15;
	public static final int RIGHT_BRACE = 16;
	public static final int ID = 17;
	public static final int INT_LIT = 18;
	public static final int AND_OP = 19;
	public static final int OR_OP = 20;
	public static final int IMPLY_OP = 21;
	public static final int F_OP = 22;
	public static final int G_OP = 23;
	public static final int U_OP = 24;
	public static final int X_OP = 25;
	public static final int LEFT_BOX = 26;
	public static final int RIGHT_BOX = 27;
	public static final int NOT_OP = 28;
	public static final int KEY_END = 29;
	public static final int PRIME_OP = 30;
	public static final int N_INT_LIT = 31;
	public static final int STRING_LIT = 32;
	public static final int E_OP = 33;
	public static final int IN_OP = 34;
	public static final int DOT_OP = 35;
	public static final int APPEND_OP=36;
	public static final int SIZE_OP=37;
	public static final int MIN_OP=38;
	public static final int MAX_OP=39;
	public static final int HASH_OP=40;
	public static final int LEADS_TO = 41;
	public static final int ALWAYS_LEADS_TO = 42;
	public static final int P_OP = 43;
	

	private static String[] lexemes = { ";", ",", "+", "-", "*", "/", "=", ">", "<", "<=", ">=", "==", "!=", "(", ")",
			"{", "}", "id", "int_lit", "&&", "||", "->", "F", "G", "U", "X", "[", "]", "!", "end", "'",
			"n_int_lit", "string_lit", "E", "in", ".", "++", "size", "min", "max", "hash", "~>", "~~>", "P"};

	public static String toString(int i) {
		if (i < 0 || i > 43)
			return "";
		else
			return lexemes[i];
	}
}
