public class VariableNode extends Node {
	//variable nodes hold the variable values, including constants and arrays
	public VariableNode(Node value, boolean changeable, String name, String type) {
		this.setValue(value);
		this.setChangeable(changeable);
		this.setName(name);
		this.setType(type);
	}
	private Node value;
	private boolean changeable;
	private String name;
	private String type;
	private int from;
	private int to;
	
	private float fromReal;
	private float toReal;
	public String toString() {
		String returnString = "";
		
		returnString += "VariableNode(Value: " + value + ", changeable: " + changeable + ", name: "+name+", Type: "+type+"";
		returnString+=to>1?", from: "+from+" to "+to:"";
		if(toReal>0.001) returnString+=", from: " + fromReal + " to " + toReal+"";
		returnString+=")";
		return returnString;
	}
	
	public float getToReal() {
		return toReal;
	}
	public void setToReal(float toReal) {
		this.toReal = toReal;
	}
	public float getFromReal() {
		return fromReal;
	}
	public void setFromReal(float fromReal) {
		this.fromReal = fromReal;
	}
	public Node getValue() {
		return value;
	}
	public void setValue(Node value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isChangeable() {
		return changeable;
	}
	public void setChangeable(boolean changeable) {
		this.changeable = changeable;
	}
	public int getFrom() {
		return from;
	}
	public void setFrom(int from) {
		this.from = from;
	}
	public int getTo() {
		return to;
	}
	public void setTo(int to) {
		this.to = to;
	}

}