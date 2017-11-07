package sudokuGame;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

/**
 * {@code Individual} abstract class is representing one player (individual), set of genes,
 * that trying to solve the gaven sudoku board, the program will end if one individual success to
 * solve the sudoku board, and if NO one success to solve the sudoku then we use evolutionary algorithm
 * {@link Population#nextGeneration (next generation)} method to create new generation of individuals that
 * trying to solve the sudoku.
 * Note: there are NO major deference between {@link Individual Individual class}
 * and {@link BoardIndividual BoardIndividual class} except wo decided to design and put everything related to
 * the Tree-Based Genetic programing and recursive methods in this class.
 * And everything related to solving the board and in BoardIndividual, we could put all the methods
 * and all the variables in one class.
 * @author Abedalrhman Nsasra
 * @version 1.0
 * @see BoardIndividual
 * @see Population
 * @see Node
 * @see TerminalOrPrimitive
 * @see <a href="https://en.wikipedia.org/wiki/Genetic_programming">Genetic Programming</a>
 */
public abstract class Individual implements Cloneable, Comparable<Object>, Variable {

    /**
     * This is the ideal fitness mean that the player reach the solution,
     * in other words, there are 0 empty cells that don't contain a number,
     * in the sudoku board.
     */
    private static final int IDEAL_FITNESS = 0;

    /** This mean that the player didn't played yet*/
    private static final int NOT_PLAYED_YET =-1;

    /** ArrayList contain the function names that we support in our {@code Individual} instances */
    static ArrayList<String> functions;

    /** ArrayList contain the operation names that we support in our {@code Individual} instances */
    static ArrayList<String> operators;

    /** We need this random instance to generate randomly the first generation of individuals */
	private static Random random=new Random();

	/*Load the functions names and the operators names*/
	static {
		fillFunctions();
		fillOperators();
	}


    /**
     * Load all the functions names in {@link #functions functions ArrayList},
     * these all the functions that we support in the {@link Terminal Terminals},
     * the Terminals exist only and only in the leaves of the Tree-Based Genetic Programing.
     * Important Note: if you decide to extend the list you should support them in the
     * {@link Terminal Terminal} class and write relative methods for them,and override
     * {@link Terminal#run(int, int, int, int[][], Hashtable[][]) run} method,
     * otherwise Runtime Exception will be thrown.
     * @see Terminal
     */
    private static void fillFunctions()
    {
        functions=new ArrayList<>(12);
        functions.add("countEmptyCellInRow");
        functions.add("countEmptyCellInCol");
        functions.add("countEmptyCellInSquare");
        functions.add("numOfOptionsInCell");
        functions.add("numOfOptionsToAppearInBoard");
        functions.add("countEmptyCellsInRowsContainsNum");
        functions.add("countEmptyCellsInColsContainsNum");
        functions.add("countEmptyCellsInSquareContainsNum");
        functions.add("countEmptyCellsInRows_ThatNotContainsNum");
        functions.add("countEmptyCellsInCols_ThatNotContainsNum");
        functions.add("countEmptyCellsInSquare_ThatNotContainsNum");
    }


    /**
     * Load all the operators names in {@link #operators operators ArrayList},
     * these all the functions that we support in the {@link Primitive Primitive},
     * the Primitive can exist almost in any Node in the Tree-Based Genetic Programing.
     * Important Note: if you decide to extend the list you should support them in the
     * {@link Primitive Primitive} class and write relative methods for them, and override
     * {@link Primitive#run(int, int, int, int[][], Hashtable[][]) run} method,
     * otherwise Runtime Exception will be thrown.
     * @see Primitive
     */
    private static void fillOperators()
    {
        operators=new  ArrayList<>(8);
        operators.add("Plus");
        operators.add("Minus");
        operators.add("Multi");
        operators.add("div");
        operators.add("Mod");
        operators.add("Maximum");
        operators.add("Minimum");
    }


    /**
     * If you decide to support other functions or to choose specific functions,
     * you can use this method.
     * @param functions arrayList contain names of the function that you support in Terminals.
     */
    public static void fillFunctions(ArrayList<String> functions){
	    Individual.functions=new ArrayList<>(functions);
    }


    /**
     * If you decide to support other operators or to choose specific operators,
     * you can use this method.
     * @param operators arrayList contain names of the operator that you support in Primitive.
     */
    public static void fillOperators(ArrayList<String> operators) {
        Individual.operators= new ArrayList<>(operators);
    }


    /**
     * The fitness of the player, it mean how many empty cell
     * remain in the sudoku board. Smaller is better.
     */
    private int fitness;


    /** Tree-Based Genetic Programing*/
    protected Node<TerminalOrPrimitive> tree;


    /** The height of the tree*/
    private int height;


    /**
     * This method is evaluate the fitness of the individual (how much good this individual),
     * and return Integer num that represent, how much good is the player (smaller is better)
     * 0 result mean zero empty cells remain to solve the sudoku board without conflict,
     * in other word the player (individual) succeed to solve the sudoku.
     * @return the fitness of the player (Individual).
     */
    protected abstract int evaluate();


    /**
     * Initialize {@code Individual} instance, the instance will try to solve the sudoku,
     * in GP way.
     * @param height is the height of the tree that the individual contain, be careful when choosing
     *               the height number,very high number (grater than 14) will slow the performance.
     */
    public Individual(int height) {
		this.fitness =NOT_PLAYED_YET;
		setHeight(height);
		this.tree=generateFullTree(this.height);
	}


    /**
     * Get the fitness of the player (individual), if the player have been played,
     * if the player (individual) did't played yet then let hem try to solve the sudoku
     * and {@link #evaluate() evaluate} the fitness.
     * @return the fitness of the individual
     */
	int getFitness() {
		if (fitness==NOT_PLAYED_YET)
			fitness = evaluate();
		return fitness;
	}


    /**
     * This method compute the grade for the specified number (key parameter) that can inserted (without conflict),
     * in the specified empty cells (board[row, col] parameters).
     * The grades are computed according to the {@link #tree tree}, the tree give the specified option,
     * in the specified index their grade.
     * @param row the specific row index that we want to compute the grade for the options
     *            that can inserted in this index.
     * @param col the specific column index that we want to compute the grade for the options
     *            that can inserted in this index.
     * @param key the number that we want to compute his grade as a solution in the board in board[row, col].
     * @param board 2D array that represent the sudoku board.
     * @param gradeboard reference to matrix of hash tables that represent in every index in the matrix
     *                   which numbers can be inserted without conflict as a keys in the hash table and
     *                   grade as a value for every key that represent how good this option as solution,
     *                   we choose smaller grade is better but you could choose larger is better,
     *                   it will not change the solution.
     * @return a grade for the key (number) as a solution in the index board[row, col],
     * the return value is a grade to tell us how much good this solution.
     */
    double run(final int row, final int col, final int key, final int[][] board,
               final Hashtable<Integer,Double> [][]  gradeboard)
    {
        return tree.getValue().run(row, col, key, board, gradeboard);
    }


    /**
     * Set the height of the tree, we decide to make this setter to avoid height with less than 1,
     * so here we can check and monitor the the height.
     * @param height is the height of the tree we want to set.
     * @throws RuntimeException if the height less than 1.
     */
    protected void setHeight(int height) {
        if(height <1)
            throw new RuntimeException("Something wrong with the tree height, cant's be less than 1");
        else
            this.height = height;
    }


    /**
     * Get the height of the tree.
     * @return the height of the tree.
     */
    int getHeight() {
        return height;
    }


    /**
     * Ideal individual is the individual that succeed to solve the sudoku,
     * in other words the ideal individual is the one who remain zero empty cell
     * until he solve the sudoku board, so here check if the individual succeed to
     * solve sudoku.
     * @return true if the player (individual) succeed to solve the sudoku, false otherwise.
     */
    boolean isIdeal() {
        return getFitness() == IDEAL_FITNESS;
    }


    /**
     * Get the root of the tree.
     * @return the root of the tree-based GP in the current individual.
     */
    Node<TerminalOrPrimitive> getTree() {
        return tree;
    }


    /**
     * Clone the tree (deep copy) and return reference to the new clone,
     * use {@link #copyFullTree(Node) copyFullTree} as a help method.
     * @return a reference to the new clone of the tree.
     */
    Node<TerminalOrPrimitive> cloneFullTree()
    {
        return copyFullTree(this.tree);
    }


    /**
     * Convert the tree-based GP to Prefix expression in a {@code String},
     * that representing the tree. This method use
     * {@link #ConvertTreeToPrefixExpression(Node) ConvertTreeToPrefixExpression}
     * as help method.
     * @return a string expression that represent the tree, so we use it to create report..
     */
    String treeAsPrefixExpression() {
        return ConvertTreeToPrefixExpression(this.tree).toString();
    }


    /**
     * Convert the tree-based GP to infix expression in a {@code String},
     * that representing the tree. This method use
     * {@link #ConvertTreeToInfixExpression(Node) ConvertTreeToInfixExpression}
     * as help method.
     * @return a string expression that represent the tree, so we use it to create report.
     */
    String treeAsInfixExpression()
    {
        return ConvertTreeToInfixExpression(this.tree).toString();
    }


    /**
     * Be careful before using this method , this method is calculate the tree height
     * in recourse way, we use it only and only when we do crossover between two trees,
     * so we can calculate in runtime the height of the new tree to update height field.
     * This method is expensive, we use it only when we do crossover.
     * @return the height of the tree after scanning all the tree nodes
     * and compute the real height.
     */
    int findHeight()
	{
		return tree.findHeight();
	}


    /**
     * Generate new full tree-based Genetic Programing, using
     * {@link #createFullTree(int, Node) createFullTree} as help method.
     * The tree is totally new tree, NO shared reference.
     * @param height is the height of the tree that you want to generate.
     * @return the root of generated tree.
     */
    Node<TerminalOrPrimitive> generateFullTree(int height) {
        Node<TerminalOrPrimitive> root=new Node<>();
        createFullTree(height,root);
        return root;
    }


    /**
     * generate the tree of the current instance from the beginning,
     * totally new generating, using {@link #generateFullTree(int) generateFullTree},
     * with the same instance {@link #height height}.
     * NO optimizing for the implantation of this method, we totally create new tree,
     * if you want to optimize the memory and get better performance, you should
     * reimplement this method.
     */
    public void reGenerateFullTree() {
        this.tree=generateFullTree(this.height);
    }


    /**
     * Generate full tree-based Genetic Programing, receive a height and root node, must be NOT null,
     * and generate a random primitive instance for the root, in case the height is greater that 0,
     * and then call {@link #createSubTree(int, Node) createSubTree} with root node,
     * and height as arguments.
     * In case the required height is 0 (it's mean we need tree with only one node),
     * the method will generate a random {@code Terminal} instance for the node, and done.
     * This method use {@link #createSubTree(int, Node) createSubTree} recursive method
     * as a help method, that create the rest of the tree in recursive calling.
     * @param height the height of the tree that you want to generate.
     * @param node the root of the tree you want to generate, the root must be NOT null.
     */
    protected void createFullTree(int height ,@NotNull Node<TerminalOrPrimitive> node ) {

        if(height>0)
        {
            int randNum=random.nextInt(operators.size());
            node.setValue((new Primitive(operators.get(randNum))));
            // Run recursive help method.
            createSubTree(height, node);
        }
        else
        {
            if(height==0)
            {
                int randNum=random.nextInt(functions.size());
                node.setValue(new Terminal(functions.get(randNum)));
                node.setLeft(null);
                node.setRight(null);
                System.out.println("They create terminal here, you probably created a tree with 1 height.");
            }
            else
                throw new RuntimeException("You cant's create a tree with NEGATIVE height");
        }
    }


    /**
     * Recursive method that create a sub tree, you must send her NOT null reference,
     * as a root node and then this method will generate a full tree with gaven height.
     * This method is a recursive, so we prefer to implement it in a functional style.
     * @param height the height of the tree that we want to generate in a recursive way.
     * @param node the root node, this root must be NOT null, you must create the root
     *             externally and send it as a parameter.
     */
    private void createSubTree(int height , Node<TerminalOrPrimitive> node ) {
		int randNum;
		if(height>1)
		{
			randNum=random.nextInt(operators.size());
			node.createLeft(new Primitive(operators.get(randNum)));
			((Primitive)node.getValue()).setLeft(node.getLeft());

			createSubTree(height-1 , node.getLeft());

			randNum=random.nextInt(operators.size());
			node.createRight(new Primitive(operators.get(randNum)));
			((Primitive)node.getValue()).setRight(node.getRight());

			createSubTree(height-1 , node.getRight());
		}
		else
		{
			if( height==1)
			{
				randNum=random.nextInt(functions.size());
				node.createLeft(new Terminal(functions.get(randNum)));
				((Primitive)node.getValue()).setLeft(node.getLeft());

				randNum=random.nextInt(functions.size());
				node.createRight(new Terminal(functions.get(randNum)));
				((Primitive)node.getValue()).setRight(node.getRight());

			}
		}
	}


    /**
     * receive a source root node as a parameter and then clone the whole tree and return it.
     * The cloning is deep cloning so we create totally new tree.
     * This method use {@link #copySubTree(Node, Node) copySubTree} as a help method,
     * here we clone only root node and call {@link #copySubTree(Node, Node) copySubTree} with
     * the new node of the root and the old root (the actual tree) as a parameters to let the
     * {@link #copySubTree(Node, Node) copySubTree} method to do the rest of the work (cloning).
     * @param sourceNode the reference of the source root that we want to clone.
     * @return totally new tree as a result of cloning.
     */
     protected Node<TerminalOrPrimitive> copyFullTree( Node<TerminalOrPrimitive> sourceNode) {
        if(sourceNode!=null)
        {
            Node<TerminalOrPrimitive>  decisionNode = new Node<>(sourceNode.getValue().clone());
            copySubTree(sourceNode,decisionNode);
            return decisionNode;
        }
        else
            return null;
    }


    /**
     * This method receive as arguments source node and decision node and clone the content
     * of the source node into decision node, the coping is done in a recursive way,
     * so we sent the two trees in the method as parameters.
     * If decisionNode is NOT only root(NOT only one node) then the method will ignore and throw all
     * the rest of the tree to GC.
     * @param sourceNode is the reference of the root that we want to copy.
     * @param decisionNode is the reference of the root that hold the copied tree.
     */
    private static void copySubTree(final Node<TerminalOrPrimitive> sourceNode
            ,Node<TerminalOrPrimitive> decisionNode ) {
		if(sourceNode!=null)
		{
			if(sourceNode.getLeft()!=null)
			{
				decisionNode.createLeft(sourceNode.getLeft().getValue().clone());
				((Primitive)decisionNode.getValue()).setLeft(decisionNode.getLeft());
				
				copySubTree(sourceNode.getLeft(),decisionNode.getLeft());
			}
			
			if(sourceNode.getRight()!=null)
			{
				decisionNode.createRight(sourceNode.getRight().getValue().clone());
				((Primitive)decisionNode.getValue()).setRight(decisionNode.getRight());
				
				copySubTree(sourceNode.getRight(),decisionNode.getRight());
			}
		}
	}


    /**
     * Build a string that represent out tree-Based GP in prefix expression,
     * the building is done in recursive way, and in efficient way.
     * we return in the end {@code StringBuilder} instance, to get the the {@code String}
     * you need just to call {@link StringBuilder#toString() toString} method and you will
     * get the {@code String}, remember this is just help method.
     * @param node is the reference of the node that we reach in the recursive calling.
     * @return StringBuilder instance that represent out tree in prefix expression.
     */
    private StringBuilder ConvertTreeToPrefixExpression(Node<TerminalOrPrimitive> node)
    {
        StringBuilder st = new StringBuilder(node.getValue().getOperationName());
        if (node.getLeft() != null)
        {
            st.append("( ");
            st.append(ConvertTreeToPrefixExpression(node.getLeft()));
            st.append("  ");
        }

        if (node.getRight() != null)
        {
            st.append(ConvertTreeToPrefixExpression(node.getRight()));
            st.append(" )");
        }
        return st;
    }


    /**
     * Build a string that represent out tree-Based GP in infix expression,
     * the building is done in recursive way, and in efficient way.
     * we return in the end {@code StringBuilder} instance, to get the the {@code String}
     * you need just to call {@link StringBuilder#toString() toString} method and you will
     * get the {@code String}, remember this is just help method.
     * @param node is the reference of the node that we reach in the recursive calling.
     * @return StringBuilder instance that represent out tree in infix expression.
     */
    private StringBuilder ConvertTreeToInfixExpression(Node<TerminalOrPrimitive> node)
    {
        StringBuilder st = new StringBuilder();
        if (node.getLeft() != null) {
            st.append("(");
            st.append(ConvertTreeToInfixExpression(node.getLeft()));
        }
        st.append(" ").append(ConvertFromFunctionToOperator(node.getValue().getOperationName()))
                .append(" ");
        if (node.getRight() != null) {
            st.append(ConvertTreeToInfixExpression(node.getRight()));
            st.append(")");
        }
        return st;
    }


    /**
     * receive String as a parameter that represent the operation name,
     * and then return the operator as a String.
     * For example (if received plus then return +).
     * We could implement an elegant way but we preferred a simple one.
     * We use this method only for creating a string expression that represent
     * the tree.
     * @param operationName is a String that represent the operation name.
     * @return the operator as a String
     */
    private String ConvertFromFunctionToOperator(String operationName)
    {
        switch (operationName){
            case "Plus":
                return 	"+";
            case "Minus":
                return "-";
            case "Multi":
                return "*";
            case "div":
                return "/";
            case "Mod":
                return "%";
            case "Maximum":
                return "Max";
            case "Minimum":
                return "Min";
            default :
                return operationName;
        }
    }


    /**
     * Compare this instance to obj instance, because we want to sort these objects.
     * return 1 if the fitness of this is grater that obj, 0 if they are equal,
     * -1 if the fitness of obj is grater than this.
     * @param obj is {@code Individual} instance that we want to compare it to this,
     *            in a matter of sorting.
     * @return 1 if the fitness of this instance is grater that obj, 0 if the fitness of both are equal,
     * -1 if the fitness of obj is grater than the fitness of this instance.
     */
    @Override
    public int compareTo(Object obj) {
        Individual other = (Individual) obj;
        return Integer.compare(getFitness(), other.getFitness());
    }


    /**
     * toString method that create a {@code String} that represent the {@code Individual} instance,
     * we use {@link #treeAsPrefixExpression() treeAsPrefixExpression} method,
     * and {@link #treeAsInfixExpression() treeAsInfixExpression} method.
     * @return a string that represent the instance.
     */
    @Override
    public String toString() {
        return "The tree as Prefix Sequence : \n" +
                treeAsPrefixExpression() +
                "\n\n" +
                "The tree as Infix sequence : \n" +
                treeAsInfixExpression();
    }


    /**
     * Clone the current instance (deep copy), and return the copied instance.
     * @return a new instance (deep copy), that equal to the current (this) instance.
     */
    @Override
    public Individual clone() {
        try {
            Individual copy = (Individual) super.clone();
            copy.setHeight(height);
            copy.tree=cloneFullTree();
            copy.fitness = NOT_PLAYED_YET;
            return copy;

        }
        catch (CloneNotSupportedException e) {
            // should NOT happen because we are cloneable.
            throw new RuntimeException("Unexpected error While copying the individual",e);
        }
    }
}