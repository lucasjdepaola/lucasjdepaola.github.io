
public class MathOpNode extends Node {
	public MathOpNode(Node left, Node right) {
		this.left = left;
		this.right = right;
	}
	
	public MathOpNode(Node left) {
		this.left = left;
	}
	
	public MathOpNode(String value) {
		this.setValue(value);
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	public operators getOperator() {
		return operator;
	}

	public void setOperator(operators operator) {
		this.operator = operator;
	}
	public enum operators {
		PLUS, MINUS, TIMES, DIVIDE, MOD
	}
	private operators operator;
	Node left;
	Node right;
	private String value;
	
	public String operatorConversion(operators operator) {//converts the enum to a better read sign
		if(operator==operators.DIVIDE)
			return " / ";
		if(operator==operators.MINUS)
			return " - ";
		if(operator==operators.PLUS)
			return " + ";
		if(operator==operators.TIMES)
			return " * ";
		if(operator == operators.MOD)
			return " mod ";
		return null;
	}
	
	public String toString() {
		String value="";
		if(right==null) {
			value+=  left.toString();
			return value;
		}
		value += "MathOpNode(" +left.toString() + operatorConversion(operator) + right.toString() + ")";
		return value;
	}
	
	
}