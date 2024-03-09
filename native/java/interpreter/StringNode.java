public class StringNode extends Node {
	//node containing a string value, to put inside of a variable node
	public StringNode(String stringValue) {
		this.setStringValue(stringValue);
	}
	private String stringValue;
	
	public String toString() {
		return "StringNode("+stringValue+")";
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

}