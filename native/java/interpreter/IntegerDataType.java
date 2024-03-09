public class IntegerDataType extends InterpreterDataType {
	
	public IntegerDataType(IntegerNode integer) {
		this.setInteger(integer);
	}

	private IntegerNode integer;
	
	@Override
	public String ToString() {
		// TODO Auto-generated method stub
		return integer.getInteger()+"";
	}

	@Override
	public void FromString(String input) {
		// TODO Auto-generated method stub
		
	}

	public IntegerNode getInteger() {
		return integer;
	}

	public void setInteger(IntegerNode integer) {
		this.integer = integer;
	}

}