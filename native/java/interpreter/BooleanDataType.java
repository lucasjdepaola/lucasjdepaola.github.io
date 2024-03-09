public class BooleanDataType extends InterpreterDataType {
	
	public BooleanDataType(BooleanNode bool) {
		this.setBool(bool);
	}
	
	private BooleanNode bool;
	
	@Override
	public String ToString() {
		// TODO Auto-generated method stub
		return bool.isBool()+"";
	}

	@Override
	public void FromString(String input) {
		// TODO Auto-generated method stub
		
	}

	public BooleanNode getBool() {
		return bool;
	}

	public void setBool(BooleanNode bool) {
		this.bool = bool;
	}

}