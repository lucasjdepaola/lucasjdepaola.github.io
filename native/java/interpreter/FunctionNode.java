import java.util.ArrayList;

public class FunctionNode extends Node {
	private ArrayList<VariableNode> parameterCollection = new ArrayList<VariableNode>();
	private ArrayList<VariableNode> variableCollection = new ArrayList<VariableNode>();
	private ArrayList<StatementNode> statements = new ArrayList<StatementNode>();
	public FunctionNode(String functionName, ArrayList<VariableNode> parameterCollection,ArrayList<VariableNode> variableCollection, ArrayList<StatementNode> statements) {
		this.setStatements(statements);
		this.setParameterCollection(parameterCollection);
		this.setfunctionName(functionName);
		this.setVariableCollection(variableCollection);
	}
	
	public FunctionNode() {
		
	}
	
	public boolean isVariadic() {
		return parameterCollection!=null;
	}
	
	private String functionName;
	
	public String toString() {
		String returnValue = "";
		returnValue+="Function name: " + functionName+"\nParameter Variables: "+parameterCollection+"\nVariable Declarations: "+variableCollection+"\nStatements: "+statements;
		return returnValue;
	}
	
	public ArrayList<StatementNode> getStatements(){
		return statements;
	}

	public void setStatements(ArrayList<StatementNode> statements) {
		this.statements = statements;
	}
	
	public String getfunctionName() {
		return functionName;
	}

	public void setfunctionName(String functionName) {
		this.functionName = functionName;
	}

	public ArrayList<VariableNode> getVariableCollection() {
		return variableCollection;
	}

	public void setVariableCollection(ArrayList<VariableNode> variableCollection) {
		this.variableCollection = variableCollection;
	}

	public ArrayList<VariableNode> getParameterCollection() {
		return parameterCollection;
	}

	public void setParameterCollection(ArrayList<VariableNode> parameterCollection) {
		this.parameterCollection = parameterCollection;
	}

}