import java.util.ArrayList;

public class ParameterNode {
	public ParameterNode(ArrayList<VariableNode> variableCollection, ArrayList<Node> boolCompareCollection) {
		this.setVariableCollection(variableCollection);
		this.setBoolCompareCollection(boolCompareCollection);
	}
	public ArrayList<VariableNode> getVariableCollection() {
		return variableCollection;
	}
	public void setVariableCollection(ArrayList<VariableNode> variableCollection) {
		this.variableCollection = variableCollection;
	}
	public ArrayList<Node> getBoolCompareCollection() {
		return boolCompareCollection;
	}
	public void setBoolCompareCollection(ArrayList<Node> boolCompareCollection) {
		this.boolCompareCollection = boolCompareCollection;
	}
	private ArrayList<VariableNode> variableCollection = new ArrayList<VariableNode>();
	private ArrayList<Node> boolCompareCollection = new ArrayList<Node>();
	
	public String toString() {
		return "ParameterNode(Variable parameters: " + variableCollection + ", condition/expression parameters: " + boolCompareCollection + ")";
	}
}