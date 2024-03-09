public class CharacterDataType extends InterpreterDataType {

	public CharacterDataType(CharacterNode character) {
		this.setCharacter(character);
		
	}
	
	private CharacterNode character;
	
	@Override
	public String ToString() {
		// TODO Auto-generated method stub
		return character.getCharacter()+"";
	}

	@Override
	public void FromString(String input) {
		// TODO Auto-generated method stub
		
	}

	public CharacterNode getCharacter() {
		return character;
	}

	public void setCharacter(CharacterNode character) {
		this.character = character;
	}

}