public class BooleanNode extends Node{
	//node containing a boolean value, stored inside a variableNode
	public BooleanNode(boolean bool) {
		this.setBool(bool);
	}
	
	private boolean bool;
	public String toString() {
		return bool+"";
	}
	public boolean isBool() {
		return bool;
	}
	public void setBool(boolean bool) {
		this.bool = bool;
	}

}