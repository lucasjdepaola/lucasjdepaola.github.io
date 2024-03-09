public class VariableReferenceNode extends Node {
	
	
	public VariableReferenceNode(String name, Node optionalNode) {
		this.setName(name);
		this.setOptionalNode(optionalNode);
		
	}
	
	public VariableReferenceNode(String name) {
		this.setName(name);
	}
	private String name;
	private Node optionalNode;
	
	public String toString() {
		String returnString = "";
		returnString += "VariableReferenceNode(name: " + name;
		if(optionalNode != null) returnString += ", array index value : " +optionalNode;
		returnString += ")";
		return returnString;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Node getOptionalNode() {
		return optionalNode;
	}

	public void setOptionalNode(Node optionalNode) {
		this.optionalNode = optionalNode;
	}
}