package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.edge;

import java.util.ArrayList;
import java.util.List;

import edu.buffalo.cse.jive.finiteStateMachine.models.Context;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.Expression;
/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public class VectorExpression extends Expression {

	private List<Object> vectorValue;

	public VectorExpression(String vectorString) {
		int len = vectorString.length();
		vectorString = vectorString.substring(1, len - 1);
		String[] token = vectorString.split(",");
		List<Object> vector = new ArrayList<>();
		for (String str : token) {
			try {
				Double d = Double.parseDouble(str);
				vector.add(d);
			} catch (Exception e) {
				vector.add(str);
			}
		}
		setVectorValue(vector);
	}

	public List<Object> getVectorValue() {
		return vectorValue;
	}

	public void setVectorValue(List<Object> vectorValue) {
		this.vectorValue = vectorValue;
	}

	@Override
	public Boolean evaluate(Context context) {
		return context.getCurrentState().getVector().size() == vectorValue.size();
	}
}
