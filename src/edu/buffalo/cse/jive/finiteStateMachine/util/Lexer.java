package edu.buffalo.cse.jive.finiteStateMachine.util;

import java.io.IOException;
import java.io.StringReader;

/**
 * @author Bharat Jayaraman
 * @email bharat@buffalo.edu
 *
 */
public class Lexer {

	private char ch;
	private String ident;
	private int nextToken;
	private StringReader stringReader;

	public Lexer(StringReader stringReader) {
		ch = ' ';
		nextToken = 0;
		this.stringReader = stringReader;
	}

	private char read() throws IOException {
		return (char) stringReader.read();
	}

	public int getNextToken() {
		return nextToken;
	}

	private void setNextToken(int nextToken) {
		this.nextToken = nextToken;
	}

	public StringReader getStringReader() {
		return stringReader;
	}

	public void setStringReader(StringReader stringReader) {
		this.stringReader = stringReader;
	}

	private void ident() throws IOException {
		ident = "";
		do {
			ident = ident + ch;
			ch = read();
		} while (Character.isLetter(ch) || Character.isDigit(ch) || ch == '\'' || ch == '\"' || ch == '_' || ch == '.'
				|| ch == ':');
	}

	public void lex() throws IOException {
		while (Character.isWhitespace(ch))
			ch = read();
		if (Character.isLetter(ch) || ch == '_') {
			ident();
			switch (ident) {
			case "F":
				setNextToken(Token.F_OP);
				break;
			case "G":
				setNextToken(Token.G_OP);
				break;
			default:
				setNextToken(Token.ID);
			}
		} else if (Character.isDigit(ch)) {
			num();
			setNextToken(Token.INT_LIT);
		} else {
			switch (ch) {
			case ';':
				setNextToken(Token.SEMICOLON);
				ch = read();
				break;
			case ',':
				setNextToken(Token.COMMA);
				ch = read();
				break;
			case '+':
				setNextToken(Token.ADD_OP);
				ch = read();
				break;
			case '-':
				ch = read();
				if (Character.isDigit(ch)) {
					num();
					setNextToken(Token.N_INT_LIT);
				} else if (ch == '>') {
					setNextToken(Token.IMPLIES_OP);
					ch = read();
				} else {
					setNextToken(Token.SUB_OP);
				}
				break;
			case '*':
				setNextToken(Token.MULT_OP);
				ch = read();
				break;
			case '/':
				nextToken = Token.DIV_OP;
				ch = read();
				break;
			case '=':
				ch = read();
				if (ch == '=') {
					setNextToken(Token.EQ_OP);
					ch = read();
				} else if (ch == '>') {
					setNextToken(Token.EDGE_OP);
					ch = read();
				} else
					setNextToken(Token.ASSIGN_OP);
				break;
			case '<':
				ch = read();
				if (ch == '=') {
					setNextToken(Token.LESSEQ_OP);
					ch = read();
				} else
					setNextToken(Token.LESSER_OP);
				break;
			case '>':
				ch = read();
				if (ch == '=') {
					nextToken = Token.GREATEREQ_OP;
				} else
					nextToken = Token.GREATER_OP;
				ch = read();
				break;
			case '!':
				ch = read();
				if (ch == '=') {
					nextToken = Token.NOT_EQ;
				} else
					nextToken = Token.NOT_OP;
				ch = read();
				break;
			case '(':
				nextToken = Token.LEFT_PAREN;
				ch = read();
				break;
			case ')':
				nextToken = Token.RIGHT_PAREN;
				ch = read();
				break;
			case '{':
				nextToken = Token.LEFT_BRACE;
				ch = read();
				break;
			case '}':
				nextToken = Token.RIGHT_BRACE;
				ch = read();
				break;
			case '[':
				nextToken = Token.LEFT_SQ_BRACE;
				ch = read();
				break;
			case ']':
				nextToken = Token.RIGHT_SQ_BRACE;
				ch = read();
				break;
			case '&':
				ch = read();
				nextToken = Token.AND_OP;
				ch = read();
				break;
			case '|':
				ch = read();
				nextToken = Token.OR_OP;
				ch = read();
				break;
			case '\'':
				ident();
				setNextToken(Token.STRING_LIT);
				break;
			case '\"':
				ident();
				setNextToken(Token.STRING_LIT);
				break;
			default:
				break;
			}
		}
	}

	public String getIdent() {
		return ident;
	}

	private void num() throws IOException {
		ident = "";
		do {
			ident = ident + ch;
			ch = read();
		} while (Character.isDigit(ch) || ch == '.');
	}
}
