package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.ListOperations;

import java.util.List;

import edu.buffalo.cse.jive.finiteStateMachine.models.Context;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value.ListValueExpression;


public class Append extends ListValueExpression {
	ListValueExpression list1;
	ListValueExpression list2;
	public Append(ListValueExpression list1, ListValueExpression list2) {
		super();
		this.list1 = list1;
		this.list2 = list2;
	}

	@Override
	public Boolean evaluate(Context context) {
		getList1().evaluate(context);
		getList2().evaluate(context);
		return true;
	}
	
	public ListValueExpression getList1() {
		return list1;
	}


	public void setList1(ListValueExpression list1) {
		this.list1 = list1;
	}


	public ListValueExpression getList2() {
		return list2;
	}


	public void setList2(ListValueExpression list2) {
		this.list2 = list2;
	}


	public List<?> getListValue() {
		return getList1().appendList(getList2());
	}

}
