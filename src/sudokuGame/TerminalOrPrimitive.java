package sudokuGame;

import java.util.Hashtable;

/**
 * {@code TerminalOrPrimitive} abstract class is represent node in our Tree-based Genetic Programing.
 * And is's an implementation to the generic {@link Node#value T value} in the {@link Node} class
 * in our tree (Tree-based Genetic Programing).
 *
 * <p>The {@link Node#value T value} in the {@link Node} class can be {@link Primitive} or {@link Terminal}.
 * {@link Primitive} and {@link Terminal} classes are extend {@link TerminalOrPrimitive} and implement
 * the abstract method {@link #run(int, int, int, int[][], Hashtable[][]) run}.
 * The abstract {@link #run(int, int, int, int[][], Hashtable[][]) run} method
 * must be implemented in a way that calculate the tree in a recursive way,
 * the {@link Primitive#run(int, int, int, int[][], Hashtable[][]) run}
 * in {@link Primitive} class do the operation and call the
 * {@link Primitive#run(int, int, int, int[][], Hashtable[][]) run}
 * method in the left and right children,
 * and in the {@link Terminal} class it's do the calculation and return the value
 * (based on {@link TerminalOrPrimitive#operationName operationName}).
 *
 * <p>This class is the one of the basis and the most important classes that create the tree structure.
 * This class must implement Cloneable Interface because we need clone the trees in the future,
 * so we need to clone the instances of {@link Node} that contain {@link TerminalOrPrimitive}
 * as a generic {@link Node#value T value}, so we need also to clone the generic {@link Node#value T value}
 * in {@link Node} class, that could be {@link Primitive} or {@link Terminal} instances.
 * @author Abedalrhman Nsasra
 * @version 1.0
 * @see Node
 * @see Primitive
 * @see Terminal
 * @see <a href="https://en.wikipedia.org/wiki/Genetic_programming">Genetic Programming</a>
 */
public abstract class TerminalOrPrimitive implements Cloneable{
    /**
     * A string that determine if the T value in {@code Node} is a Terminal Or Primitive.
     * We choose a simple way to to know in runtime if the node are Terminal or Primitive.
     * (We could implement an elegant way but we briefer a simple one).
     */
    protected final String nodeType;

    /** The function or operator that should run at runtime */
    protected String operationName;

    /**
	 * This abstract method is responsible to calculate the Tree-based genetic programing,
	 * this method is abstract because {@code Primitive} and {@code Terminal} need to implemented in a different ways.
	 * Notice : when this method is called, the whole options with their mapping grades are calculated.
	 * Important note: we use these parameters only in {@code Terminal} class, but we need to pass them in a recursive
	 * way to the leaves ({@code Terminal} instances), so we receive them as a parameters.
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
	 * @see Terminal#run(int, int, int, int[][], Hashtable[][])
	 * @see Primitive#run(int, int, int, int[][], Hashtable[][])
     */
	abstract double run(final int row, final int col, final int key, final int[][] board
			, final Hashtable<Integer,Double> [][]  gradeBoard);

	/**
	 * Constructor that initialize the class members.
	 * @param nodeType String that determined if the T value in {@code Node} is a Terminal Or Primitive,
	 *                 so we can know in runtime the type of the T value in {@code Node} class.
	 * @param operationName String that determine which operation or function should run in the tree,(genetic algorithm)
	 */
	public TerminalOrPrimitive(String nodeType, String operationName) {
		this.nodeType = nodeType;
		this.operationName=operationName;
	}


    /**
     * Returns the node type as string value, and is's immutable and determined in the constructor.
     * @return the node type in runtime.
     */
	public String getNodeType() {
		return nodeType;
	}


	/**
     * Returns the operation name as string value, and is's immutable and determined in the constructor.
     * @return operation name in runtime (getter)
     * */
	public String getOperationName() {
		return operationName;
	}


	/**
     * Make a clone of the instance (deep copy) and return it.
	 * @return a clone of {@code TerminalOrPrimitive} instance, We create new instance (deep copy).
	 */
	@Override
	protected TerminalOrPrimitive clone() {
        try {
            return (TerminalOrPrimitive) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Unexpected Error in clone method");
        }
    }

	/**
     * Check if the type of the instance is {@code Terminal}.
	 * @return {@code true} if the instance is {@code Terminal},{@code false} otherwise.
	 */
	public boolean isTerminal()
	{
		return this.nodeType.equals("Terminal");
	}


	/**
     * Check if the type of the instance is {@code Primitive}.
	 * @return {@code true} if the instance is {@code Primitive},{@code false} otherwise.
	 */
	public boolean isPrimitive()
	{
		return this.nodeType.equals("Primitive");
	}
}
