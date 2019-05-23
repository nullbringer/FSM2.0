package edu.buffalo.cse.jive.finiteStateMachine.parser.expression.value;

import java.util.ArrayList;
import java.util.List;

import edu.buffalo.cse.jive.finiteStateMachine.models.Context;
import edu.buffalo.cse.jive.finiteStateMachine.parser.expression.expression.Expression;

public class ListValueExpression extends Expression implements Comparable<ListValueExpression>{

	List<?> listValue = new ArrayList<>();
	String id;
	boolean isListVar;

	public ListValueExpression(List<?> listValue) {
		this.listValue = listValue;
	}

	public ListValueExpression() {
	}

	public ListValueExpression(String id, boolean isListVar) {
		this.id = id;
		this.isListVar = isListVar;
	}

	public List<?> getListValue() {
		return listValue;
	}

	public void setListValue(List<?> listValue) {
		this.listValue = listValue;
	}

	public Boolean in(ValueExpression valueExpression) {

		Object value = valueExpression.getValue();
		if (value.getClass().getSimpleName().equals("String")) {
			return this.getListValue().contains((String) value);
		} else {
			return this.getListValue().contains((Integer) value);
		}
	}

	@SuppressWarnings("unchecked")
	public List<?> appendList(ListValueExpression list) {
		List<?> listValue1 = this.getListValue();
		List<?> listValue2 = list.getListValue();
		if (listValue1.isEmpty()) {
			return listValue2;
		} else if (listValue2.isEmpty()) {
			return listValue1;
		} else if (listValue1.get(0).getClass().getSimpleName().equals("String")) {
			List<String> stringListValue1 = (List<String>) listValue1;
			List<String> stringListValue2 = (List<String>) listValue2;

			for (int i = 0; i < stringListValue2.size(); i++) {
				stringListValue1.add(stringListValue2.get(i));
			}
			return stringListValue1;
		} else {
			List<Integer> intListValue1 = (List<Integer>) listValue1;
			List<Integer> intListValue2 = (List<Integer>) listValue2;

			for (int i = 0; i < intListValue2.size(); i++) {
				intListValue1.add(intListValue2.get(i));
			}
			return intListValue1;
		}

	}

	@Override
	public Boolean evaluate(Context context) {
		// TODO Auto-generated method stub
		if (context.getCurrentState().getVector().get(id) != null) {
			List<Integer> intList = new ArrayList<>();
			List<String> stringList = new ArrayList<>();
			String variableValue = (String) context.getCurrentState().getVector().get(id).getValue();
			if (variableValue.charAt(0) == '[') {
				String[] listValue = variableValue.split(" |\\]|\\[");
				for (int i = 0; i < listValue.length; i++) {
					if (!(listValue[i].equals("") || listValue[i].equals("[") || listValue[i].equals("]"))) {
						if (listValue[i].charAt(0) == '-' || listValue[i].charAt(0) == '0'
								|| listValue[i].charAt(0) == '1' || listValue[i].charAt(0) == '2'
								|| listValue[i].charAt(0) == '3' || listValue[i].charAt(0) == '4'
								|| listValue[i].charAt(0) == '5' || listValue[i].charAt(0) == '6'
								|| listValue[i].charAt(0) == '7' || listValue[i].charAt(0) == '8'
								|| listValue[i].charAt(0) == '9') {
							intList.add(Integer.parseInt(listValue[i]));
						} else {
							stringList.add(listValue[i]);
						}

					}
				}
				if (intList.size() > stringList.size()) {
					this.listValue = intList;
				} else {
					this.listValue = stringList;
				}
			}
		}

		return true;
	}

	@Override
	public int compareTo(ListValueExpression listValueExpression) {
		if(this.listValue.hashCode() == listValueExpression.hashCode()) {
			return 0;
		}
		else if(this.listValue.hashCode() > listValueExpression.hashCode()) {
			return 1;
		}
		else {
			return -1;
		}
		
	}

}
