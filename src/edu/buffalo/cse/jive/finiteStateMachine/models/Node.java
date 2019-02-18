package edu.buffalo.cse.jive.finiteStateMachine.models;
/**
 * @author Shashank Raghunath
 * @email sraghuna@buffalo.edu
 *
 */
public class Node<T> implements Comparable<T>{

	private T data;
	private Node<T> left;
	private Node<T> right;

	public Node(T data) {
		super();
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Node<T> getLeft() {
		return left;
	}

	public void setLeft(Node<T> left) {
		this.left = left;
	}

	public Node<T> getRight() {
		return right;
	}

	public void setRight(Node<T> right) {
		this.right = right;
	}

	public Node() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(T o) {
		// TODO Compare based on the Operators.OPERATOR_PRECEDENCE MAP
		if(o instanceof String) {
			
		}
		return 0;
	}

}
