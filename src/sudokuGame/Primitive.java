package sudokuGame;

import java.util.Hashtable;

/**
 * {@code Primitive} class is representing the the primitive operating (primitive node)
 * in our Tree-based Genetic Programing. And we use this class as a generic
 * {@link Node#value T value} in the {@link Node Node} class.
 * The primitive set that we support is : +, -, *, /, %, Max, Min, But the list can be extended.
 * @author Abedalrhman Nsasra
 * @version 1.0
 * @see Node
 * @see TerminalOrPrimitive
 * @see Terminal
 * @see <a href="https://en.wikipedia.org/wiki/Genetic_programming">Genetic Programming</a>
 */
public class Primitive extends TerminalOrPrimitive {

    /**
     * Reference to left child so we can call
     * {@link #run(int, int, int, int[][], Hashtable[][]) run method}
     * on left child in recursive way.
     */
	private Node<TerminalOrPrimitive> left=null;

    /**
     * Reference to right child so we can call
     * {@link #run(int, int, int, int[][], Hashtable[][]) run method}
     * on right child in recursive way.
     */
	private Node<TerminalOrPrimitive> right=null;


    /**
     * Constructor that initialize {@code Primitive} instance,
     * and always determine {@code "Primitive"} {@code String} as {@link #nodeType nodeType}.
     * @param operationName is determine the operation of the {@code Primitive} {@code Node}
     *                      in our Tree-based Genetic Programing.
     *                      The operation could be: +, -, *, /, %, Max, Min.
     */
	public Primitive(String operationName) {
		super("Primitive",operationName);
	}


    /**
     * We need to clone the tree in the future, so we need to clone
     * {@link Node#value T value} in {@link Node} Class.
     * So we cloning {@code Primitive} instances.
     * @return a clone of {@code Primitive} instance.
     */
	@Override
	protected Primitive clone() {
		Primitive copy=(Primitive) super.clone();
		copy.left=null;
		copy.right=null;
		return copy;
	}

    /**
     * Set pointer to the left child, so we can call
     * {@link #run(int, int, int, int[][], Hashtable[][]) run} method.
     * @param left is the reference that we want set.
     */
	public void setLeft(Node<TerminalOrPrimitive> left) {
		this.left = left;
	}

    /**
     * Set pointer to the right child, so we can call
     * {@link #run(int, int, int, int[][], Hashtable[][]) run} method.
     * @param right is the reference that we want set.
     */
	public void setRight(Node<TerminalOrPrimitive> right) {
		this.right = right;
	}


    /**
     * Call run method on the left and right children in recursive way,
     * and then do the operation (+, -, *, /, %, Max, Min) on the result
     * of the grade child and the right child, notice that all the calls
     * are recursive on the left child and the right child.
     * Notice: that all the operations are binary operation,
     * because our Tree-based Genetic Programing is binary.
     * @param row The index of the row in the Sudoku board.
     * @param col The index of the column in the Sudoku board.
     * @param key One of the options (number 1-9) that can be inserted in [row,col] in the board without conflict.
     * @param board Reference to the Sudoku board matrix with size 9x9, we don't copy the board it's just a reference.
     * @param gradeBoard Reference to matrix of hash tables that represent in every index in the matrix
     *                   which numbers can be inserted without conflict as a keys in the hash table and
     *                   grade as a value for every key that represent how good this option as solution,
     *                   we choose smaller grade is better but you could choose larger is better,
     *                   it will not change the solution.
     * @return a grade that represent how much good the key parameter as solution in [row,col] index in Sudoku board,
     * we have determined smaller is a better grade.
     */
	@Override
	double run(final int row, final int col, final int key, final  int[][] board,
               final Hashtable<Integer,Double> [][] gradeBoard) {
		
		switch (this.operationName){
		case "Plus":
			return 	Plus(left.getValue().run(row, col, key,board, gradeBoard)
					,right.getValue().run(row, col, key,board, gradeBoard));
			
		case "Minus":
			return Minus(left.getValue().run(row, col, key,board, gradeBoard)
					,right.getValue().run(row, col, key,board, gradeBoard));

		case "Multi":
			return Multi(left.getValue().run(row, col, key,board, gradeBoard)
					,right.getValue().run(row, col, key,board, gradeBoard));
			
		case "div":
			return div(left.getValue().run(row, col, key,board, gradeBoard)
					,right.getValue().run(row, col, key,board, gradeBoard));
			
		case "Mod":
			return Mod(left.getValue().run(row, col, key,board, gradeBoard)
					,right.getValue().run(row, col, key,board, gradeBoard));
			
			
		case "Maximum":
			return Maximum(left.getValue().run(row, col, key,board, gradeBoard)
					,right.getValue().run(row, col, key,board, gradeBoard));
			
		case "Minimum":
			return Minimum(left.getValue().run(row, col, key,board, gradeBoard)
					,right.getValue().run(row, col, key,board, gradeBoard));

		default :
		    /*This should Not happen unless you add operations in SudokuUtil::fillOperators*/
			throw new RuntimeException("Operation is Not supported, you should add it here");
		}
	}


    /* All our operators that we support */

    /**
     * Plus operator (+) that add leftValue to rightValue, and return it.
     * @param leftValue is the left value of binary operator (+).
     * @param rightValue is the right value of binary operator (+).
     * @return the sum of left value and right value
     */
	private double Plus(double leftValue,double rightValue)
	{
		return leftValue+rightValue;
	}


    /**
     * Minus operator (-) that calculate the deference between leftValue and rightValue and return it.
     * Notice: the deference is always positive.
     * @param leftValue is the left value of binary operator (-).
     * @param rightValue is the right value of binary operator (-).
     * @return the deference between leftValue and rightValue.
     */
	private double Minus(double leftValue,double rightValue)
	{
		return Math.abs(leftValue-rightValue);
	}


    /**
     * Multiplication operator (*) that multi leftValue in rightValue and return it.
     * @param leftValue is the left value of binary operator (*).
     * @param rightValue is the right value of binary operator (*).
     * @return the multiplication of leftValue and rightValue.
     */
	private double Multi(double leftValue,double rightValue)
	{
		return leftValue*rightValue;
	}


    /**
     * Division operator (/) that calculate leftValue divide rightValue, (leftValue / rightValue)
     * if rightValue is NOT equal to ZERO, other wise return the leftValue.
     * Important note: the reason we doing this because we avoiding to deal
     * with {@link Double#NaN Not a Number}.
     * @param leftValue is the left value of binary operator (/).
     * @param rightValue is the right value of binary operator (/).
     * @return the result of: leftValue divide rightValue (leftValue / rightValue),
     * if rightValue is NOT equal to zero.
     */
	private double div(double leftValue,double rightValue)
	{
        return (rightValue != 0) ? leftValue / rightValue : leftValue;
	}


    /**
     * Modulo operator (%) that calculate the leftValue modulo right rightValue, (leftValue % rightValue)
     * if rightValue is NOT equal to zero, other wise return the leftValue.
     * Important note: the reason we doing this because we avoiding to deal
     * with {@link Double#NaN Not a Number}.
     * @param leftValue is the left value of binary operator (%).
     * @param rightValue is the right value of binary operator (%).
     * @return the result of: leftValue modulo rightValue (leftValue % rightValue),
     * if rightValue is NOT equal to zero.
     */
	private double Mod(double leftValue,double rightValue)
	{
        return (rightValue != 0) ? leftValue % rightValue : leftValue;
	}


    /**
     * Choose the maximum value of leftValue and rightValue parameters, and return it.
     * @param leftValue is the left value of binary max operator.
     * @param rightValue is the right value of binary max operator.
     * @return the maximum value of leftValue and rightValue.
     */
	private double Maximum(double leftValue,double rightValue)
	{
        return (leftValue >= rightValue) ? leftValue : rightValue;
	}


    /**
     * Choose the minimum value of leftValue and rightValue parameters, and return it.
     * @param leftValue is the left value of binary min operator.
     * @param rightValue is the right value of binary min operator.
     * @return the minimum value of leftValue and rightValue.
     */
	private double Minimum(double leftValue,double rightValue)
	{
        return (leftValue <= rightValue) ? leftValue : rightValue;
	}
}