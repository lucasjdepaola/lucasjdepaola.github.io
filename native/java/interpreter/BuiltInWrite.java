import java.util.ArrayList;

public class BuiltInWrite extends FunctionNode {

	public BuiltInWrite(String functionName, ArrayList<VariableNode> parameterCollection,
			ArrayList<VariableNode> variableCollection, ArrayList<StatementNode> statements) {
		super(functionName, parameterCollection, variableCollection, statements);
	}

	public void execute(ArrayList<InterpreterDataType> dataCollection) {
		System.out.println(dataCollection);
	}

}