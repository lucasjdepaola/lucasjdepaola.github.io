import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Lexer {

	ArrayList<Token> objectList = new ArrayList<>();
	String word = "";
	int spaceCounter = 0;
	int lineValue = 1;
	int periodIterator = 0;
	int tabCounter = 0;
	int oldTabValue = 0;

	public ArrayList<Token> lex(String list)
			throws CharacterNotFoundException, IOException, InvalidCharacterLengthException, InvalidCharacterException {

		Token token = new Token(Token.tokenType.NONE);
		HashMap<String, Token.tokenType> knownWords = new HashMap<String, Token.tokenType>();
		HashMapTokenSetter(knownWords);
		token.setToken(Token.tokenType.IDENTIFIER);
		Token.tokenType currentState = Token.tokenType.ENDOFLINE;
		for (int i = 0; i < list.length(); i++) {// iterating through the list to go character-by-character
			char charIterator = list.charAt(i);
			// here is where the state machine will start, the cases include (in order)
			// letters, numbers, spaces, newlines, and finally the single period double
			// state, used for decimal values
			if (IsLetter(charIterator))
				token.setToken(Token.tokenType.IDENTIFIER);
			else if (IsNumber(charIterator))
				token.setToken(Token.tokenType.NUMBER);
			else if (charIterator == ' ')
				token.setToken(Token.tokenType.NONE);
			else if (charIterator == '\n')
				token.setToken(Token.tokenType.ENDOFLINE);
			else if ((currentState != Token.tokenType.ENDOFLINE || currentState != Token.tokenType.NONE)
					&& charIterator == '.' && periodIterator == 0) {
				token.setToken(Token.tokenType.NUMBER);
				periodIterator++;// to ensure only 1 period is used for decimal values, if more are used then an
									// error will be thrown
			} else if (charIterator == ':') {//will peek to check if it is a comment 
				if (peekNextCharacter(i, list) == '=') {
					token.setToken(Token.tokenType.ASSIGNMENT);
					i++;
				} else
					token.setToken(Token.tokenType.COLAN);
			} else if (charIterator == '<') {//will peek to check if it is <> or <=
				if (peekNextCharacter(i, list) == '=') {
					token.setToken(Token.tokenType.LESSTHANOREQUAL);
					i++;
				} else if (peekNextCharacter(i, list) == '>') {
					token.setToken(Token.tokenType.NOTEQUAL);
					i++;
				} else
					token.setToken(Token.tokenType.LESSTHAN);
			} else if (charIterator == '>') {//will peek to check if it is >=
				if (peekNextCharacter(i, list) == '=') {
					token.setToken(Token.tokenType.GREATERTHANOREQUAL);
					i++;
				} else
					token.setToken(Token.tokenType.GREATERTHAN);
			} else if (charIterator == '=')
				token.setToken(Token.tokenType.EQUALS);
			else if (charIterator == '(')
				token.setToken(Token.tokenType.LPAREN);
			else if (charIterator == ')')
				token.setToken(Token.tokenType.RPAREN);
			else if (charIterator == '[')
				token.setToken(Token.tokenType.LBRACKET);
			else if (charIterator == ']')
				token.setToken(Token.tokenType.RBRACKET);
			else if (charIterator == '{')
				token.setToken(Token.tokenType.COMMENT);
			else if (charIterator == '+')
				token.setToken(Token.tokenType.PLUS);
			else if (charIterator == '-')
				token.setToken(Token.tokenType.MINUS);
			else if (charIterator == ',')
				token.setToken(Token.tokenType.COMMA);
			else if (charIterator == '"') {
				token.setToken(Token.tokenType.STRINGLITERAL);
			} else if (charIterator == 39) {// using the apostrophe ASCII value
				token.setToken(Token.tokenType.CHARLITERAL);
			} else if (charIterator == '\t') 
				token.setToken(Token.tokenType.INDENT);
			else if(charIterator=='*')
				token.setToken(Token.tokenType.TIMES);
			else if(charIterator == '/')
				token.setToken(Token.tokenType.DIVIDE);
			else if(charIterator == ';')
				token.setToken(Token.tokenType.SEMICOLAN);
			 else
				throw new CharacterNotFoundException("Incorrect character on line " + lineValue);

			// the handling for the states, will deal with letters numbers, and a single
			// decimal point
			switch (token.getToken()) {
			case IDENTIFIER:
				if (IsLetter(charIterator) || IsNumber(charIterator)) {
					word += charIterator;// collecting letters for the word
					if (containsSpecialCharSimplified(i, list)) {
						Token tokenNonSpaceParser = new Token(Token.tokenType.IDENTIFIER);
						tokenNonSpaceParser.setStringValue(Identify(word.charAt(0), knownWords, word, token));
						hashTokenTypeSetter(tokenNonSpaceParser, word, knownWords);
						tokenNonSpaceParser.setLineNumber(lineValue);
						objectList.add(tokenNonSpaceParser);
						word = "";
						periodIterator = 0;
					}
				}
				break;
			case NUMBER:
				if (IsLetter(charIterator) || IsNumber(charIterator) || charIterator == '.') {
					word += charIterator;
				}
				if(containsSpecialCharSimplified(i,list)) {
					Token NonSpaceParserNumber = new Token(Token.tokenType.NUMBER);
					NonSpaceParserNumber.setStringValue(word);
					NonSpaceParserNumber.setLineNumber(lineValue);
					objectList.add(NonSpaceParserNumber);
					word = "0";
					periodIterator=0;
				}
				break;
			case NONE:// the "none" state will tokenize the word or number that is separated by a
						// space
				if (word.length() != 0) {
					Token tokenInstance = new Token(Token.tokenType.IDENTIFIER);// instancing the word Token
					tokenInstance.setToken(token.getToken());
					
																										// word value to
																										// the Token
																										// object
					tokenInstance.setLineNumber(lineValue);
					hashTokenTypeSetter(tokenInstance, word, knownWords);
					if(tokenInstance.getToken()==Token.tokenType.NUMBER||tokenInstance.getToken()==Token.tokenType.IDENTIFIER)
						tokenInstance.setStringValue(word);// giving the
					objectList.add(tokenInstance);
					word = "";// erasing the word after it has successfully been tokenized, otherwise multiple
								// words will be collected
					periodIterator = 0;
				}
				break;
			case ENDOFLINE:// the \n state, also has to handle tokenizing the final word not separated by a
							// space
				if (word.length() != 0) {
					Token tokenInstance = new Token(Token.tokenType.IDENTIFIER); // instancing the word Token and creating an
																			// object for it
					Token tokenEndOfLineInstance = new Token(Token.tokenType.ENDOFLINE);
					if(Character.isLetter(word.charAt(0))) {
						tokenInstance.setToken(Token.tokenType.IDENTIFIER);
					} else {
						tokenInstance.setToken(Token.tokenType.NUMBER);
					}
					tokenInstance.setStringValue(Identify(word.charAt(0), knownWords, word, token));// setting the
																										// value for the
																										// token object
					tokenInstance.setLineNumber(lineValue);
					tokenEndOfLineInstance.setLineNumber(lineValue);
					hashTokenTypeSetter(tokenInstance, word, knownWords);
					objectList.add(tokenInstance);
					objectList.add(tokenEndOfLineInstance);
					word = "";
					periodIterator = 0;
					lineValue++;

				} else {// if the word is empty we should only be adding an end of line instance, not an
						// empty word
					Token tokenEndOfLineInstance = new Token(Token.tokenType.ENDOFLINE);
					tokenEndOfLineInstance.setLineNumber(lineValue);
					objectList.add(tokenEndOfLineInstance);
					lineValue++;

				}
				// Keeping track of indent and dedent values after the new line has been created
				Token tokenInstanceDedent = new Token(Token.tokenType.DEDENT);
				tokenInstanceDedent.setType("DEDENT");
				tabCounter = IndentCount(i, list);
				if (tabCounter < oldTabValue)
					for (int j = tabCounter; j < oldTabValue; j++) {
						objectList.add(tokenInstanceDedent);
					}
				Token tokenInstanceIndent = new Token(Token.tokenType.INDENT);
				tokenInstanceIndent.setType("INDENT");
				tabCounter = IndentCount(i, list);
				if (tabCounter > oldTabValue) {
					for (int j = oldTabValue; j < tabCounter; j++) {
						objectList.add(tokenInstanceIndent);
					}
				}
				oldTabValue = tabCounter;
				break;
			case COLAN:
				Token tokenInstance = new Token(Token.tokenType.COLAN);
				tokenInstance.setType("COLAN");
				objectList.add(tokenInstance);
				word = "";
				break;
			case ASSIGNMENT:
				Token tokenInstanceAssignment = new Token(Token.tokenType.ASSIGNMENT);
				tokenInstanceAssignment.setType("ASSIGNMENT");
				tokenInstanceAssignment.setStringValue("ASSIGNMENT");
				objectList.add(tokenInstanceAssignment);
				word = "";
				break;
			case LESSTHANOREQUAL:
				Token tokenInstanceLessThanOrEqual = new Token(Token.tokenType.LESSTHANOREQUAL);
				tokenInstanceLessThanOrEqual.setType("LESSTHANOREQUAL");
				tokenInstanceLessThanOrEqual.setStringValue("LESSTHANOREQUAL");
				objectList.add(tokenInstanceLessThanOrEqual);
				word = "";
				break;
			case NOTEQUAL:
				Token tokenInstanceNotEqual = new Token(Token.tokenType.NOTEQUAL);
				tokenInstanceNotEqual.setType("NOTEQUAL");
				objectList.add(tokenInstanceNotEqual);
				word = "";
				break;
			case LESSTHAN:
				Token tokenInstanceLessThan = new Token(Token.tokenType.LESSTHAN);
				tokenInstanceLessThan.setType("LESSTHAN");
				tokenInstanceLessThan.setStringValue("LESSTHAN");
				objectList.add(tokenInstanceLessThan);
				word = "";
				break;
			case GREATERTHANOREQUAL:
				Token tokenInstanceGreaterThanorEqual = new Token(Token.tokenType.GREATERTHANOREQUAL);
				tokenInstanceGreaterThanorEqual.setType("GREATERTHANOREQUAL");
				tokenInstanceGreaterThanorEqual.setStringValue("GREATERTHANOREQUAL");
				objectList.add(tokenInstanceGreaterThanorEqual);
				word = "";
				break;
			case GREATERTHAN:
				Token tokenInstanceGreaterThan = new Token(Token.tokenType.GREATERTHAN);
				tokenInstanceGreaterThan.setType("GREATERTHAN");
				tokenInstanceGreaterThan.setStringValue("GREATHERTHAN");
				objectList.add(tokenInstanceGreaterThan);
				word = "";
				break;
			case EQUALS:
				Token tokenInstanceEquals = new Token(Token.tokenType.EQUALS);
				tokenInstanceEquals.setType("EQUALS");
				tokenInstanceEquals.setStringValue("EQUALS");
				objectList.add(tokenInstanceEquals);
				word = "";
				break;
			case LPAREN:
				Token tokenInstanceLParen = new Token(Token.tokenType.LPAREN);
				tokenInstanceLParen.setType("LPAREN");
				objectList.add(tokenInstanceLParen);
				word = "";
				break;
			case RPAREN:
				Token tokenInstanceRParen = new Token(Token.tokenType.RPAREN);
				tokenInstanceRParen.setType("RPAREN");
				objectList.add(tokenInstanceRParen);
				word = "";
				break;
			case LBRACKET:
				Token tokenInstanceLBracket = new Token(Token.tokenType.LBRACKET);
				tokenInstanceLBracket.setType("LBRACKET");
				objectList.add(tokenInstanceLBracket);
				word = "";
				break;
			case RBRACKET:
				Token tokenInstanceRBracket = new Token(Token.tokenType.RBRACKET);
				tokenInstanceRBracket.setType("RBRACKET");
				objectList.add(tokenInstanceRBracket);
				word = "";
				break;
			case COMMENT:// comment token, does not save the comment, will yield an error if nested comment braces
				Token tokenInstanceLCurlyBrace = new Token(Token.tokenType.COMMENT);
				tokenInstanceLCurlyBrace.setType("COMMENT");
				String commentValue="";
				while (peekNextCharacter(i, list) != '}') {
					
					if (peekNextCharacter(i, list) == '{')
						throw new InvalidCharacterException(
								"Cannot use two left curly braces within a comment, line " + lineValue);
					charIterator = list.charAt(i);
					i++;
					commentValue+=list.charAt(i);
				}
				i++;
				tokenInstanceLCurlyBrace.setStringValue(commentValue);
				objectList.add(tokenInstanceLCurlyBrace);
				word = "";
				break;
			case PLUS:
				Token tokenInstancePlus = new Token(Token.tokenType.PLUS);
				tokenInstancePlus.setType("PLUS");
				objectList.add(tokenInstancePlus);
				word = "";
				break;
			case MINUS:
				Token tokenInstanceMinus = new Token(Token.tokenType.MINUS);
				tokenInstanceMinus.setType("MINUS");
				objectList.add(tokenInstanceMinus);
				word = "";
				break;
			case TIMES:
				Token tokenInstanceTimes = new Token(Token.tokenType.TIMES);
				tokenInstanceTimes.setType("TIMES");
				objectList.add(tokenInstanceTimes);
				word = "";
				break;
			case DIVIDE:
				Token tokenInstanceDivide = new Token(Token.tokenType.DIVIDE);
				tokenInstanceDivide.setType("DIVIDE");
				objectList.add(tokenInstanceDivide);
				word="";
				break;
			case COMMA:
				Token tokenInstanceComma = new Token(Token.tokenType.COMMA);
				tokenInstanceComma.setType("COMMA");
				objectList.add(tokenInstanceComma);
				word = "";
				break;
			case STRINGLITERAL:// tokenizing a String value, must be surrounded by double quotes
				Token tokenInstanceStringLiteral = new Token(Token.tokenType.STRINGLITERAL);
				tokenInstanceStringLiteral.setType("STRINGLITERAL");

				String stringLiteral = "";
				while (peekNextCharacter(i, list) != '"') {
					i++;
					stringLiteral += list.charAt(i);
					charIterator = list.charAt(i);
				}
				i++;
				tokenInstanceStringLiteral.setStringValue(stringLiteral);
				objectList.add(tokenInstanceStringLiteral);
				word = "";

				break;
			case CHARLITERAL:// tokenizing a char value, will yield an error if more than 1 character is inside ''
				Token tokenInstanceCharLiteral = new Token(Token.tokenType.CHARLITERAL);
				tokenInstanceCharLiteral.setType("CHARLITERAL");
				char charLiteralChar = peekNextCharacter(i, list);
				i++;
				if (peekNextCharacter(i, list) != 39)
					throw new InvalidCharacterLengthException("Char values can only contain 1 character, line "+lineValue);
				i++;
				tokenInstanceCharLiteral.setStringValue(Character.toString(charLiteralChar));
				objectList.add(tokenInstanceCharLiteral);
				word = "";
				break;
			case INDENT://refer to the endOfLine state where indents are handled, this case isn't necessary 
				break;
			case SEMICOLAN:
				Token tokenInstanceSemiColan = new Token(Token.tokenType.SEMICOLAN);
				tokenInstanceSemiColan.setType("SEMICOLAN");
				objectList.add(tokenInstanceSemiColan);
				word = "";
				break;
			}
			

		}
		return objectList;
	}
	
	
	public void HashMapTokenSetter(HashMap<String, Token.tokenType> knownWords) {// setting all of the known shank
																					// keywords into a hashmap
		knownWords.put("while", Token.tokenType.WHILE);
		knownWords.put("define", Token.tokenType.DEFINE);
		knownWords.put("for", Token.tokenType.FOR);
		knownWords.put("from", Token.tokenType.FROM);
		knownWords.put("write", Token.tokenType.WRITE);
		knownWords.put("constants", Token.tokenType.CONSTANTS);
		knownWords.put("variables", Token.tokenType.VARIABLES);
		knownWords.put("integer", Token.tokenType.INTEGER);
		knownWords.put("var", Token.tokenType.VAR);
		knownWords.put("mod", Token.tokenType.MOD);
		knownWords.put("if", Token.tokenType.IF);
		knownWords.put("elsif", Token.tokenType.ELSIF);
		knownWords.put("else", Token.tokenType.ELSE);
		knownWords.put("then", Token.tokenType.THEN);
		knownWords.put("repeat", Token.tokenType.REPEAT);
		knownWords.put("until", Token.tokenType.UNTIL);
		knownWords.put("string", Token.tokenType.STRING);
		knownWords.put("to", Token.tokenType.TO);
		knownWords.put("array", Token.tokenType.ARRAY);
		knownWords.put("of",Token.tokenType.OF);
		knownWords.put("bool",Token.tokenType.BOOL);
		knownWords.put("true",Token.tokenType.TRUE);
		knownWords.put("false",Token.tokenType.FALSE);
		knownWords.put("char",Token.tokenType.CHAR);
		knownWords.put("real", Token.tokenType.REAL);
	}
	
	public void hashTokenTypeSetter(Token token, String word,HashMap<String, Token.tokenType> knownWords) {
		if(knownWords.containsKey(word)) {
			token.setToken(knownWords.get(word));
		}
		else if(Character.isLetter(word.charAt(0)))
			token.setToken(Token.tokenType.IDENTIFIER);
		else if(Character.isDigit(word.charAt(0)))
			token.setToken(Token.tokenType.NUMBER);
	}

	public int IndentCount(int i, String list) {//helps count the number of indents per line, loops until there are no indentations
		int indentCount = 0;
		int spaceCount = 0;
		i += i == list.length() - 1 ? 0 : 1;
		while (list.charAt(i) == ' ' || list.charAt(i) == '\t') {
			if (list.charAt(i) == ' ') {
				spaceCount++;
			}
			if (list.charAt(i) == '\t')
				indentCount++;
			indentCount += spaceCount / 4;//4 spaces counts as an indentation, no decimal case due to integers
			if (i != list.length() - 1)
				i++;
			else {
				break;
			}
		}
		i++;
		return indentCount;
	}

	boolean containsSpecialCharSimplified(int i, String list) {// method used to help parse out tokens that are not spaced, such as 4+4 or 2>1, yields true if token needs parsing
		if (peekNextCharacter(i, list) == '<' || peekNextCharacter(i, list) == '(' || peekNextCharacter(i, list) == '>'
				|| peekNextCharacter(i, list) == '{' || peekNextCharacter(i, list) == '='
				|| peekNextCharacter(i, list) == ':' || peekNextCharacter(i, list) == ')'
				|| peekNextCharacter(i, list) == '+' || peekNextCharacter(i, list) == '-'
				|| peekNextCharacter(i, list) == ',' || peekNextCharacter(i, list) == '"'
				|| peekNextCharacter(i, list) == '*' || peekNextCharacter(i, list) =='/'
				|| peekNextCharacter(i, list) == ';' || peekNextCharacter(i, list) =='['
				|| peekNextCharacter(i, list) == ']')
			return true;
		return false;
	}

	public char peekNextCharacter(int i, String list) {// peak function for the double character tokens such as :=
		return i == list.length() - 1 ? ' ' : list.charAt(i + 1);
	}

	boolean IsLetter(char value) {// returns true if the character is a letter
		return Character.isLetter(value);
	}

	boolean IsNumber(char value) {// returns true if the character is a number
		return Character.isDigit(value);
	}

	public String Identify(char firstCharacterOfWord, HashMap<String, Token.tokenType> knownWords, String word,
			Token token) {//identifies whether a token is a known key word, number, or identifier
		if (knownWords.containsKey(word)) {
			
			return null;
		}
		return Character.isLetter(firstCharacterOfWord) ?  word:word;
		//takes the first character and decides if it is going to be a number or a word																										
	}

	public String toString() {
		String tokensToString = "";
		for (int i = 0; i < objectList.size(); i++) {// iterating through the size of the array list, getting each value
			tokensToString += objectList.get(i).getToken();	 // and adding it to the string
			if(objectList.get(i).getStringValue()!=null) {
				tokensToString += "(" + objectList.get(i).getStringValue()+")";
			}
			tokensToString += objectList.get(i).getToken() == Token.tokenType.ENDOFLINE ? "\n" : " ";// determining whether to
																								// space the token or to
																								// give it a new line
		}
		return tokensToString;
	}
}

class CharacterNotFoundException extends Exception {// exception for the unhandled special characters that have not been
													// implemented yet, terminates the program on error
	public CharacterNotFoundException(String message) {
		super(message);
	}
}

class TooManyArgumentsException extends Exception {// exception for adding too many arguments to the configuration
	public TooManyArgumentsException(String message) {
		super(message);
	}
}

class InvalidCharacterException extends Exception {// exception if someone tries to nest a comment inside of a comment
	public InvalidCharacterException(String message) {
		super(message);
	}
}

class InvalidCharacterLengthException extends Exception {// exception if someone tries to create a char that is more than 1 character
	public InvalidCharacterLengthException(String message) {
		super(message);
	}
}