package sudokuGame;

import java.io.*;
import java.util.Random;

/**
 * {@code SudokuFileUtil} class is responsible to load the sudoku board from a file,
 * into a 2D array. In other words this class is represent the board sudoku.
 * So this class receive the file path and the sudoku dimensions and can
 * load any sudoku board from this file with :
 * {@link #loadSudoku(int)} {@link #loadSudoku()}
 * {@link #loadPrintSudoku(int)} {@link #loadPrintSudoku()} methods.
 * The sudoku dimensions should be NxN when sqrt(N) is a natural number.
 * @author Abedalrhman Nsasra
 * @version 1.0
 */
public class SudokuFileUtil {

    /** We need it to load a random sudoku from the gaven file*/
    private static Random rnd = new Random();

    /** The path of the file that contain sudoku boards*/
    private final String filePath;

    /** The amount of the boards that the file contain */
    private final int boardsAmount;

    /** The dimensions of the board*/
    private final int sudokuDimens;

    /** 2D array with size [sudokuDimens,sudokuDimens] that represent sudoku board */
    private final int[][] board;

    /** The last sudoku we loaded, to optimize the code */
    private int sudokuNumCache =-1;

    /**
     * Create instance that can load sudoku boards from the gaven file path.
     * @param filePath is the path of the file that contain sudoku boards
     * @param sudokuDimens is the dimensions of the sudoku we should load (read)
     */
    public SudokuFileUtil(final String filePath, final int sudokuDimens) {
        this.filePath = filePath;
        this.sudokuDimens = sudokuDimens;
        this.board= new int[sudokuDimens][sudokuDimens];
        this.boardsAmount = countBoardsInFile();
        checkIfValidDimensions();
    }


    /**
     * Count the amount of the boards that the gaven file contain, and return it.
     * @return the amount of the boards that exist in the gaven file.
     */
    private int countBoardsInFile() {
        int count = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
            String str;
            while ((str = br.readLine()) != null) {
                if (str.startsWith("G")) {
                    count++;
                }
            }
            br.close();
        }
        catch (IOException e) {
            System.err.println(filePath + " file NOT found");
        }
        return count;
    }


    /**
     * Load the sudoku board that with sudokuNum index from the gaven file,
     * the loading is applied in runtime, we try to optimize the the operation.
     * @param sudokuNum is the index of the board that we want load, start from 0
     *                  to {@link #boardsAmount boardsAmount}.
     * @return 2D array the reparent the loaded sudoku board with sudokuNum index,
     * in case sudokuNum aberrant the range [0,boardsAmount) the method load an empty
     * board.
     */
    public int[][] loadSudoku(final int sudokuNum) {

        if (sudokuNum >= boardsAmount || sudokuNum < 0){
            sudokuNumCache =-1;
            restSudoku();
            System.err.println("\nYou inserted "+sudokuNum+" as board index "+
                    ",while we have only "+boardsAmount+" boards");
            return board;
        }
        if (sudokuNum==sudokuNumCache)
            return board;

        sudokuNumCache =sudokuNum;
        boolean founded=false;
        int numOfSudokuBoardInFile = 0, read;
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
            String str;
            while ((str = br.readLine()) != null && (!founded))
            {
                if (str.startsWith("G"))
                {
                    if (sudokuNum == numOfSudokuBoardInFile)
                    {
                        for (int i = 0; i < sudokuDimens; i++) {
                            for (int j = 0; j < sudokuDimens; j++) {
                                read = br.read();
                                this.board[i][j] = Character.getNumericValue(read);
                            }
                        }
                        founded=true;
                    }
                    else
                        numOfSudokuBoardInFile++;
                }
            }
            if (!founded)
            {
                System.err.println("\nYou inserted "+sudokuNum+" as board index "+
                        ",while we have only "+boardsAmount+" boards");
            }
            br.close();
        }
        catch (IOException e) {
            System.err.println(filePath + " file NOT found");
        }
        return board;
    }


    /**
     * Load a random sudoku board from the gaven file.
     * @return 2D array the reparent the loaded sudoku board.
     */
    public int[][] loadSudoku() {
        int randomIndex = rnd.nextInt(boardsAmount);
        return loadSudoku(randomIndex);
    }


    /**
     * Load the sudoku board that with sudokuNum index from the gaven file,
     * and then print it.
     * The loading is applied in runtime, we try to optimize the the operation.
     * @param sudokuNum is the index of the board that we want load, start from 0
     *                  to {@link #boardsAmount boardsAmount}.
     * @return 2D array the reparent the loaded sudoku board with sudokuNum index,
     * in case sudokuNum aberrant the range [0,boardsAmount) the method load an empty
     * board.
     */
    public int[][] loadPrintSudoku(final int sudokuNum) {
        loadSudoku(sudokuNum);
        printSudoku();
        return board;
    }


    /**
     * Load a random sudoku board from the gaven file,
     * then print it.
     * @return 2D array the reparent the loaded sudoku board.
     */
    public int[][] loadPrintSudoku() {
        loadSudoku();
        printSudoku();
        return board;
    }


    /**
     * Just print the loaded sudoku board.
     */
    public void printSudoku() {
        System.out.println("Sudoku :"+sudokuNumCache);
        printSudoku(board);
    }


    /**
     * Rest the the {@link #board board}, fill it with 0.
     */
    private void restSudoku() {
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                this.board[i][j]=0;
            }
        }
    }


    /**
     * This method just check if the dimensions is good for the application,
     * in case the dimensions is NOT good this method throw an RuntimeException
     * @throws RuntimeException in case you sent sudoku dimensions as NxN
     * but sqrt(N) is NOT a natural number.
     */
    private void checkIfValidDimensions(){
        final double realSquareLength = Math.sqrt(sudokuDimens);
        final int squareLength = (int) realSquareLength;
         /*This should NEVER happen unless you sent a sudoku with dimensions NxN,
         * but sqrt(N) is NOT a natural number */
        if (realSquareLength != squareLength) {
            throw new RuntimeException("You should send sudoku dimensions as NxN," +
                    " when sqrt(N) is a natural number");
        }
    }


    /**
     * Create a string that reparent the loaded sudoku board.
     * @return {@code String} that reparent the loaded sudoku board
     */
    @Override
    public String toString() {
        final int squareLength=(int)Math.sqrt(board.length);
        StringBuilder builder=new StringBuilder();
        builder.append("File path : ").append(filePath).append("\n")
                .append("Boards amount : ").append(boardsAmount).append("\n")
                .append("Sudoku dimension : ").append(sudokuDimens).append("\n")
                .append("Sudoku num : ").append(sudokuNumCache).append("\n")
                .append("Empty cell : ").append(Terminal.countEmptyCellInSudoku(board))
                .append("\n\n");

        for(int i=0;i<board.length;i++)
        {
            for(int j=0;j<board[i].length;j++)
            {
                builder.append(board[i][j]+" ");
                if((j+1)%squareLength==0)
                    builder.append(" ");
            }
            builder.append("\n");
            if((i+1)%squareLength==0)
                builder.append("\n");
        }
        return builder.toString();
    }



    /** Static methods : */

    /**
     * Just print the board parameter.
     * this is a static method, and we use it as help method.
     * @param board 2D array that represent a sudoku board, and we print it.
     */
    public static void printSudoku(int[][] board )
    {
        final int squareLength=(int)Math.sqrt(board.length);
        System.out.println("Empty cell : "+Terminal.countEmptyCellInSudoku(board));
        for(int i=0;i<board.length;i++)
        {
            for(int j=0;j<board[i].length;j++)
            {
                System.out.print(board[i][j]+" ");
                if((j+1)%squareLength==0)
                    System.out.print(" ");
            }
            System.out.println();
            if((i+1)%squareLength==0)
                System.out.println();
        }
    }

}