import java.util.ArrayList;

public class RepeatNode extends StatementNode {
	public RepeatNode(BooleanCompareNode boolNode, ArrayList<StatementNode> statementNodes) {
		this.setBoolNode(boolNode);
		this.setStatementNodes(statementNodes);
	}
	private BooleanCompareNode boolNode;
	private ArrayList<StatementNode> statementNodes = new ArrayList<StatementNode>();
	public BooleanCompareNode getBoolNode() {
		return boolNode;
	}
	public void setBoolNode(BooleanCompareNode boolNode) {
		this.boolNode = boolNode;
	}
	public ArrayList<StatementNode> getStatementNodes() {
		return statementNodes;
	}
	public void setStatementNodes(ArrayList<StatementNode> statementNodes) {
		this.statementNodes = statementNodes;
	}
	
	public String toString() {
		String returnValue = "";
		returnValue +="RepeatNode(conditions: "  + boolNode + " repeat statements: " + statementNodes + ")";
		return returnValue;
	}
}