package sudokuGame;

import java.util.Hashtable;

/**
 * {@code Terminal} class is representing the the terminal function (terminal node)
 * in our Tree-based Genetic Programing. And we use this class as a generic
 * {@link Node#value T value} in the {@link Node} class.
 * @author Abedalrhman Nsasra
 * @version 1.0
 * @see Node
 * @see Primitive
 * @see TerminalOrPrimitive
 * @see <a href="https://en.wikipedia.org/wiki/Genetic_programming">Genetic Programming</a>
 */
public class Terminal extends TerminalOrPrimitive {

    /**
     * Constructor that initialize {@code Terminal} instance,
     * and always determine {@code "Terminal"} {@code String} as {@link #nodeType nodeType}.
     * and set operationName parameter as the function that should
     * {@link #run(int, int, int, int[][], Hashtable[][]) run}
     * in the {@code Terminal} instance in our Tree-based Genetic Programing
     * @param operationName is a {@code String} that determine the function operation in
     *                      the {@code Terminal} {@code Node} in our Tree-based Genetic Programing.
     *                      The function that the {@code Terminal} instance could operate (run):
     *                      {@link #countEmptyCellInRow(int, int[][]) countEmptyCellInRow},
     *                      {@link #countEmptyCellInCol(int, int[][]) countEmptyCellInCol},
     *                      {@link #countEmptyCellInSquare(int, int, int[][]) countEmptyCellInSquare},
     *                      {@link #numOfOptionsInCell(int, int, Hashtable[][]) numOfOptionsInCell},
     *                      {@link #numOfOptionsToAppearInBoard(int, int[][]) numOfOptionsToAppearInBoard},
     *                      {@link #countEmptyCellsInRowsContainsNum(int, int[][]) countEmptyCellsInRowsContainsNum},
     *                      {@link #countEmptyCellsInColsContainsNum(int, int[][]) countEmptyCellsInColsContainsNum},
     *                      {@link #countEmptyCellsInSquareContainsNum(int, int[][]) countEmptyCellsInSquareContainsNum},
     *                      {@link #countEmptyCellsInRows_ThatNotContainsNum(int, int[][]) countEmptyCellsInRows_ThatNotContainsNum},
     *                      {@link #countEmptyCellsInCols_ThatNotContainsNum(int, int[][]) countEmptyCellsInCols_ThatNotContainsNum},
     *                      {@link #countEmptyCellsInSquare_ThatNotContainsNum(int, int[][]) countEmptyCellsInSquare_ThatNotContainsNum}.
     *                      and of course the list can be extended if you want to.
     *                      Notice: that {@code Terminal} can only operate and only one function.
     */
	public Terminal(String operationName ){
		super("Terminal",operationName);
	}

    /**
     * We need to clone the tree in the future, so we need to clone
     * {@link Node#value T value} in {@link Node} Class.
     * So we cloning {@code Terminal} instances.
     * @return a clone of {@code Terminal} instance.
     */
    @Override
    protected Terminal clone() {
        return (Terminal) super.clone();
    }


    /**
     * Call the method with name {@link #operationName operationName} with the parameters that received,
     * and return the number that method is returned.
     * The number is represent how much good is the parameter key as a solution in board[row,col],
     * smaller is better, we could choose the the opposite, it will NOT change the solution.
     * Notice: that all return numbers will returned to parent node in the tree (it should be Primitive type).
     * @param row The index of the row in the Sudoku board.
     * @param col The index of the column in the Sudoku board.
     * @param key One of the options (number 1-9) that can be inserted in [row,col] in the board without conflict.
     * @param board Reference to the Sudoku board matrix with size 9x9, we don't copy the board it's just a reference.
     * @param gradeBoard Reference to matrix of hash tables that represent in every index in the matrix
     *                   which numbers can be inserted without conflict as a keys in the hash table and
     *                   grade as a value for every key that represent how good this option as solution,
     *                   we choose smaller grade is better but you could choose larger is better,
     *                   it will not change the solution.
     * @return the number that the method with name {@link #operationName operationName} is returned.
     */
	@Override
	double run(final int row, final int col, final int key, final int[][] board,
               final Hashtable<Integer,Double> [][] gradeBoard) {
		switch (this.operationName){
		case "countEmptyCellInRow":
			return (double)countEmptyCellInRow(row,board);

		case "countEmptyCellInCol":
			return (double)countEmptyCellInCol(col,board);
			
		case "countEmptyCellInSquare":
			return (double)countEmptyCellInSquare(row,col,board);
		
		case "numOfOptionsInCell":
			return (double) numOfOptionsInCell(row,col, gradeBoard);
			
		case "numOfOptionsToAppearInBoard":
			return (double)numOfOptionsToAppearInBoard(key,board);
			
		case "countEmptyCellsInRowsContainsNum":
			return (double)countEmptyCellsInRowsContainsNum(key,board);
		
		case "countEmptyCellsInColsContainsNum":
			return (double)countEmptyCellsInColsContainsNum(key, board);
			
		case "countEmptyCellsInSquareContainsNum":
			return (double)countEmptyCellsInSquareContainsNum(key, board);

		case "countEmptyCellsInRows_ThatNotContainsNum":
			return (double)countEmptyCellsInRows_ThatNotContainsNum(key,board);
		
		case "countEmptyCellsInCols_ThatNotContainsNum":
			return (double)countEmptyCellsInCols_ThatNotContainsNum(key, board);
			
		case "countEmptyCellsInSquare_ThatNotContainsNum":
			return (double)countEmptyCellsInSquare_ThatNotContainsNum(key, board);
			
		default :
		    /*This should Not happen unless you add Function in SudokuUtil::fillOperators*/
            throw new RuntimeException("Function is Not supported, you should add it here");
		}
	}

    /* Functions for Terminal instances */

    /**
     * Count the empty cells in the specific row in the sudoku board, and return it.
     * @param row is the row index that we check (iterate and count).
     * @param board is 2D array that represent the sudoku board.
     * @return the count of the empty cell in the specific row.
     */
    private int countEmptyCellInRow(int row, int[][] board){
        int numOfEmptyRow=0;
        for(int i=0;i<board[row].length;i++)
        {
            /*Here we will check if the cells  in our row is empty (0),
            and we count how many empty cells we got in the row.*/
            if(board[row][i]==0)
                numOfEmptyRow++;
        }
        return numOfEmptyRow;
    }


    /**
     * Count the empty cells in the specific column in the sudoku board, and return it.
     * @param col is the column index that we check (iterate and count).
     * @param board is 2D array that represent the sudoku board.
     * @return the count of the empty cell in the specific column.
     */
    private int countEmptyCellInCol(int col, int[][] board){
        int numOfEmptyColumn=0;
        for(int i=0;i<board.length;i++)
        {
            if(board[i][col]==0)
            {
                numOfEmptyColumn++;
            }
        }
        return numOfEmptyColumn;
    }


    /**
     * This method calculate the range of the gaven index of a square and count the empty cell
     * in that square.
     * For example: in 9x9 sudoku board, if the method receive [row=0, col=0] it will count the empty indexes,
     * in the range [0-2,0,2].
     * And another example if it receive [row= 3, col=3] it will count the empty indexes,
     * in the range [3-5,3-5].
     * So we determine the range and count the empty in that range (square),
     * Notice that the code written to support any sudoku with length NxN,
     * when sqrt(N) is a natural number.
     * squareLength - it's the length of the square, in 9x9 sudoku the squareLength is 3,
     * in in 16x16 sudoku the squareLength is 4. we use it for a general NxN sudoku.
     * @param row is the row index that exist in a square.
     * @param col is the column index that exist in a square.
     * @param board is 2D array that represent the sudoku board.
     * @return the empty cells in the square that comprehensive the gaven index [row,col],
     * [rowStartIndex, colStartIndex] to [rowStartIndex+squareLength, colStartIndex+squareLength].
     * @throws RuntimeException if the board dimensions NxN is NOT quadratic: the sqrt(N) is NOT natural number.
     */
    private int countEmptyCellInSquare(int row,int col, int[][] board) {
        final double realSquareLength = Math.sqrt(board.length);
        final int squareLength = (int) realSquareLength;
         /* This should NEVER happen unless you sent a sudoku with dimensions NxN,
         *  but sqrt(N) is NOT a natural number */
        if (realSquareLength != squareLength) {
            throw new RuntimeException("Something Wrong in countEmptyCellInSquare method in Terminal class," +
                    "you should send sudoku board with NxN dimensions, when sqrt(N) is a natural number");
        }

        /* This mean that the sudoku with dimensions NxN, when sqrt(N) is a natural number.
        *  Example for good sudoku dimensions is : 9x9, 16x16, 25x25, 100x100... etc*/
        final int rowRest = row % squareLength;
        final int colRest = col % squareLength;
        final int rowStartIndex = row - rowRest;
        final int colStartIndex = col - colRest;

        int numOfEmptyCell = 0;
        for (int i = rowStartIndex; i < rowStartIndex + squareLength; i++)
            for (int j = colStartIndex; j < colStartIndex + squareLength; j++) {
                if (board[i][j] == 0)
                    numOfEmptyCell++;
            }
        return numOfEmptyCell;
    }

    /**
     * This method receive index (row,col) in the sudoku board and count the legal options
     * (without conflict) that can be inserted in that index.
     * @param row is the row index that we want check.
     * @param col is the column index that we want check.
     * @param gradeboard a matrix of hash tables, that save in every index the legal options
     *                   that can be inserted (without conflict) in that index as keys,
     *                   and mapping a grade to that option that represent how much good that option
     *                   as solution in that index. Note: The size of keys in any index is the number
     *                   of the options (without conflict) in that index.
     * @return the count of the options that can be inserted (without conflict) in in index [row, col].
     */
    private int numOfOptionsInCell(int row, int col, Hashtable<Integer,Double> [][]  gradeboard){

        return (gradeboard[row][col].size());

    }

    /**
     * In 9x9 sudoku every number can appear exactly 9 times,
     * in NxN sudoku every number can appear exactly N times.
     * So in this method we count the times that number "key" appear in the sudoku,
     * and return the remaining times that can appear (N - appearTimes).
     * @param key is a number (1 to 9 in 9x9 sudoku) or (1 to N in NxN sudoku) that can be solution
     *            in any cell.
     * @param board is 2D array that represent the sudoku board.
     * @return the remaining times that the number "key" can appear in the sudoku.
     */
    private int numOfOptionsToAppearInBoard(int key, int[][] board){
        int numOfAppears=0;
        for(int i=0;i<board.length;i++)
            for(int j=0;j<board[i].length;j++)
            {
                if(board[i][j]==key)
                    numOfAppears++;
            }
        return board.length-numOfAppears;
    }


    /**
     * This method scan all the rows in the board, if the row contain the key number,
     * then the method count the empty cells in that row, and eventually return the sum
     * of all empty cells in all rows that contain key number.
     * @param key is a number (1 to 9 in 9x9 sudoku) or (1 to N in NxN sudoku) that can be solution
     *            in any cell.
     * @param board is 2D array that represent the sudoku board.
     * @return the empty cells in all rows that contain "key" number as a solution.
     */
    private  int countEmptyCellsInRowsContainsNum(int key, int[][] board){
        int numOfEmptyCell=0;
        for(int i=0;i<board.length;i++)
        {
            if(existInRow(i,key,board))
            {
                for(int j=0;j<board[i].length;j++)
                {
                    if(board[i][j]==0)
                        numOfEmptyCell++;
                }
            }
        }
        return numOfEmptyCell;
    }


    /**
     * This method scan all the columns in the board, if the column contain the "key" number,
     * then the method count the empty cells in that column, and eventually return the sum
     * of all empty cells in all columns that contain key number as a solution.
     * @param key is a number (1- to 9 in 9x9 sudoku) or (1 to N in NxN sudoku) that can be solution
     *            in any cell.
     * @param board is 2D array that represent the sudoku board.
     * @return the empty cells in all columns that contain "key" number as a solution.
     */
    private int countEmptyCellsInColsContainsNum(int key, int[][] board){
        int numOfEmptyCell=0;
        for(int i=0;i<board[0].length;i++)
        {
            if(existInCol(i,key,board))
            {
                for(int j=0;j<board.length;j++)
                {
                    if(board[j][i]==0)
                        numOfEmptyCell++;
                }
            }
        }
        return numOfEmptyCell;
    }


    /**
     * This method scan all the nxn square in the board, if the square contain the "key" number,
     * then the method count the empty cells in that square, and eventually return the sum
     * of all empty cells in all square that contain key number as a solution.
     * Important Note: if the sudoku dimensions is NxN then the square dimensions is nxn,
     * when n=sqrt(N) and n is a neutral number.
     * squareLength - it's the length of the square, in 9x9 sudoku the squareLength is 3,
     * in in 16x16 sudoku the sizeOfSquare is 4. we use it for a general NxN sudoku.
     * @param key is a number (1 to 9 in 9x9 sudoku) or (1 to N in NxN sudoku) that can be solution
     *            in any cell.
     * @param board is 2D array that represent the sudoku board.
     * @return the empty cells in all squares that contain "key" number as a solution.
     * @throws RuntimeException if the board dimensions NxN is NOT quadratic: the sqrt(N) is NOT natural number.
     */
    private int countEmptyCellsInSquareContainsNum(int key, int[][] board)
    {
        final double realSquareLength = Math.sqrt(board.length);
        final int squareLength = (int) realSquareLength;
        /*This should NEVER happen unless you sent a sudoku with dimensions NxN,
        * but sqrt(N) is NOT a natural number */
        if (realSquareLength != squareLength){
            throw new RuntimeException("Something Wrong in countEmptyCellsInSquareContainsNum method" +
                    " in Terminal class, you should send sudoku board with NxN dimensions," +
                    " when sqrt(N) is a natural number");
        }

        int numOfEmptyCell=0;
        for(int i=0;i<board.length;i+=squareLength)
        {
            for(int j=0;j<board[i].length;j+=squareLength)
            {
                if(existInSquare(i, j, key, board))
                {
                    numOfEmptyCell+= countEmptyCellInSquare(i,j,board);
                }
            }
        }
        return numOfEmptyCell;
    }


    /**
     * This method scan all the rows in the board, if the row does NOT contain the key number,
     * then the method count the empty cells in that row, and eventually return the sum
     * of all empty cells in all rows that do NOT contain key number.
     * @param key is a number (1 to 9 in 9x9 sudoku) or (1 to N in NxN sudoku) that can be solution
     *            in any cell.
     * @param board is 2D array that represent the sudoku board.
     * @return the empty cells in all rows that do NOT contain "key" number as a solution.
     */
    private int countEmptyCellsInRows_ThatNotContainsNum(int key, int[][] board){
        int numOfEmptyCell=0;
        for(int i=0;i<board.length;i++)
        {
            if(!existInRow(i,key,board))
            {
                for(int j=0;j<board[i].length;j++)
                {
                    if(board[i][j]==0)
                        numOfEmptyCell++;
                }
            }
        }
        return numOfEmptyCell;
    }


    /**
     * This method scan all the columns in the board, if the column does NOT contain the "key" number,
     * then the method count the empty cells in that column, and eventually return the sum
     * of all empty cells in all columns that do NOT contain key number as a solution.
     * @param key is a number (1- to 9 in 9x9 sudoku) or (1 to N in NxN sudoku) that can be solution
     *            in any cell.
     * @param board is 2D array that represent the sudoku board.
     * @return the empty cells in all columns that do NOT contain "key" number as a solution.
     */
    private int countEmptyCellsInCols_ThatNotContainsNum(int key, int[][] board){
        int numOfEmptyCell=0;
        for(int i=0;i<board[0].length;i++)
        {
            if(!existInCol(i,key,board))
            {
                for(int j=0;j<board.length;j++)
                {
                    if(board[j][i]==0)
                        numOfEmptyCell++;
                }
            }
        }
        return numOfEmptyCell;
    }


    /**
     * This method scan all the nxn square in the board, if the square does NOT contain the "key" number,
     * as a solution, then the method count the empty cells in that square, and eventually return the sum
     * of all empty cells in all square that do NOT contain key number as a solution.
     * Important Note: if the sudoku dimensions is NxN then the square dimensions is nxn,
     * when n=sqrt(N) and n is a neutral number.
     * squareLength - it's the length of the square, in 9x9 sudoku the squareLength is 3,
     * in in 16x16 sudoku the sizeOfSquare is 4. we use it for a general NxN sudoku.
     * @param key is a number (1 to 9 in 9x9 sudoku) or (1 to N in NxN sudoku) that can be solution
     *            in any cell.
     * @param board is 2D array that represent the sudoku board.
     * @return the empty cells in all squares that do NOT contain "key" number as a solution.
     * @throws RuntimeException if the board dimensions NxN is NOT quadratic: the sqrt(N) is NOT natural number.
     */
    private int countEmptyCellsInSquare_ThatNotContainsNum(int key, int[][] board)
    {
        final double realSquareLength = Math.sqrt(board.length);
        final int squareLength = (int) realSquareLength;
        /*This should NEVER happen unless you sent a sudoku with dimensions NxN,
        * but sqrt(N) is NOT a natural number */
        if (realSquareLength != squareLength){
            throw new RuntimeException("Something Wrong in countEmptyCellsInSquare_ThatNotContainsNum method" +
                    " in Terminal class, you should send sudoku board with NxN dimensions," +
                    " when sqrt(N) is a natural number");
        }

        int numOfEmptyCell=0;
        for(int i=0;i<board.length;i+=3)
        {
            for(int j=0;j<board[i].length;j+=3)
            {
                if(!existInSquare(i, j, key, board))
                {
                    numOfEmptyCell+= countEmptyCellInSquare(i,j,board);
                }
            }
        }
        return numOfEmptyCell;
    }






   /* Static methods:
   * We use them as a helping method in this class (Terminal class), and in other classes.
   * So we decide to make them a static, so we can use them in other class as a help methods
   * without to create Terminal instance */


    /**
     * Count the empty cells in the board sudoku and return it.
     * @param board is 2D array that represent the sudoku board.
     * @return the count on the empty cells in the sudoku board.
     */
    static int countEmptyCellInSudoku(int[][] board )
    {
        int numOfEmptyCells=0;
        for(int i=0;i<board.length;i++)
        {
            for(int j=0;j<board[i].length;j++)
            {
                if(board[i][j]==0)
                    numOfEmptyCells++;
            }
        }
        return numOfEmptyCells;
    }


    /**
     * This method receive index of row and number (between 1-9 in 9x9 sudoku, 1-N in NxN sudoku),
     * and scan and check if the row contain the num as a solution, return true if it's really exist,
     * false otherwise.
     * @param row is the index of the row that we want check.
     * @param num is the number that we want to check if it exist in the row as a solution.
     * @param board is 2D array that represent the sudoku board.
     * @return true if the num already exist in the row, false otherwise.
     */
    static boolean existInRow(int row, int num, int[][] board)
    {
        for(int i=0;i<board[row].length;i++)
        {
            if(board[row][i]==num)
                return true;
        }
        return false;
    }


    /**
     * This method receive index of column and number (between 1 to 9 in 9x9 sudoku, 1 to N in NxN sudoku),
     * and scan and check if the column contain the num as a solution, return true if it's really exist,
     * false otherwise.
     * @param col is the index of the column that we want check.
     * @param num is the number that we want to check if it exist in the column as a solution.
     * @param board is 2D array that represent the sudoku board.
     * @return true if the num already exist in the column, false otherwise.
     */
    static boolean existInCol(int col, int num, int[][] board)
    {
        for(int i=0;i<board.length;i++)
        {
            if(board[i][col]==num)
                return true;
        }
        return false;
    }


    /**
     * This method receive the index of cell [row, col] and check if the num is really exist in the
     * square that comprehensive that index.
     * For example in 9x9 sudoku, the square [0-2,0-2] is comprehensive the index [0,0].
     * Notice that the code written to support any sudoku with length NxN,
     * when sqrt(N) is a natural number.
     * squareLength - it's the length of the square, in 9x9 sudoku the squareLength is 3,
     * in in 16x16 sudoku the squareLength is 4. we use it for a general NxN sudoku.
     * @param row is the index of the row that we want to check if it contain num.
     * @param col is the index of the column that we want to check if it contain num.
     * @param num the number that we want to check if it's already exist in the square that comprehensive
     *            the index [row, col].
     * @param board is 2D array that represent the sudoku board.
     * @throws RuntimeException if the board dimensions NxN is NOT quadratic: the sqrt(N) is NOT natural number.
     * @return true if the number already exist in the range if the square that comprehensive [row, col],
     * false otherwise.
     */
    static boolean existInSquare(int row, int col, int num, int[][] board) {
        final double realSquareLength = Math.sqrt(board.length);
        final int squareLength = (int) realSquareLength;
         /*This should NEVER happen unless you sent a sudoku with dimensions NxN,
         * but sqrt(N) is NOT a natural number */
        if (realSquareLength != squareLength) {
            throw new RuntimeException("Something Wrong in existInSquare method" +
                    " in Terminal class, you should send sudoku board with NxN dimensions," +
                    " when sqrt(N) is a natural number");
        }

        final int rowRest = row % squareLength;
        final int colRest = col % squareLength;
        final int rowStartIndex = row - rowRest;
        final int colStartIndex = col - colRest;

        for (int i = rowStartIndex; i < rowStartIndex + squareLength; i++) {
            for (int j = colStartIndex; j < colStartIndex + squareLength; j++) {
                if (board[i][j] == num) {
                    return true;
                }
            }
        }
        return false;
    }
}
