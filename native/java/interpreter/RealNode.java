public class RealNode extends Node {
	//node containing a float value, node will be stored inside a variable node
	public RealNode(float floatMember) {
		this.setFloatMember(floatMember);
	}
	
	private float floatMember;
	public float getFloatMember() {
		return floatMember;
	}
	
	public void setFloatMember(float floatMember) {
		this.floatMember = floatMember;
	}

	public String toString() {
		return "RealNode("+floatMember + ")";// could not work make sure to go back to this
	}
}