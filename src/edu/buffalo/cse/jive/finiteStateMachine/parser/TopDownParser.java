package edu.buffalo.cse.jive.finiteStateMachine.parser;

import java.util.ArrayList;
import java.util.List;

import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.ListOperations.Append;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.ListOperations.InExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.ListOperations.ListEqualityExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.arithmetic.AdditionExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.arithmetic.DivisionExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.arithmetic.MultiplicationExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.arithmetic.SubtractionExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.core.ImplicationExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.core.ListMaxExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.core.ListMinExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.core.ListPrimeMaxExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.core.ListPrimeMinExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.core.ListPrimeSizeExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.core.ListSizeExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.core.PrimeSubscriptExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.core.PrimeVariableExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.core.SubscriptExpression;
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
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.temporal.PathExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.temporal.UExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.temporal.XExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.DoubleValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.IntegerValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.ListValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.StringValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.ValueExpression;
import edu.buffalo.cse.jive.finiteStateMachine.util.TemporaryDataTransporter;

/**
 * @author Bharat Jayaraman
 * @email bharat@buffalo.edu
 * 
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 * 
 * @author Saurabh Pansare
 * @email sppansar@buffalo.edu
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
			TemporaryDataTransporter.shouldHighlight = true;
			break;
		case Token.P_OP: // P is the path operator
			lexer.lex(); // skip over 'P'
			if (lexer.getNextToken() != Token.LEFT_BOX)
				throw new IllegalArgumentException("Syntax Error in Properties");
			lexer.lex(); // skip over [
			e = new Imply(lexer);
			if (lexer.getNextToken() == Token.LEADS_TO)  {
				lexer.lex();
				Imply e2 = new Imply(lexer);
				if (e2.getExpression() == null || e.getExpression() == null)
					throw new IllegalArgumentException("Syntax Error in Properties");
				if (lexer.getNextToken() == Token.LEADS_TO) {
					lexer.lex();
					Imply e3 = new Imply(lexer);
					if (e3.getExpression() == null)
						throw new IllegalArgumentException("Syntax Error in Properties");
					else
						expression = new EExpression(new AndExpression(e.getExpression(), 
																	   new EExpression(new AndExpression(e2.getExpression(), new EExpression(e3.getExpression())))));
				}
				else expression = new EExpression(new AndExpression(e.getExpression(), new EExpression(e2.getExpression())));
				     // p ~> q is equivalent to E [p && E[q] ]
				     // p ~> q ~> r is eqvt to E [p && E[q && E[r]]]
			}
			else {
				//  We must have lexer.getNextToken() == Token.ALWAYS_LEADS_TO)
				
				lexer.lex();
				Imply e2 = new Imply(lexer);
				if (e2.getExpression() == null || e.getExpression() == null)
					throw new IllegalArgumentException("Syntax Error in Properties");
				if (lexer.getNextToken() == Token.ALWAYS_LEADS_TO) {
					lexer.lex();
					Imply e3 = new Imply(lexer);
					if (e3.getExpression() == null)
						throw new IllegalArgumentException("Syntax Error in Properties");
					else
						expression = new PathExpression(e.getExpression(), e2.getExpression(), e3.getExpression());
				}
				else expression = new FExpression(new AndExpression(e.getExpression(), new FExpression(e2.getExpression())));
		    }
			
			if (lexer.getNextToken() != Token.RIGHT_BOX)
				throw new IllegalArgumentException("Syntax Error in Properties");
			lexer.lex(); // skip over ]
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

class Rel { // relexp -> expr ('<' | '>' | '<=' | '>=' | '==' | '!= ' | 'in')

	private Expression expression;

	public Rel(Lexer lexer) throws Exception {
		Expr e1;
		Expr e2;
		ListExpr l;
		e1 = new Expr(lexer, false);
		if (lexer.getNextToken() == Token.EQ_OP || lexer.getNextToken() == Token.GREATER_OP
				|| lexer.getNextToken() == Token.LESSER_OP || lexer.getNextToken() == Token.GREATEREQ_OP
				|| lexer.getNextToken() == Token.LESSEQ_OP || lexer.getNextToken() == Token.NOT_EQ
				|| lexer.getNextToken() == Token.IN_OP) {
			int op = lexer.getNextToken();
			lexer.lex();
			if (op == Token.IN_OP) {
				if (lexer.getNextToken() == Token.LEFT_BOX) {
					l = new ListExpr(lexer, null);
					if (e1.getExpression() == null || l.getExpression() == null) {
						throw new IllegalArgumentException("Syntax Error in Properties");
					}
					expression = new InExpression((ValueExpression) e1.getExpression(),
							(ListValueExpression) l.getExpression());
				} else {
					e2 = new Expr(lexer, true);
					if (e1.getExpression() == null || e2.getExpression() == null) {
						throw new IllegalArgumentException("Syntax Error in Properties");
					}
					if (e2.getExpression() instanceof ValueExpression) {
						throw new IllegalArgumentException("In operator cannot have string literals");
					}
					expression = new InExpression((ValueExpression) e1.getExpression(),
							(ListValueExpression) e2.getExpression());
				}
			} else {
				e2 = new Expr(lexer, false);
				if (e1.getExpression() == null || e2.getExpression() == null)
					throw new IllegalArgumentException("Syntax Error in Properties");

				switch (op) {
				case Token.EQ_OP:
					if (e1.getExpression() instanceof ValueExpression) {
						expression = new EqualityExpression((ValueExpression) e1.getExpression(),
								(ValueExpression) e2.getExpression());
					} else {
						expression = new ListEqualityExpression((ListValueExpression) e1.getExpression(),
								(ListValueExpression) e2.getExpression());
					}
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

	public Expr(Lexer lexer, Boolean listIdFlag) throws Exception {
		Term t;
		Expr e;
		t = new Term(lexer, listIdFlag);
		expression = t.getExpression();
		if (lexer.getNextToken() == Token.ADD_OP || lexer.getNextToken() == Token.SUB_OP) {
			int op = lexer.getNextToken();
			lexer.lex();
			e = new Expr(lexer, false);
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

	public Term(Lexer lexer, Boolean listIdFlag) throws Exception {
		Factor f;
		Term t;
		f = new Factor(lexer, listIdFlag);
		expression = f.getExpression();
		if (lexer.getNextToken() == Token.MULT_OP || lexer.getNextToken() == Token.DIV_OP) {
			int op = lexer.getNextToken();
			lexer.lex();
			t = new Term(lexer, listIdFlag);
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

class Factor { // factor -> int_ | id | '(' expr ')' ************ list literal [ while ]

	private Expression expression;

	public Factor(Lexer lexer, Boolean listIdFlag) throws Exception {
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
		case Token.ID:
			id = lexer.getIdent();
			lexer.lex();
			if (lexer.getNextToken() == Token.PRIME_OP) {
				lexer.lex();
				

				if (lexer.getNextToken() == Token.LEFT_BOX) {

					lexer.lex();
					e = new Expr(lexer, false);
					if (lexer.getNextToken() != Token.RIGHT_BOX) {
						throw new IllegalArgumentException("Syntax Error in Properties");
					}
					lexer.lex(); // skip over ')'
					expression = new PrimeSubscriptExpression(id, (ValueExpression) e.getExpression());

				} else if (lexer.getNextToken() == Token.HASH_OP) {

					if (listIdFlag) {
						throw new IllegalArgumentException("Syntax Error in Properties");
					} else {
						lexer.lex();
						switch (lexer.getNextToken()) {

						case Token.SIZE_OP:
							lexer.lex();
							expression = new ListPrimeSizeExpression(id);
							break;
						case Token.MIN_OP:
							lexer.lex();
							expression = new ListPrimeMinExpression(id);
							break;
						case Token.MAX_OP:
							lexer.lex();
							expression = new ListPrimeMaxExpression(id);
							break;
						default:
							throw new IllegalArgumentException("Syntax Error in Properties");

						}

					}

				} else if (listIdFlag) {
					expression = new ListValueExpression(id, listIdFlag, true);
					if (lexer.getNextToken() == Token.APPEND_OP) {
						lexer.lex();
						e = new Expr(lexer, listIdFlag);
						expression = new Append((ListValueExpression) expression,
								(ListValueExpression) e.getExpression());
					}

				}
				else {
					expression = new PrimeVariableExpression(id, null);
				}

			} else if (lexer.getNextToken() == Token.LEFT_BOX) {
				if (listIdFlag) {
					throw new IllegalArgumentException("Syntax Error in Properties");
				} else {
					lexer.lex();
					e = new Expr(lexer, false);
					if (lexer.getNextToken() != Token.RIGHT_BOX) {
						throw new IllegalArgumentException("Syntax Error in Properties");
					}
					lexer.lex();

					expression = new SubscriptExpression(id, (ValueExpression) e.getExpression());
				}
			}

			else if (lexer.getNextToken() == Token.HASH_OP) {

				if (listIdFlag) {
					throw new IllegalArgumentException("Syntax Error in Properties");
				} else {
					lexer.lex();
					switch (lexer.getNextToken()) {

					case Token.SIZE_OP:
						lexer.lex();
						expression = new ListSizeExpression(id);
						break;
					case Token.MIN_OP:
						lexer.lex();
						expression = new ListMinExpression(id);
						break;
					case Token.MAX_OP:
						lexer.lex();
						expression = new ListMaxExpression(id);
						break;
					default:
						throw new IllegalArgumentException("Syntax Error in Properties");

					}

				}

			} else {
				if (listIdFlag) {
					expression = new ListValueExpression(id, listIdFlag , false);
					if (lexer.getNextToken() == Token.APPEND_OP) {
						lexer.lex();
						e = new Expr(lexer, listIdFlag);
						expression = new Append((ListValueExpression) expression,
								(ListValueExpression) e.getExpression());
					}

				} else {
					expression = new VariableExpression(id, null);
				}
			}
			break;
		case Token.STRING_LIT: // id
			id = lexer.getIdent();
			lexer.lex();
			expression = new StringValueExpression(id.substring(1, id.length() - 1));
			break;
		case Token.LEFT_PAREN: // '('
			lexer.lex();
			e = new Expr(lexer, false);
			if (lexer.getNextToken() != Token.RIGHT_PAREN)
				throw new IllegalArgumentException("Syntax Error in Properties");
			lexer.lex(); // skip over ')'
			expression = e.getExpression();
			break;
		case Token.LEFT_BOX:
			throw new IllegalArgumentException("Enclose the list inside double quotes");

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

class ListExpr { // List_literal [ ++ ListExp ]

	private Expression expression;
	List<Integer> intList;
	List<String> stringList;
	ListExpr l;
	String id;
	String type;

	public ListExpr(Lexer lexer, String type) throws Exception {

		if (lexer.getNextToken() != Token.LEFT_BOX) {
			throw new IllegalArgumentException("Syntax Error in Properties");
		} else {
			lexer.lex(); //
			this.type = type;
			if (type != null && ((type.equals("int")
					&& (lexer.getNextToken() == Token.STRING_LIT || lexer.getNextToken() == Token.ID))
					|| (type.equals("string")
							&& (lexer.getNextToken() == Token.INT_LIT || lexer.getNextToken() == Token.N_INT_LIT)))) {
				throw new IllegalArgumentException("Syntax Error in Properties");
			}
			if (lexer.getNextToken() == Token.INT_LIT || lexer.getNextToken() == Token.N_INT_LIT) {
				intList = new ArrayList<Integer>();
				while (lexer.getNextToken() != Token.RIGHT_BOX) {
					id = lexer.getIdent();
					intList.add(Integer.parseInt(id));
					lexer.lex();
				}
				lexer.lex();

				expression = new ListValueExpression(intList);
				if (lexer.getNextToken() == Token.APPEND_OP) {
					lexer.lex();
					l = new ListExpr(lexer, "int");
					// append class define here
					expression = new Append((ListValueExpression) expression, (ListValueExpression) l.getExpression());
				}

			} else {

				stringList = new ArrayList<String>();
				while (lexer.getNextToken() != Token.RIGHT_BOX) {
					id = lexer.getIdent();
					stringList.add(id);
					lexer.lex();
				}
				lexer.lex();
				expression = new ListValueExpression(stringList);
				if (lexer.getNextToken() == Token.APPEND_OP) {
					lexer.lex();
					l = new ListExpr(lexer, "string");
					// append class define here
					expression = new Append((ListValueExpression) expression, (ListValueExpression) l.getExpression());

				}

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
