public class StringDataType extends InterpreterDataType {
	
	public StringDataType(StringNode string) {
		this.setString(string);
	}
	
	private StringNode string;

	@Override
	public String ToString() {
		// TODO Auto-generated method stub
		return string.getStringValue()+"";
	}

	@Override
	public void FromString(String input) {
		// TODO Auto-generated method stub
		
	}

	public StringNode getString() {
		return string;
	}

	public void setString(StringNode string) {
		this.string = string;
	}

}