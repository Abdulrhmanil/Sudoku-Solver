package sudokuGame;

import java.util.ArrayList;

import static javafx.scene.input.KeyCode.T;

/**
 * {@code Node} is our basic binary tree, we use this class to build our Tree-based Genetic Programing.
 * We prefer to build our tree to make the code more flexible,
 * and easier to build and call all our recursive functions.
 * @param <T> There are two types of T values we have, there are {@link Primitive }
 *           and there are {@link Terminal},and both are extended from abstract
 *           {@link TerminalOrPrimitive} class.
 * @author Abedalrhman Nsasra
 * @version 1.0
 * @see TerminalOrPrimitive
 * @see Primitive
 * @see Terminal
 */

class Node<T> {
    /**Reference of left child*/
	private Node<T> left;
	/**Reference of right child*/
    private Node<T> right;
    /**Generic T value */
    private T value;


    /**
     * This contractor create a parent {@code Node} in case we have left and right children.
     * @param left is the reference of the left child.
     * @param right is the reference of the right child.
     * @param value is the value of the {@code Node}, it could be {@link Primitive}
	 *              instance or {@link Terminal} instance.
     */
	Node(Node<T> left, Node<T> right, T value) {
		super();
		this.left = left;
		this.right = right;
		this.value = value;
	}

    /**
     * This constrictor is create only the {@code Node} with the T value
	 * and set the left and right reference to be null.
     * @param value is the value (instance) of the {@code Node} and it could be
     *              instance of {@link Primitive} or {@link Terminal} classes.
     */
	Node(T value) {
		super();
		this.left = null;
		this.right = null;
		this.value = value;
	}

    /**
     * Default constrictor that create empty {@code Node} without value and without left and right reference.
     * We use this constrictor in case we want to reset a {@code Node} without dealing with null pointer exception.
     */
	Node(){
		super();
	}

    /**
	 * Returns left child of the current {@code Node},notice: it can be null reference.
     * @return the reference of the left child.
     */
    Node<T> getLeft() {
		return left;
	}

    /**
     * Set the reference of the left child of current {@code Node}.
     * @param left is the new reference to set.
     */
	void setLeft(Node<T> left) {
		this.left = left;
	}

    /**
     * Returns right child of the current {@code Node},notice: it can be null reference.
     * @return the reference of the right child, it could be null reference.
     */
	Node<T> getRight() {
		return right;
	}

    /**
     * Set the reference of the right child of current {@code Node}.
     * @param right is the new reference to set.
     */
	void setRight(Node<T> right) {
		this.right = right;
	}

    /**
     * Returns the generic {@link #value T value} of the current {@code Node}
     * ,it's usually instance of {@link Primitive} or {@link Terminal} classes.
     * @return the value of the {@code Node}.
     */
	T getValue() {
		return value;
	}

    /**
     * Sets the generic {@link #value T value} of current {@code Node},
     * it's usually instance of {@link Primitive} or {@link Terminal} classes.
     * @param value is the value of current {@code Node} to set.
     */
	void setValue(T value) {
		this.value = value;
	}

    /**
     * Create new left child with leftValue as generic {@link #value T value} then return the left child reference.
     * @param leftValue is new generic T value of the left child of the current {@code Node}.
     * @return the new reference of the left child.
     */
	Node<T> createLeft(T leftValue) {
		this.left=new Node<T>(leftValue);
		return this.left;
	}
    /**
     * Create new right child with leftValue as generic {@link #value T value} then return the right child reference.
     * @param RightValue is new generic T value of the right child of the current {@code Node}.
     * @return the new reference of the right child.
     */
	public Node<T> createRight(T RightValue) {
		this.right=new Node<T>(RightValue);
		return this.right;
	}


    /**
     * Is't recursive private method that finds the height of the general tree in recursive way.
     * @param node is the current node that we reach in recursive function,
     *            because the function is recursive then it must have the current tree node as parameter.
     * @return the height of the tree in recursive way.
     */
	private int findTreeHeight(Node<T> node) {
	    if (node == null) {
	        return -1;
	    }

	    int leftHeight = findTreeHeight(node.left);
	    int rightHeight = findTreeHeight(node.right);

	    if (leftHeight > rightHeight) {
	        return leftHeight + 1;
	    } else {
	        return rightHeight + 1;
	    }
	}

    /**
     * Finds the height of the tree (longest path from the root to any leaf).
     * use {@link #findTreeHeight(Node) findTreeHeight} recursive function.
     * @return the height of the tree from current {@code Node} (root).
     */
	int findHeight()
	{
		return findTreeHeight(this);
	}

    /**
     * Finds the height of the left child tree, use {@link #findTreeHeight(Node) findTreeHeight} recursive function.
     * @return the height of the left child tree.
     */
	int findLeftHeight()
	{
		return findTreeHeight(this.left);
	}

    /**
     * Finds the height of the right child tree, use {@link #findTreeHeight(Node) findTreeHeight} recursive function.
     * @return the height of the right child tree.
     */
	int findRightHeight()
	{
		return findTreeHeight(this.right);
	}

}
