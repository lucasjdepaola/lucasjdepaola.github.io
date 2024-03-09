
public class FunctionCallNode extends StatementNode {
	public FunctionCallNode(ParameterNode params, String name) {
		this.setParams(params);
		this.setName(name);
	}
	private ParameterNode params;
	private String name;
	public void setParams(ParameterNode params) {
		this.params = params;
	}
	
	public ParameterNode getParams() {
		return params;
	}
	public String toString() {
		return "FunctionCallNode(name: " + name + ", parameters: "+params + ")";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}