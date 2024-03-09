
public class Token extends Lexer {
	
	public Token(tokenType token) {
		this.setToken(token);
	}

	public enum tokenType {//enumerator types to determine the states of the state machine
		NONE, IDENTIFIER, NUMBER, ENDOFLINE, WHILE, DEFINE, FOR, FROM, WRITE, CONSTANTS, VARIABLES, INTEGER, 
		VAR, MOD, IF, ELSIF, ELSE, THEN, REPEAT, UNTIL, STRING, TO, LPAREN, RPAREN, EQUALS, MINUS, PLUS,
		COLAN, ASSIGNMENT, LESSTHANOREQUAL, LESSTHAN, GREATERTHANOREQUAL, GREATERTHAN, LBRACKET, RBRACKET,
		COMMENT, COMMA, NOTEQUAL, STRINGLITERAL, CHARLITERAL, INDENT, DEDENT, TIMES, DIVIDE, SEMICOLAN, ARRAY, OF,BOOL,
		TRUE, FALSE, CHAR,REAL;
	}
	private String type;
	private String value;
	private int lineNumber = 0;
	private tokenType token;
	
	public tokenType getToken() {//tokenType getter
		return token;
	}

	public void setToken(tokenType token) {//tokenType setter
		this.token = token;
	}

	public void setStringValue(String value) {//sets a string value for a tokenType instance
		this.value = value;
	}

	public String getStringValue() {//gets the string value of a tokenType instance
		return value;
	}
	
	public String getType() {//type getter
		return type;
	}
	
	public void setType(String type) {//type setter
		this.type = type;
	}
	public int getLineNumber() {//getter for the line number
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {//setter for the line number
		this.lineNumber = lineNumber;
	}

}