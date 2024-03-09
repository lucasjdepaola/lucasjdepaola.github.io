import java.util.ArrayList;

public class IfNode extends StatementNode {
	
	public IfNode(BooleanCompareNode boolNode, ArrayList<StatementNode> statementNodes) {
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
		return "IfNode(condition: " + boolNode + "\nif statements: " + statementNodes + ")";
	}
	
}