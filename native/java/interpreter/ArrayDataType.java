public class ArrayDataType extends InterpreterDataType {

	public ArrayDataType(InterpreterDataType array) {
		this.setArray(array);
	}
	
	private InterpreterDataType array;
	
	@Override
	public String ToString() {
		// TODO Auto-generated method stub
		return array.ToString();
	}

	@Override
	public void FromString(String input) {
		// TODO Auto-generated method stub
		
	}

	public InterpreterDataType getArray() {
		return array;
	}

	public void setArray(InterpreterDataType array) {
		this.array = array;
	}

}