import java.util.HashMap;

public class ProgramNode extends Node {
	//hashmap containing all of the functions, (functionName, functionNode)
	
	HashMap<String, FunctionNode> FunctionNodes = new HashMap<String, FunctionNode>();
	public ProgramNode(HashMap<String, FunctionNode> FunctionNodes) {
		this.FunctionNodes = FunctionNodes;
	}
	public String toString() {
		String returnValue = "";


		return"PROGRAMTOSTRING"+ FunctionNodes+"";
	}
	
	
}