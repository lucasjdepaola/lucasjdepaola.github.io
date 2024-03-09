public class CharacterNode extends Node {
	//node containing a char literal, stored inside of a variable node
public CharacterNode(char character) {
	this.setCharacter(character);
}
private char character;
public String toString(){
	return character +"";
}
public char getCharacter() {
	return character;
}
public void setCharacter(char character) {
	this.character = character;
}
}