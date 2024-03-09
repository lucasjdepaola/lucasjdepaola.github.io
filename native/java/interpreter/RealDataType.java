public class RealDataType extends InterpreterDataType {
	
	public RealDataType(RealNode real) {
		this.setReal(real);
	}

	private RealNode real;
	@Override
	public String ToString() {
		// TODO Auto-generated method stub
		return real.getFloatMember()+"";
	}

	@Override
	public void FromString(String input) {
		// TODO Auto-generated method stub
		
	}

	public RealNode getReal() {
		return real;
	}

	public void setReal(RealNode real) {
		this.real = real;
	}

}