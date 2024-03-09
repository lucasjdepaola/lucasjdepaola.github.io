import java.util.ArrayList;
import java.util.HashMap;



public class Parser {
	//recursive descent
	private ArrayList<Token> objectList;
	public Parser(ArrayList<Token> objectList) {
		this.objectList = objectList;
	}
HashMap<String, FunctionNode> programMap = new HashMap<String, FunctionNode>();	
	public Node parse() throws NoEndOfLineException, MultipleAdditionException, IncorrectIndentationException, IncorrectSyntaxException{
		Token blank = new Token(Token.tokenType.INDENT);
		objectList.add(blank);//adding this to ensure that once everything is parsed, there is still 1 token so the array list does not go out of bounds
		FunctionNode functionValue = function();
		if(functionValue!=null) programMap.put(functionValue.getfunctionName(), functionValue);
		while(functionValue!=null) {
			while(matchAndRemove(Token.tokenType.ENDOFLINE)!=null);//need to remove potential space between lines
			System.out.println(functionValue+"\n");//printing the functions while adding them to the program node map to know what has been added
			functionValue = function();
			if(functionValue==null) break;
			programMap.put(functionValue.getfunctionName(), functionValue);//accumulating functions
		}
		ProgramNode program = new ProgramNode(programMap);
		System.out.println("finished parsing, the remaining token should be the placeholder indent --> (" + objectList.get(0).getToken()+")");
		return program;
	}
	
	private Token matchAndRemove(Token.tokenType token) {//looks for a tokenType and if found, it will remove the element that contains the token
		Token returnToken;
		if(objectList.size()==0) return null;
		if(objectList.get(0).getToken()==token) {
			returnToken = objectList.get(0);
			objectList.remove(0);
			return returnToken;
			
		}
		return null;
	}
	
	public ArrayList<StatementNode> statements() throws MultipleAdditionException, IncorrectIndentationException, IncorrectSyntaxException{//returns a collection of statements
		if(matchAndRemove(Token.tokenType.INDENT)==null) throw new IncorrectIndentationException("incorrect indentation, use 1 indent after making declaration statements, your token is: " + objectList.get(0).getToken());
		ArrayList<StatementNode> statementList = new ArrayList<StatementNode>();
		StatementNode statement = statement();
		
		while(statement!=null) {
			statementList.add(statement);
			statement = statement();
			
		}
		matchAndRemove(Token.tokenType.DEDENT);
		return statementList;
	}
	
	public StatementNode statement() throws MultipleAdditionException, IncorrectIndentationException, IncorrectSyntaxException {//returns a singular statement
		if(peek(0)==Token.tokenType.IDENTIFIER&&(peek(1)==Token.tokenType.ASSIGNMENT)||peek(1)==Token.tokenType.LBRACKET) {
			AssignmentNode assignment = assignment();
			return assignment;
		}
		if(peek(0)==Token.tokenType.FOR) {//checking each case and calling the appropriate function based on the peek value
			ForNode forParseNode = parseFor();
			return forParseNode;
		}
		if(peek(0)==Token.tokenType.WHILE) {
			WhileNode whileParseNode = parseWhile();
			return whileParseNode;
		}
		if(peek(0)==Token.tokenType.REPEAT) {
			RepeatNode repeatParseNode = parseRepeat();
			return repeatParseNode;
		}
		if(peek(0)==Token.tokenType.IF) {
			IfNode ifParseNode = parseIf();
			return ifParseNode;
		}
		if(peek(0)==Token.tokenType.IDENTIFIER) {
			FunctionCallNode functionCall = functionCall();
			return functionCall;
		}
		return null;
	}
	
	public Node BoolCompare() throws MultipleAdditionException {//compares 2 expressions and returns a boolean compare node
		Node expressionNode = expression();
		if(expressionNode == null) return null;
		Token comparer = objectList.get(0);
		Token assignment = matchAndRemoveAssignment();
		if(assignment==null) return expressionNode;//if its just an expression with no bool compare
		BooleanCompareNode.compareType type = CompareType(comparer.getToken());//setting the type based on a method that interprets the Token.tokenType to BooleanCompare.comparetype
		Node secondExpression = expression();
		BooleanCompareNode boolNode = new BooleanCompareNode(expressionNode,secondExpression,type);//change to actual value
		
		return boolNode;
	}
	
	private BooleanCompareNode.compareType CompareType(Token.tokenType comparer){//returns the proper compareType token when given a regular lex token
		//NOTEQUAL, ASSIGNMENT, LESSTHAN, GREATHERTHAN, LESSTHANEQUAL, GREATHERTHANEQUAL, EQUAL,
		if(comparer == Token.tokenType.GREATERTHANOREQUAL) {
			return BooleanCompareNode.compareType.GREATHERTHANEQUAL;
		}
		else if(comparer == Token.tokenType.NOTEQUAL) {
			return BooleanCompareNode.compareType.NOTEQUAL;
		}
		else if(comparer == Token.tokenType.ASSIGNMENT) {
			return BooleanCompareNode.compareType.ASSIGNMENT;
		}
		else if(comparer == Token.tokenType.LESSTHAN) {
			return BooleanCompareNode.compareType.LESSTHAN;
		}
		else if(comparer == Token.tokenType.GREATERTHAN) {
			return BooleanCompareNode.compareType.GREATHERTHAN;
		}
		else if(comparer == Token.tokenType.LESSTHANOREQUAL) {
			return BooleanCompareNode.compareType.LESSTHANEQUAL;
		}
		else if(comparer == Token.tokenType.EQUALS) {
			return BooleanCompareNode.compareType.EQUAL;
		}
		return null;//if it doesn't satisfy any conditions then just return null
	}
	
	public Token matchAndRemoveAssignment() {//finding, removing, and returning the assignment tokens for statements
		Token returnToken = matchAndRemove(Token.tokenType.NOTEQUAL);
		if(returnToken!=null) return returnToken;
		returnToken = matchAndRemove(Token.tokenType.LESSTHAN);
		if(returnToken!=null) return returnToken;
		returnToken = matchAndRemove(Token.tokenType.GREATERTHAN);
		if(returnToken!=null) return returnToken;
		returnToken = matchAndRemove(Token.tokenType.GREATERTHANOREQUAL);
		if(returnToken!=null) return returnToken;
		returnToken = matchAndRemove(Token.tokenType.ASSIGNMENT);
		if(returnToken!=null) return returnToken;
		returnToken = matchAndRemove(Token.tokenType.LESSTHANOREQUAL);
		if(returnToken!=null) return returnToken;
		returnToken = matchAndRemove(Token.tokenType.EQUALS);
		if(returnToken!=null) return returnToken;
		returnToken = matchAndRemove(Token.tokenType.NOTEQUAL);
		if(returnToken!=null) return returnToken;
		return null;
	}
	
	public AssignmentNode assignment() throws MultipleAdditionException {//handling a variable assignment
		Token identifier = matchAndRemove(Token.tokenType.IDENTIFIER);
		
		if(identifier == null) return null;
		
		if(matchAndRemove(Token.tokenType.LBRACKET)!=null) {//handling complex array values, which are defined as expressions within the brackets
			Node expressionNode = expression();
			matchAndRemove(Token.tokenType.RBRACKET);
			matchAndRemove(Token.tokenType.ASSIGNMENT);
			VariableReferenceNode referenceNode = new VariableReferenceNode(identifier.getStringValue(),expressionNode);
			Node boolNode = BoolCompare();
			AssignmentNode assignmentValue = new AssignmentNode(referenceNode,boolNode);
			matchAndRemove(Token.tokenType.ENDOFLINE);
			return assignmentValue;
		}
		
		VariableReferenceNode referenceNode = new VariableReferenceNode(identifier.getStringValue());
		if(matchAndRemove(Token.tokenType.ASSIGNMENT)==null)return null;
		Node boolNode = BoolCompare();
		AssignmentNode assignmentValue = new AssignmentNode(referenceNode,boolNode);
		matchAndRemove(Token.tokenType.ENDOFLINE);
		return assignmentValue;
	}
	
	
	public FunctionNode function() throws NoEndOfLineException, MultipleAdditionException, IncorrectIndentationException, IncorrectSyntaxException {
		//correct function syntax: define identifier(param:type) endofline constant or variable declarations, indent, statements							
		ArrayList<VariableNode> parameters = new ArrayList<VariableNode>();
		if(matchAndRemove(Token.tokenType.DEFINE)==null) return  null;//starting the function, if it doesn't have define then it's not a function so return null
		Token identifierToken = objectList.get(0);
		if(matchAndRemove(Token.tokenType.IDENTIFIER)==null) return null;
		if(matchAndRemove(Token.tokenType.LPAREN)==null) return null;
		parameters = parameterDeclaration();//finding the variables inside of the parameters
		ArrayList<VariableNode> variableList = new ArrayList<VariableNode>();
		variableList = parameters;
		if(matchAndRemove(Token.tokenType.RPAREN)==null)return null;
		expectsEndOfLine();
		ArrayList<VariableNode> variableArrayList = new ArrayList<VariableNode>();
		variableArrayList = variableDeclaration();
		ArrayList<StatementNode> referenceNodes = new ArrayList<StatementNode>();
		referenceNodes = statements();
		matchAndRemove(Token.tokenType.ENDOFLINE);
		FunctionNode functionValue = new FunctionNode(identifierToken.getStringValue(),parameters,variableArrayList,referenceNodes);
		matchAndRemove(Token.tokenType.DEDENT); 
		
		return functionValue;//should return a function node due to it processing a function
	}
	
	public FunctionCallNode functionCall() throws IncorrectSyntaxException, MultipleAdditionException {//creating 2 array lists that store the bool comparison parameters, and the variable parameters
		ArrayList<VariableNode> variableCollection = new ArrayList<VariableNode>();
		ArrayList<Node> boolCompareCollection = new ArrayList<Node>();
		Token functionIdentifier = matchAndRemove(Token.tokenType.IDENTIFIER);
		if(functionIdentifier == null) return null;
		while(matchAndRemove(Token.tokenType.ENDOFLINE)==null) {
			if(matchAndRemove(Token.tokenType.VAR)!=null) {
				Token identifier = matchAndRemove(Token.tokenType.IDENTIFIER);
				VariableNode varNode = new VariableNode(null,true,identifier.getStringValue(),null);//since the value is not accessible and the type is not, just leaving it null
				matchAndRemove(Token.tokenType.COMMA);
				variableCollection.add(varNode);
			} else {//there are only 2 conditions, meaning that if it's not a var, its a boolComparison/expression
				Node boolCompareNode = BoolCompare();
				if(boolCompareNode==null) return null;//need to make sure that an infinite while loop doesn't happen, so if the else case doesn't catch it, return null
				matchAndRemove(Token.tokenType.COMMA);
				boolCompareCollection.add(boolCompareNode);
			}
			
		}
		ParameterNode params = new ParameterNode(variableCollection, boolCompareCollection);
		FunctionCallNode functionCall = new FunctionCallNode(params,functionIdentifier.getStringValue());
		
		return functionCall;
	}
	
	public WhileNode parseWhile() throws MultipleAdditionException, IncorrectIndentationException, IncorrectSyntaxException {//parses out the while function
		if(matchAndRemove(Token.tokenType.WHILE)==null) return null;
		BooleanCompareNode condition = (BooleanCompareNode)BoolCompare();// remove while, find its boolCompare condition, find its statements via recursion, then return the new while node
		matchAndRemove(Token.tokenType.ENDOFLINE);
		ArrayList<StatementNode> whileStatements = statements();
		WhileNode whileParsed = new WhileNode(condition, whileStatements);
		return whileParsed;
	}
	
	public IfNode parseIf() throws MultipleAdditionException, IncorrectIndentationException, IncorrectSyntaxException {//parses out the if function
		Token ifType = objectList.get(0);//to see what type of if we have
		if(matchAndRemove(Token.tokenType.IF)==null&&matchAndRemove(Token.tokenType.ELSIF)==null&&matchAndRemove(Token.tokenType.ELSE)==null) return null;
		BooleanCompareNode condition = (BooleanCompareNode)BoolCompare();//finding the comparison value
		if(matchAndRemove(Token.tokenType.THEN)==null) return null;
		matchAndRemove(Token.tokenType.ENDOFLINE);
		ArrayList<StatementNode> ifStatements = statements();//using recursion to find the sub statements
		IfNode ifParsed = new IfNode(condition, ifStatements);
		
		return ifParsed;
	}
	
	public RepeatNode parseRepeat() throws MultipleAdditionException, IncorrectIndentationException, IncorrectSyntaxException {//parses out the repeat function
		if(matchAndRemove(Token.tokenType.REPEAT)==null) return null;
		if(matchAndRemove(Token.tokenType.UNTIL)==null) return null;//the format is repeat until *condition*
		BooleanCompareNode condition = (BooleanCompareNode)BoolCompare();//could either set as Node or cast to bool compare node, since we are expecting boolcompare, casting is appropriate
		matchAndRemove(Token.tokenType.ENDOFLINE);
		ArrayList<StatementNode> repeatStatements = statements();//using recursion to find the sub statements
		RepeatNode repeatParsed = new RepeatNode(condition, repeatStatements);
		return repeatParsed;
	}
	
	public ForNode parseFor() throws MultipleAdditionException, IncorrectIndentationException, IncorrectSyntaxException {//parses out the for function
		if(matchAndRemove(Token.tokenType.FOR)==null) return null;
		Token loopVariableDeclaration = matchAndRemove(Token.tokenType.IDENTIFIER);
		if(matchAndRemove(Token.tokenType.FROM)==null) return null;
		Node fromExpression = expression();//we need a from range expression (usually its just a number but still has expression capabilities
		if(matchAndRemove(Token.tokenType.TO)==null) return null;
		Node toExpression = expression();//end number expression
		matchAndRemove(Token.tokenType.ENDOFLINE);
		ArrayList<StatementNode> statements = statements();//using recursion to process sub statements
		ForNode forParsed = new ForNode(fromExpression, toExpression, statements,loopVariableDeclaration.getStringValue());
		return forParsed;
	}
	
	public ArrayList<VariableNode> parameterDeclaration() {//processes the parameters and returns collection of variable Node
		ArrayList<VariableNode> variableNodeList = new ArrayList<VariableNode>();
		VariableNode varNode = variableFinder();
		while(varNode!=null) {
			variableNodeList.add(varNode);
			varNode = variableFinder();
		}
		return variableNodeList;
	}
	
	public ArrayList<VariableNode> variableDeclaration() throws NoEndOfLineException{//variable list for the variable declaration
		ArrayList<VariableNode> varNodes = new ArrayList<VariableNode>();
		VariableNode varNode = processVariables();
		while(varNode!=null) {
			varNodes.add(varNode);
			varNode = processVariables();
		}
		return varNodes;
	}
	
	public VariableNode variableFinder() {//finds a variable inside the parameters
		boolean changeable = true;//parameter declarations are variables, needs to be true
		String identifierString = "";
		String type = "";
		StringNode value = new StringNode("Undefined");
		Token identifier = objectList.get(0);
		if(matchAndRemove(Token.tokenType.IDENTIFIER)==null) return null;
		identifierString = identifier.getStringValue();
		matchAndRemove(Token.tokenType.COLAN);
		Token typeToken = objectList.get(0);
		type = typeToken.getToken().toString();
		matchAndRemove(Token.tokenType.INTEGER);
		matchAndRemove(Token.tokenType.STRING);
		matchAndRemove(Token.tokenType.BOOL);
		matchAndRemove(Token.tokenType.CHAR);
		//need to add real type
		VariableNode variableNode = new VariableNode(value, changeable, identifierString, type);
		if(peek(0)!=Token.tokenType.RPAREN) matchAndRemove(Token.tokenType.SEMICOLAN);//remove semi colan if more declarations are to come
		
		return variableNode;
	}
	
	public VariableNode processVariables() throws NoEndOfLineException {//need to handle consts and variable declarations below parameters
		VariableNode variableNode; 
		ArrayList<String> identifierList = new ArrayList<String>();
		boolean changeable = true;
		String type = "";
		if(peek(0)==Token.tokenType.IDENTIFIER&&peek(1)==Token.tokenType.EQUALS) {//you can only assign a constant with an equals, meaning this type of assignment casts it to constant
			Token constIdentifier = matchAndRemove(Token.tokenType.IDENTIFIER);
			matchAndRemove(Token.tokenType.EQUALS);
			changeable = false;
			Token value = objectList.get(0);
			type = value.getToken().toString();
			if(type=="STRINGLITERAL") {
				type = "string";
				StringNode stringNode = new StringNode(value.getStringValue());
				VariableNode constNode = new VariableNode(stringNode,changeable,constIdentifier.getStringValue(),type);
				matchAndRemove(Token.tokenType.ENDOFLINE);
				return constNode;
			}
			if(type=="CHARLITERAL") {
				type = "char";
				char charValue = value.getStringValue().charAt(0);
				CharacterNode returnNode = new CharacterNode(charValue);
				VariableNode constNode = new VariableNode(returnNode,changeable,constIdentifier.getStringValue(),type);
				return constNode;
			}	
		}
		if(matchAndRemove(Token.tokenType.VARIABLES)!=null||matchAndRemove(Token.tokenType.VAR)!=null) {//variable declaration
			Token identifier = matchAndRemove(Token.tokenType.IDENTIFIER);
			identifierList.add(identifier.getStringValue());
			while(matchAndRemove(Token.tokenType.COMMA)!=null) {
				identifierList.add(matchAndRemove(Token.tokenType.IDENTIFIER).getStringValue());//adding the string value of the variable to the list while removing it at the same time
			}
			matchAndRemove(Token.tokenType.COLAN);
			Token typeToken = objectList.get(0);
			type = typeToken.getToken().toString();
			if(typeToken.getToken()==Token.tokenType.ARRAY) {//array variable declaration
				Token arrayToken = matchAndRemove(Token.tokenType.ARRAY);
				matchAndRemove(Token.tokenType.FROM);
				int from = Integer.parseInt(matchAndRemove(Token.tokenType.NUMBER).getStringValue());
				matchAndRemove(Token.tokenType.TO);
				int to = Integer.parseInt(matchAndRemove(Token.tokenType.NUMBER).getStringValue());
				matchAndRemove(Token.tokenType.OF);
				String arrayType = objectList.get(0).getToken().toString();
				matchAndRemove(Token.tokenType.STRING);
				matchAndRemove(Token.tokenType.INTEGER);
				matchAndRemove(Token.tokenType.BOOL);
				matchAndRemove(Token.tokenType.CHAR);
				VariableNode arrayNode = new VariableNode(null, changeable,identifier.getStringValue(),arrayToken.getToken().toString()+" of "+arrayType);
				arrayNode.setFrom(from);
				arrayNode.setTo(to);
				expectsEndOfLine();
				return arrayNode;
			}
			matchAndRemove(Token.tokenType.INTEGER);//removing token types off of the queue after creating nodes for them
			matchAndRemove(Token.tokenType.STRING);
			matchAndRemove(Token.tokenType.BOOL);
			matchAndRemove(Token.tokenType.CHAR);
			matchAndRemove(Token.tokenType.REAL);
			
			String variableString="";
			for(int i = 0;i<identifierList.size();i++) {
				variableString+=identifierList.get(i)+" ";
			}
			variableNode = new VariableNode(null,changeable,variableString,type);
			if(peek(0)==Token.tokenType.FROM&&type=="REAL") {
				matchAndRemove(Token.tokenType.FROM);
				float from = Float.parseFloat(matchAndRemove(Token.tokenType.NUMBER).getStringValue());
				matchAndRemove(Token.tokenType.TO);
				float to = Float.parseFloat(matchAndRemove(Token.tokenType.NUMBER).getStringValue());
				variableNode.setFromReal(from);
				variableNode.setToReal(to);
			}
			if(matchAndRemove(Token.tokenType.FROM)!=null&&(type=="INTEGER"||type=="STRING")) {
				int from = Integer.parseInt(matchAndRemove(Token.tokenType.NUMBER).getStringValue());
				matchAndRemove(Token.tokenType.TO);
				int to = Integer.parseInt(matchAndRemove(Token.tokenType.NUMBER).getStringValue());
				variableNode.setFrom(from);
				variableNode.setTo(to);
			}
			
			expectsEndOfLine();
			return variableNode;
		}
		if(matchAndRemove(Token.tokenType.CONSTANTS)!=null) {
			changeable = false;
			Token identifier = matchAndRemove(Token.tokenType.IDENTIFIER);
			matchAndRemove(Token.tokenType.EQUALS);
			Token value = objectList.get(0);
			if(matchAndRemove(Token.tokenType.STRINGLITERAL)!=null) {//if string literal, make a string node and a variable node, the variable node holds the string node
				StringNode stringNode = new StringNode(value.getStringValue());
				type = "string";
				VariableNode constantNode = new VariableNode(stringNode,changeable,identifier.getStringValue(),type);
				matchAndRemove(Token.tokenType.COMMA);
				matchAndRemove(Token.tokenType.ENDOFLINE);
				return constantNode;
			}
			if(matchAndRemove(Token.tokenType.NUMBER)!=null) {
				if(isDecimal(value.getStringValue())) {//incase it is a real value, rather than an integer value
					float decimalValue = Float.parseFloat(value.getStringValue());
					RealNode doubleValue = new RealNode(decimalValue);
					type = "real";
					VariableNode constantNode = new VariableNode(doubleValue,changeable,identifier.getStringValue(),type);
					matchAndRemove(Token.tokenType.COMMA);
					matchAndRemove(Token.tokenType.ENDOFLINE);
					return constantNode;
				}
				IntegerNode integerValue = new IntegerNode(Integer.parseInt(value.getStringValue()));
				type = "integer";
				VariableNode constantNode = new VariableNode(integerValue,changeable,identifier.getStringValue(),type);
				matchAndRemove(Token.tokenType.COMMA);//removing the comma incase there are more values to be declared in 1 line
				matchAndRemove(Token.tokenType.ENDOFLINE);//cannot use expectsEndOfLine() because it's not guaranteed to be endofLine
				return constantNode;
			}
			if(matchAndRemove(Token.tokenType.TRUE)!=null||matchAndRemove(Token.tokenType.FALSE)!=null){
				boolean boolCase=false;
				if(value.getToken()==Token.tokenType.TRUE) boolCase = true;
				type = "bool";
				BooleanNode boolValue = new BooleanNode(boolCase);
				VariableNode constantNode = new VariableNode(boolValue,changeable,identifier.getStringValue(),type);
				matchAndRemove(Token.tokenType.COMMA);
				matchAndRemove(Token.tokenType.ENDOFLINE);
				return constantNode;
			}
			if(matchAndRemove(Token.tokenType.CHARLITERAL)!=null) {
				CharacterNode charValue = new CharacterNode(identifier.getStringValue().charAt(0));
				type = "char";
				VariableNode constantNode = new VariableNode(charValue,changeable,identifier.getStringValue(),type);
				matchAndRemove(Token.tokenType.COMMA);
				matchAndRemove(Token.tokenType.ENDOFLINE);
				return constantNode;
			}

			variableNode = new VariableNode(null,changeable,identifier.getToken().toString(),type);
		}
		return null;
	}
	
	public Node expression() throws MultipleAdditionException{//finding one or more terms, if more than one will find an operator, and constructs a mathOpNode for it	
		MathOpNode terms = null;
		Node expressionNodeLeft = term();
		if(expressionNodeLeft == null) return null;//if there is no term, then return null
		Token operator = objectList.get(0);
		if(matchAndRemove(Token.tokenType.PLUS)==null&&matchAndRemove(Token.tokenType.MINUS)==null) {
			return expressionNodeLeft;//case for there being only 1 term, will return simply the term given if there are no more
		}
		Node expressionNodeRight = term();
		if(expressionNodeRight == null) {//case for there being a sign but no other term, should be an error
			terms = new MathOpNode(expressionNodeLeft);
			setOperators(terms, operator);
			return terms;
		}
		terms = new MathOpNode(expressionNodeLeft, expressionNodeRight);//constructing a mathOpNode for 2 terms found
		setOperators(terms, operator);//method finds the tokenType token and converts it to an operator token
		
		
		//after handling cases 0,1 and 2, now we go into the infinite term case, finding infinite amounts of terms and constructing them properly
		while(peek(0)==Token.tokenType.PLUS||peek(0)==Token.tokenType.MINUS) {
			operator = objectList.get(0);
			matchAndRemove(Token.tokenType.PLUS);
			matchAndRemove(Token.tokenType.MINUS);
			expressionNodeLeft = term();
			terms = new MathOpNode(terms, expressionNodeLeft);
			setOperators(terms, operator);
		}
		return terms;	
	}
	
	public Node term() throws MultipleAdditionException {//finding a factor, then *,/, or mod another factor
		Node leftInt=null;
		Token operator=null;
		leftInt = factor();//finding a number (or a left paren which would return an expression)
		if(leftInt==null) return null;
		if(operator == null)operator = objectList.get(0);
		if(matchAndRemove(Token.tokenType.TIMES)==null && matchAndRemove(Token.tokenType.DIVIDE)==null&&matchAndRemove(Token.tokenType.MOD)==null) return leftInt;//if no "right" factor within the term, return the factor
		Node rightInt = factor();
		if(rightInt==null) {
			MathOpNode sign = new MathOpNode(leftInt);
			setOperators(sign,operator);
			return sign;
		}
		MathOpNode sign = new MathOpNode(leftInt,rightInt);//constructing case 2, a left and right node that will construct a base term
		setOperators(sign,operator);
		while(peek(0)==Token.tokenType.TIMES||peek(0)==Token.tokenType.DIVIDE||peek(0)==Token.tokenType.MOD) {//infinite term case, constructs infinite amounts of term nodes so long as syntax is proper
			operator = objectList.get(0);
			matchAndRemove(Token.tokenType.TIMES);
			matchAndRemove(Token.tokenType.DIVIDE);
			matchAndRemove(Token.tokenType.MOD);
			leftInt = factor();
			sign = new MathOpNode(sign, leftInt);
			setOperators(sign, operator);
		}
		return sign;
	}
	
	public Node factor() throws MultipleAdditionException{//usually looking for a number, but will find left parens that will yield an expression of its own
		//format: FACTOR= number or lparen EXPRESSION rparen
		Token numberToken = objectList.get(0);
		
		int isNegative = 0;//number that is correlated to a factor being positive or negative
		if(matchAndRemove(Token.tokenType.NUMBER)==null && matchAndRemove(Token.tokenType.MINUS)==null&& matchAndRemove(Token.tokenType.LPAREN)==null&&matchAndRemove(Token.tokenType.IDENTIFIER)==null)
		{ 
			return null;
		}
		if(numberToken.getToken()==Token.tokenType.IDENTIFIER) {//this is the variable reference case
			VariableReferenceNode variableReference = new VariableReferenceNode(numberToken.getStringValue(),null);
			if(peek(0)==Token.tokenType.LBRACKET) {
				matchAndRemove(Token.tokenType.LBRACKET);
				Node arrayExpression = expression();
				matchAndRemove(Token.tokenType.RBRACKET);
				variableReference.setOptionalNode(arrayExpression);
			}
			return variableReference;
		}
		if(numberToken.getToken()==Token.tokenType.PLUS) {
			throw new MultipleAdditionException("Syntax error, on token "+objectList.get(0).getToken()+", cannot use multiple addition signs.");
		}
		if(numberToken.getToken()==Token.tokenType.MINUS) {
			isNegative++;
			while(matchAndRemove(Token.tokenType.MINUS)!=null) {
				isNegative++;
			}
			numberToken = matchAndRemove(Token.tokenType.NUMBER);
		}
		if(numberToken.getToken()==Token.tokenType.LPAREN) {//should be if there is a 
			Node temp = expression();
			matchAndRemove(Token.tokenType.RPAREN);
			return temp;
		}
		
		//look for a decimal value of the number before adding as an integer need to add
		if(isDecimal(numberToken.getStringValue())) {
			//it is a real node
			float toFloat = Float.parseFloat(numberToken.getStringValue());
			if(isNegative%2==1) toFloat*=-1;
			RealNode realNode = new RealNode(toFloat);
			return realNode;
		}
		if(numberToken.getStringValue()==null) return null;
		int toInt = Integer.parseInt(numberToken.getStringValue());
		if(isNegative%2==1) toInt*=-1;//negative and positive are correlated with odd and even in this case. Odd being negative, even being positive
		IntegerNode integerNode = new IntegerNode(toInt);
		return integerNode;
	}
	
	private Token.tokenType expectsEndOfLine() throws NoEndOfLineException {//method used when endOfLine is 100% expected, will throw a user syntax error if it cannot find the endofline token
		if(matchAndRemove(Token.tokenType.ENDOFLINE)==null) {
			throw new NoEndOfLineException("Syntax error, cannot parse an invalidly typed line");
		}
		else {
			return Token.tokenType.ENDOFLINE;
		}
	}
	
	private void setOperators(MathOpNode sign, Token tokenSign) {
		
		if(tokenSign.getToken()==Token.tokenType.PLUS)
			sign.setOperator(MathOpNode.operators.PLUS);
		else if(tokenSign.getToken()==Token.tokenType.MINUS)
			sign.setOperator(MathOpNode.operators.MINUS);
		else if(tokenSign.getToken()==Token.tokenType.DIVIDE)
			sign.setOperator(MathOpNode.operators.DIVIDE);
		else if(tokenSign.getToken()==Token.tokenType.MOD)
			sign.setOperator(MathOpNode.operators.MOD);
		else if(tokenSign.getToken()==Token.tokenType.TIMES)
			sign.setOperator(MathOpNode.operators.TIMES);
	}
	
	private Token.tokenType peek(int i)  {//peeking ahead of the list, i determines how far it peeks
		if(i>objectList.size()-1) return null;
		return objectList.get(i).getToken();
	}

	public boolean isDecimal(String number) {
		for(int i = 0; i<number.length(); i++) {//checking every character looking for a decimal
			if(number.charAt(i)=='.') {
				return true;//if it finds a decimal then it returns true, else false
			}
		}
		return false;
	}
}

class NoEndOfLineException extends Exception {//if an end of line was not found
	public NoEndOfLineException(String message) {
		super(message);
	}
}

class MultipleAdditionException extends Exception{//if a user uses multiple addition signs, example: 2++2
	public MultipleAdditionException(String message) {
		super(message);
	}
}

class IncorrectIndentationException extends Exception{
	public IncorrectIndentationException(String message) {
		super(message);
	}
}

class IncorrectSyntaxException extends Exception{
	public IncorrectSyntaxException(String message) {
		super(message);
	}
}