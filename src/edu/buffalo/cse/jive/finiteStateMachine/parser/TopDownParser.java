package edu.buffalo.cse.jive.finiteStateMachine.parser;

import java.util.ArrayList;
import java.util.List;

import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.arithmetic.AdditionExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.arithmetic.DivisionExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.arithmetic.MultiplicationExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.arithmetic.SubtractionExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.core.ImplicationExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.core.PrimeVariableExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.core.VariableExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.Expression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.logical.AndExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.logical.NotExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.logical.OrExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.relational.EqualityExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.relational.GreaterThanEqualToExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.relational.GreaterThanExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.relational.LessThanEqualToExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.relational.LessThanExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.relational.NotEqualityExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.temporal.EExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.temporal.FExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.temporal.GExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.temporal.UExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.temporal.XExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.DoubleValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.IntegerValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.StringValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.ValueExpression;

/**
 * @author Bharat Jayaraman
 * @email bharat@buffalo.edu
 * 
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
/**
 * TopDownParser - Ask the Professor about this.
 *
 */
public class TopDownParser implements Parser {

	@Override
	public List<Expression> parse(String[] inputs) throws Exception {
		List<Expression> expressions = new ArrayList<Expression>();
		for (String input : inputs) {
			Expression expression = new ExpressionFactory(new Lexer(input + ";")).getExpression();
			if (!expression.isEvaluatable())
				throw new IllegalArgumentException("Syntax Error in Properties");
			expressions.add(expression);
		}
		return expressions;
	}
}

class ExpressionFactory {
	private Lexer lexer;
	private Expression expression;

	public ExpressionFactory(Lexer lexer) {
		super();
		this.lexer = lexer;
	}

	public Expression getExpression() throws Exception {
		lexer.lex();
		Imply imply = new Imply(lexer);
		expression = imply.getExpression();
		if (expression == null) {
			throw new IllegalArgumentException("Syntax Error in Properties");
		}
		return expression;
	}

	public Lexer getLexer() {
		return lexer;
	}

	public void setLexer(Lexer lexer) {
		this.lexer = lexer;
	}

}

class Imply {
	private Expression expression;

	public Imply(Lexer lexer) throws Exception {
		Or e1, e2;
		e1 = new Or(lexer);
		expression = e1.getExpression();
		if (lexer.getNextToken() == Token.IMPLY_OP) {
			lexer.lex();
			e2 = new Or(lexer);
			if (e1.getExpression() == null || e2.getExpression() == null)
				throw new IllegalArgumentException("Syntax Error in Properties");
			expression = new ImplicationExpression(e1.getExpression(), e2.getExpression());
		}
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}
}

class Or {
	private Expression expression;

	public Or(Lexer lexer) throws Exception {
		And t;
		Or e;
		t = new And(lexer);
		expression = t.getExpression();
		if (lexer.getNextToken() == Token.OR_OP) {
			lexer.lex();
			e = new Or(lexer);
			if (t.getExpression() == null || e.getExpression() == null)
				throw new IllegalArgumentException("Syntax Error in Properties");
			expression = new OrExpression(t.getExpression(), e.getExpression());
		}

	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

}

class And {
	private Expression expression;

	public And(Lexer lexer) throws Exception {
		BF f;
		And t;
		f = new BF(lexer);
		expression = f.getExpression();
		if (lexer.getNextToken() == Token.AND_OP) {
			lexer.lex();
			t = new And(lexer);
			if (f.getExpression() == null || t.getExpression() == null)
				throw new IllegalArgumentException("Syntax Error in Properties");
			expression = new AndExpression(f.getExpression(), t.getExpression());
		}
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}
}

class BF {
	private Expression expression;

	public BF(Lexer lexer) throws Exception {
		Imply e;
		Rel r;
		switch (lexer.getNextToken()) {
		case Token.INT_LIT: // number
			r = new Rel(lexer);
			expression = r.getExpression();
			break;
		case Token.ID: // id
			r = new Rel(lexer);
			expression = r.getExpression();
			break;
		case Token.LEFT_PAREN: // '('
			lexer.lex();
			e = new Imply(lexer);
			if (lexer.getNextToken() != Token.RIGHT_PAREN)
				throw new IllegalArgumentException("Syntax Error in Properties");
			lexer.lex(); // skip over ')'
			expression = e.getExpression();
			break;
		case Token.NOT_OP:
			lexer.lex(); // !
			lexer.lex(); // '('
			e = new Imply(lexer);
			lexer.lex(); // skip over ')'
			expression = new NotExpression(e.getExpression());
			break;
		case Token.F_OP: // F
			lexer.lex(); // skip over 'F'
			if (lexer.getNextToken() != Token.LEFT_BOX)
				throw new IllegalArgumentException("Syntax Error in Properties");
			lexer.lex(); // skip over [
			e = new Imply(lexer);
			if (lexer.getNextToken() != Token.RIGHT_BOX)
				throw new IllegalArgumentException("Syntax Error in Properties");
			lexer.lex(); // skip over ]
			expression = new FExpression(e.getExpression());
			break;
		case Token.G_OP: // G
			lexer.lex(); // skip over 'G'
			if (lexer.getNextToken() != Token.LEFT_BOX)
				throw new IllegalArgumentException("Syntax Error in Properties");
			lexer.lex(); // skip over [
			e = new Imply(lexer);
			if (lexer.getNextToken() != Token.RIGHT_BOX)
				throw new IllegalArgumentException("Syntax Error in Properties");
			lexer.lex(); // skip over ]
			expression = new GExpression(e.getExpression());
			break;
		case Token.X_OP: // X
			lexer.lex(); // skip over 'X'
			if (lexer.getNextToken() != Token.LEFT_BOX)
				throw new IllegalArgumentException("Syntax Error in Properties");
			lexer.lex(); // skip over [
			e = new Imply(lexer);
			if (lexer.getNextToken() != Token.RIGHT_BOX)
				throw new IllegalArgumentException("Syntax Error in Properties");
			lexer.lex(); // skip over ]
			expression = new XExpression(e.getExpression());
			break;
		case Token.U_OP: // X
			lexer.lex(); // skip over 'X'
			if (lexer.getNextToken() != Token.LEFT_BOX)
				throw new IllegalArgumentException("Syntax Error in Properties");
			lexer.lex(); // skip over [
			e = new Imply(lexer);
			if (lexer.getNextToken() != Token.RIGHT_BOX)
				throw new IllegalArgumentException("Syntax Error in Properties");
			lexer.lex(); // skip over ]
			expression = new UExpression(e.getExpression());
			break;
		case Token.E_OP: // X
			lexer.lex(); // skip over 'X'
			if (lexer.getNextToken() != Token.LEFT_BOX)
				throw new IllegalArgumentException("Syntax Error in Properties");
			lexer.lex(); // skip over [
			e = new Imply(lexer);
			if (lexer.getNextToken() != Token.RIGHT_BOX)
				throw new IllegalArgumentException("Syntax Error in Properties");
			lexer.lex(); // skip over ]
			expression = new EExpression(e.getExpression());
			break;
		default:
			throw new IllegalArgumentException("Syntax Error in Properties");
		}
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}
}

class Rel { // relexp -> expr ('<' | '>' | '<=' | '>=' | '==' | '!= ') expr

	private Expression expression;

	public Rel(Lexer lexer) throws Exception {
		Expr e1;
		Expr e2;
		e1 = new Expr(lexer);
		if (lexer.getNextToken() == Token.EQ_OP || lexer.getNextToken() == Token.GREATER_OP
				|| lexer.getNextToken() == Token.LESSER_OP || lexer.getNextToken() == Token.GREATEREQ_OP
				|| lexer.getNextToken() == Token.LESSEQ_OP || lexer.getNextToken() == Token.NOT_EQ) {
			int op = lexer.getNextToken();
			lexer.lex();
			e2 = new Expr(lexer);
			if (e1.getExpression() == null || e2.getExpression() == null)
				throw new IllegalArgumentException("Syntax Error in Properties");
			switch (op) {
			case Token.EQ_OP:
				expression = new EqualityExpression((ValueExpression) e1.getExpression(),
						(ValueExpression) e2.getExpression());
				break;
			case Token.GREATEREQ_OP:
				expression = new GreaterThanEqualToExpression((ValueExpression) e1.getExpression(),
						(ValueExpression) e2.getExpression());
				break;
			case Token.GREATER_OP:
				expression = new GreaterThanExpression((ValueExpression) e1.getExpression(),
						(ValueExpression) e2.getExpression());
				break;
			case Token.NOT_EQ:
				expression = new NotEqualityExpression((ValueExpression) e1.getExpression(),
						(ValueExpression) e2.getExpression());
				break;
			case Token.LESSER_OP:
				expression = new LessThanExpression((ValueExpression) e1.getExpression(),
						(ValueExpression) e2.getExpression());
				break;
			case Token.LESSEQ_OP:
				expression = new LessThanEqualToExpression((ValueExpression) e1.getExpression(),
						(ValueExpression) e2.getExpression());
				break;
			default:
				break;
			}
		}
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

}

class Expr { // expr -> term (+ | -) expr | term

	private Expression expression;

	public Expr(Lexer lexer) throws Exception {
		Term t;
		Expr e;
		t = new Term(lexer);
		expression = t.getExpression();
		if (lexer.getNextToken() == Token.ADD_OP || lexer.getNextToken() == Token.SUB_OP) {
			int op = lexer.getNextToken();
			lexer.lex();
			e = new Expr(lexer);
			if (t.getExpression() == null || e.getExpression() == null)
				throw new IllegalArgumentException("Syntax Error in Properties");
			switch (op) {
			case Token.ADD_OP:
				expression = new AdditionExpression((ValueExpression) e.getExpression(),
						(ValueExpression) t.getExpression());
				break;
			case Token.SUB_OP:
				expression = new SubtractionExpression((ValueExpression) e.getExpression(),
						(ValueExpression) t.getExpression());
				break;
			default:
				break;
			}
		}
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}
}

class Term { // term -> factor (* | /) term | factor

	private Expression expression;

	public Term(Lexer lexer) throws Exception {
		Factor f;
		Term t;
		f = new Factor(lexer);
		expression = f.getExpression();
		if (lexer.getNextToken() == Token.MULT_OP || lexer.getNextToken() == Token.DIV_OP) {
			int op = lexer.getNextToken();
			lexer.lex();
			t = new Term(lexer);
			if (t.getExpression() == null || f.getExpression() == null)
				throw new IllegalArgumentException("Syntax Error in Properties");
			switch (op) {
			case Token.MULT_OP:
				expression = new MultiplicationExpression((ValueExpression) f.getExpression(),
						(ValueExpression) t.getExpression());
				break;
			case Token.DIV_OP:
				expression = new DivisionExpression((ValueExpression) f.getExpression(),
						(ValueExpression) t.getExpression());
				break;
			default:
				break;
			}
		}
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

}

class Factor { // factor -> int_ | id | '(' expr ')'

	private Expression expression;

	public Factor(Lexer lexer) throws Exception {
		Expr e;
		String id;
		String i;
		switch (lexer.getNextToken()) {
		case Token.INT_LIT: // number
			i = lexer.getIdent();
			lexer.lex();
			try {
				expression = new IntegerValueExpression(Integer.parseInt(i));
			} catch (NumberFormatException e2) {
				expression = new DoubleValueExpression(Double.parseDouble(i));
			}
			break;
		case Token.N_INT_LIT: // negative number
			i = lexer.getIdent();
			lexer.lex();
			try {
				expression = new IntegerValueExpression(Integer.parseInt("-" + i));
			} catch (NumberFormatException e2) {
				expression = new DoubleValueExpression(Double.parseDouble("-" + i));
			}
			break;
		case Token.ID: // id
			id = lexer.getIdent();
			lexer.lex();
			if (lexer.getNextToken() == Token.PRIME_OP) {
				lexer.lex();
				expression = new PrimeVariableExpression(id, null);
			} else {
				expression = new VariableExpression(id, null);
			}
			break;
		case Token.STRING_LIT: // id
			id = lexer.getIdent();
			lexer.lex();
			expression = new StringValueExpression(id.substring(1, id.length() - 1));
			break;
		case Token.LEFT_PAREN: // '('
			lexer.lex();
			e = new Expr(lexer);
			if (lexer.getNextToken() != Token.RIGHT_PAREN)
				throw new IllegalArgumentException("Syntax Error in Properties");
			lexer.lex(); // skip over ')'
			expression = e.getExpression();
			break;
		default:
			break;
		}
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}
}
