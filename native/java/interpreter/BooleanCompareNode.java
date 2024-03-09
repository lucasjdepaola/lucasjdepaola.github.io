public class BooleanCompareNode extends Node {
	
	public BooleanCompareNode(Node left, Node right, compareType compare) {
		this.setLeft(left);
		this.setRight(right);
		this.setCompare(compare);
		
	}
	
	public Node getLeft() {
		return left;
	}
	public void setLeft(Node left) {
		this.left = left;
	}

	public Node getRight() {
		return right;
	}

	public void setRight(Node right) {
		this.right = right;
	}

	public compareType getCompare() {
		return compare;
	}

	public void setCompare(compareType compare) {
		this.compare = compare;
	}

	public enum compareType{
		NOTEQUAL, ASSIGNMENT, LESSTHAN, GREATHERTHAN, LESSTHANEQUAL, GREATHERTHANEQUAL, EQUAL, 
	}
	private Node left;
	private Node right;
	private compareType compare;
	public String toString() {
		String returnValue = "";
		returnValue += "BooleanCompareNode(" + left + " "+compare+" " + right + ")";
		return returnValue;
	}
}