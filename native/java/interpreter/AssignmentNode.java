public class AssignmentNode extends StatementNode {
	public AssignmentNode(VariableReferenceNode target, Node value) {
		this.setTarget(target);
		this.setValue(value);
	}
	private VariableReferenceNode target;
	private Node value;
	public String toString() {
		String returnValue = "";
		returnValue += "AssignmentNode(target: " + target + " value: " + value + ")"; 
		return returnValue;
	}
	public VariableReferenceNode getTarget() {
		return target;
	}
	public void setTarget(VariableReferenceNode target) {
		this.target = target;
	}
	public Node getValue() {
		return value;
	}
	public void setValue(Node value) {
		this.value = value;
	}
}