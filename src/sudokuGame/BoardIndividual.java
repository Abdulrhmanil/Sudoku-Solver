package sudokuGame;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * {@code BoardIndividual} is representing the player that trying
 * to solve the sudoku board, this class is extend {@code Individual} abstract class
 * and implement the abstract method {@link #evaluate() evaluate}
 * that call {@link #play() play} method that
 * will try to solve the sudoku board according to the {@link #tree tree-based GP},
 * and return the fitness of the {@code BoardIndividual}.
 * The fitness is the amount of the empty cells that remain until solve the whole sudoku board,
 * remain zero cells mean that we succeed to solve the sudoku board and it's
 * the {@link #IDEAL_FITNESS ideal fitness}.
 * @author Abedalrhman Nsasra
 * @version 1.0
 * @see Individual
 * @see Population
 * @see <a href="https://en.wikipedia.org/wiki/Genetic_programming">Genetic Programming</a>
 */
public class BoardIndividual extends Individual {


    /**
     * Reference to the original sudoku board,
     * we need while creating the new generation,
     * it's important NOT to change the content of the board,
     * it's should be immutable
     */
    private final int[][] originalSudoku;

    /**
     * 2D array that hold the sudoku board
     */
    private int[][] board;

    /**
     * 2D array (matrix) that hold in every cell a hash table,
     * the hash table holds as a keys the possible number that
     * can inserted without conflict ,and as a values grades
     * for the keys that represent how much good the key as a solution.
     */
    private Hashtable<Integer, Double>[][] gradeboard;


    /**
     * First we check if the board's dimension is NxN when sqrt(N) is natural number,
     * and then we initialize the fields and build the instance of {@code BoardIndividual}.
     * @param height is the height of the tree that we want to hold.
     * @param board is reference of the sudoku that we want to solve.
     */
    @SuppressWarnings("unchecked")
    public BoardIndividual(int height, int[][] board) {
        super(height);
        /* We need to test board's dimensions*/
        this.testIfGoodDimensionBoard(board);

        this.originalSudoku = board;
        this.board = new int[board.length][];
        this.gradeboard = new Hashtable[board.length][];

        for (int i = 0; i < board.length; i++) {
            this.board[i] = new int[board[i].length];
            this.gradeboard[i] = new Hashtable[board[i].length];
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                this.board[i][j] = board[i][j];
                this.gradeboard[i][j] = new Hashtable<>();
            }
        }
    }



    /**
     * Check if we still can solve more cells without conflict,
     * return true if we can forward solving, false otherwise.
     * @return true if we can solve and put numbers in empty cells without conflict,
     * false otherwise.
     */
    boolean isForward() {
        for (int i = 0; i < gradeboard.length; i++) {
            for (int j = 0; j < gradeboard[i].length; j++) {
                if (!gradeboard[i][j].isEmpty())
                    return true;
            }
        }
        return false;
    }


    /**
     * Try to solve the sudoku board according to the tree-based GP,
     * until you can't forward without conflict, and then return
     * the amount of the remaining empty cell, the fitness of the individual.
     * @return the amount of the remaining empty cell after trying to solve the board,
     * in other words it's the fitness of the individual.
     */
    int play() {
        initializeGradeboard();
        int fitness = countEmptyCellInSudoku();
        while (isForward()) {
            evaluateGradeboard();

            double min = Double.MAX_VALUE;
            int x = -1, y = -1, minKey = 0;

            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (!gradeboard[i][j].isEmpty() && board[i][j] == 0) {
                        Enumeration<Integer> keys = this.gradeboard[i][j].keys();
                        while (keys.hasMoreElements()) {
                            int key = keys.nextElement();
                            double tmpMin = this.gradeboard[i][j].get(key);
                            if (tmpMin < min) {
                                minKey = key;
                                x = i;
                                y = j;
                                min = tmpMin;
                            }
                        }
                    }
                }
            }
            if (x != -1 && y != -1 && minKey != 0) {
                this.board[x][y] = minKey;
                gradeboard[x][y].clear();
                fitness--;
            }
            initializeGradeboard();
        }
        return fitness;
    }


    /**
     * Evaluate the {@link #gradeboard gradeboard} (2D array)
     * that hold in every cell a hash table (map),
     * the hash table holds as a keys the possible numbers that
     * can inserted without conflict in the current index,
     * and as a values grades for the keys that represent how much good
     * is the key as a solution. Smaller is better.
     * So this method is evaluate and build this gradeboard.
     */
    void evaluateGradeboard() {
        for (int i = 0; i < gradeboard.length; i++) {
            for (int j = 0; j < gradeboard[i].length; j++) {
                Enumeration<Integer> keys = this.gradeboard[i][j].keys();
                while (keys.hasMoreElements()) {
                    int key = keys.nextElement();
                    double value = run(i, j, key, board, gradeboard);
                    this.gradeboard[i][j].put(key, value);
                }
            }
        }
    }


    /**
     * Find which numbers can inserted in every cell without conflict,
     * with the solved cells, and saves these number in {@link #gradeboard gradeboard}.
     * The implementation is NOT efficient, we can optimize these code.
     */
    void initializeGradeboard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (this.board[i][j] == 0) {
                    for (int k = 1; k <= board.length; k++) {
                        this.gradeboard[i][j].put(k, Double.NaN);
                    }
                }
            }
        }
        for (int i = 0; i < gradeboard.length; i++) {
            for (int j = 0; j < gradeboard[i].length; j++) {
                if (!(gradeboard[i][j].isEmpty())) {
                    for (int k = 1; k <= board.length; k++) {
                        if (existInRowColSquare(i, j, k))
                            this.gradeboard[i][j].remove(k);
                    }
                }
            }
        }
    }


    /**
     * Check if the gaven number is exit in the gaven row or column or in the square
     * that border these index [row,col].
     * Return true if it's already there, false otherwise.
     * @param row is the index of the row that we want to check if the num parameter exist in it.
     * @param col is the index of the column that we want to check if the num parameter exist in it.
     * @param num is the possible number that we want to check, if we can insert it in the index [row, col]
     *            without conflict.
     * @return true if we can NOT insert num in index [row, col] (already exist),
     * false otherwise.
     */
    boolean existInRowColSquare(int row, int col, int num) {
        return Terminal.existInRow(row, num, board) ||
                Terminal.existInCol(col, num, board) ||
                Terminal.existInSquare(row, col, num, board);
    }


    /**
     * count the empty cells in the sudoku board, after progress, and return it
     * @return the amount of the empty cells in the sudoku board.
     */
    int countEmptyCellInSudoku() {
        return Terminal.countEmptyCellInSudoku(this.board);
    }


    /**
     * Count the empty cells in the original sudoku board, and return it.
     * @return the amount of the empty cells in the original sudoku board.
     */
    int countEmptyCellInOriginalSudoku() {
        return Terminal.countEmptyCellInSudoku(this.originalSudoku);
    }


    /**
     * Evaluate the fitness of the current individual by letting him try
     * to play and solve the sudoku board. the fitness is the remaining empty cells.
     * @return the fitness of the current (this) individual.
     */
    @Override
    protected int evaluate() {
        return play();
    }


    /**
     * Clone the current instance (deep copy),
     * and then reset the solving board copying the original board.
     * @return new instance (deep copy) that equal to the current instance.
     */
    @SuppressWarnings("unchecked")
    @Override
    public BoardIndividual clone() {
        BoardIndividual copy = (BoardIndividual) super.clone();
        copy.board = new int[board.length][];
        copy.gradeboard = new Hashtable[gradeboard.length][];

        for (int i = 0; i < copy.board.length; i++) {
            copy.board[i] = new int[board[i].length];
            copy.gradeboard[i] = new Hashtable[gradeboard[i].length];
        }
        for (int i = 0; i < copy.board.length; i++) {
            for (int j = 0; j < copy.board[i].length; j++) {
                copy.board[i][j] = copy.originalSudoku[i][j];
                copy.gradeboard[i][j] = new Hashtable<>();
            }
        }
        return copy;
    }


    /**
     * Very important NOT to change this method unless you know exactly what you do.
     * Mutate the tree-based GP by changing randomly a part of the tree.
     * The Mutation is NOT less important than the crossover method.
     * @return a new clone of the current instance with some mutation,
     * (random change in the tree-based GP).
     */
    @Override
    public Individual mutate() {
        BoardIndividual copy = clone();
        int treeHeight = copy.tree.findHeight();
        int changeInDeep = nextInc1ExcMax(treeHeight);
        int deep;
        Node<TerminalOrPrimitive> mover = copy.tree;
        Node<TerminalOrPrimitive> parent = mover;

        for (deep = 0; deep < changeInDeep && (mover.getValue().isPrimitive()); deep++) {
            parent = mover;
            if (Math.random() < 0.5)
                mover = mover.getLeft();
            else
                mover = mover.getRight();
        }
        createFullTree(treeHeight - deep, mover);
        ((Primitive) parent.getValue()).setLeft(parent.getLeft());
        ((Primitive) parent.getValue()).setRight(parent.getRight());
        return copy;
    }


    /**
     * Very important NOT to change this method unless you know exactly what you do.
     * Mating (crossover) between current instance and object parameter,
     * the mating done between the trees. We return new instance that have
     * a tree half of his nodes from the current instance's tree and the other half
     * from object's tree.
     * @param object is the {@code Individual} instance that we want to matting (crossover),
     *               with it.
     * @return a new object that made as result of the matting (crossover) current instance and
     * object parameter.
     */
    @Override
    public Individual crossover(Individual object) {
        BoardIndividual copy = clone();
        BoardIndividual other = (BoardIndividual) object;
        if (Math.random() < 0.5) {
            if (Math.random() < 0.5)
                copy.tree.setRight(copyFullTree(other.tree.getRight()));
            else
                copy.tree.setRight(copyFullTree(other.tree.getLeft()));

            ((Primitive) copy.tree.getValue()).setRight(copy.tree.getRight());
        }
        else {
            if (Math.random() < 0.5)
                copy.tree.setLeft(copyFullTree(other.tree.getRight()));
            else
                copy.tree.setLeft(copyFullTree(other.tree.getLeft()));

            ((Primitive) copy.tree.getValue()).setLeft(copy.tree.getLeft());
        }
        copy.setHeight(copy.findHeight());
        return copy;
    }


    /**
     * Create {@code String} that represent this instance.
     * The {@code String} contain the sudoku board after solving,
     * the amount of the remaining empty cells, the solved cells,
     * and the tree of the instance as infix and prefix expression.
     * @return a string represent the current instance.
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder("Individual : \n\n");
        final int squareLength = (int) Math.sqrt(board.length);

        int originalEmptyCell = countEmptyCellInOriginalSudoku();
        int currentEmptyCell = countEmptyCellInSudoku();
        if (originalEmptyCell == currentEmptyCell)
            buf.append("this individual property not played\n\n");
        else {
            if (currentEmptyCell < originalEmptyCell) {
                buf.append("Solve = ")
                        .append(originalEmptyCell - currentEmptyCell)
                        .append(" / ")
                        .append(originalEmptyCell)
                        .append("\n")
                        .append("Left = ")
                        .append(currentEmptyCell)
                        .append("\n\n");
            } else
                buf.append("Something Wrong in playing function\n");
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                buf.append(board[i][j]).append(" ");
                if ((j + 1) % squareLength == 0)
                    buf.append("  ");
            }
            buf.append("\n");
            if ((i + 1) % squareLength == 0)
                buf.append("\n");
        }
        buf.append(super.toString());
        return buf.toString();
    }


    /**
     * The only purpose of this method to test if the board dimensions
     * is good : NxN when sqrt(N) is natural number.
     * @param board is 2D array represent the sudoku board that we want to test.
     */
    private void testIfGoodDimensionBoard(int[][] board) {
        final double realSquareLength = Math.sqrt(board.length);
        final int squareLength = (int) realSquareLength;

        if (realSquareLength != squareLength) {
            throw new RuntimeException("You should send sudoku board with NxN dimensions," +
                    " when sqrt(N) is a natural number");
        }
        for (int i = 0; i < board.length; i++) {
            if (board[i].length!=board.length){
                throw new RuntimeException("You should send sudoku board with NxN dimensions");
            }
        }
    }


    /**
     * This method gives you a random number between 1 (inclusive) and max (exclusive),
     * this method give a number in range [min,max) .
     * This method act in strange way:
     * if you call it with (1) argument it will return : 1
     * and if you run it with (2) argument it's rerun : 1
     * and if you run it with (0) argument it's return : 1
     * and if you run it with (3) argument it's return : 1 or 2
     * it's return a random number of focusing on min number is 1,
     * if you send max number larger than 1, then it will return number in range exclusive max.
     * We using it in {@link #mutate() mutate} method, it's very important method.
     * @param max is the max limit of the range that we want to generate number from it.
     * @return a random number in the range 1 (inclusive) to max (exclusive).
     */
    private int nextInc1ExcMax(int max) {
        return 1 + (int) (Math.random() * (max - 1));
    }


}