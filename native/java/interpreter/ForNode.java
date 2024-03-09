import java.util.ArrayList;

public class ForNode extends StatementNode{
	
	public ForNode(Node from, Node to, ArrayList<StatementNode> statements, String identifier) {
		this.setFrom(from);
		this.setTo(to);
		this.setStatements(statements);
		this.setIdentifier(identifier);
	}
	private ArrayList<StatementNode> statements;
	private Node from;
	private Node to;
	private String identifier;
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public Node getFrom() {
		return from;
	}
	public void setFrom(Node from) {
		this.from = from;
	}
	public Node getTo() {
		return to;
	}
	public void setTo(Node to) {
		this.to = to;
	}
	
	public ArrayList<StatementNode> getStatements(){
		return statements;
	}
	
	public void setStatements(ArrayList<StatementNode> statements) {
		this.statements = statements;
	}
	
	public String toString() {
		String returnString = "";
		returnString+="\nForNode("+identifier+" from: " + from + ", to: " + to + "\nfor statements: " + statements + ")";
		return returnString;
	}
	

}