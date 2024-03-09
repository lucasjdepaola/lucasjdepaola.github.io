public class IntegerNode extends Node {
	public IntegerNode(int integerMember) {
		this.setInteger(integerMember);
	}
	
	public void setInteger(int integerMember) {
		this.integerMember = integerMember;
	}
	
	public int getInteger() {
		return integerMember;
	}
	private int integerMember;
	
	public String toString() {
		return "IntegerNode("+integerMember + ")"; // returns int member
	}
}