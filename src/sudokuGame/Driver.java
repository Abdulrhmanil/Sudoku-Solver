package sudokuGame;

/**
 * The initial settings of the application,
 * and the start point of the application.
 */
public class Driver {
	
	public static void main(String[] args) {

	    /* Population size, the amount of the individuals in the population*/
        int popSize = 100;

        /* Max generations, the max attempts of creating generations to solve the sudoku*/
        int maxGenerations = 100;

        /* Mutation probability, is the probability to apply mutation*/
        double mutationProb = 0.3;

        /* Crossover probability, is the probability to apply crossover*/
        double crossoverProb = 0.7;

        /* Percent of good population, percent of what we consider as a good individuals*/
        double goodPopulationPercent = 0.4;

        /* Tree height, the height of the trees in the individuals*/
        int height = 5;

        /* Sudoku dimensions, is the sudoku board dimensions*/
        int sudokuDimensions=9;

        /* File path that contain the sudoku boards, in specific format*/
        String filePath="boards/realBoards.txt";

        final SudokuFileUtil fileUtil = new SudokuFileUtil(filePath, sudokuDimensions);
        int[][] board = fileUtil.loadPrintSudoku();

        Individual prototype = new BoardIndividual(height, board);
        Selection select = new TournamentSelection(mutationProb, crossoverProb, goodPopulationPercent);
        Population firstPopulation = new Population(popSize, prototype, select);
        Evolution evolution = new Evolution(firstPopulation, maxGenerations);
        evolution.evolve();
    }

}
