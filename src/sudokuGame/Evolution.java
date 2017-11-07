package sudokuGame;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * {@code Evolution} engine that responsible to search the player (individual)
 * who can solve the sudoku board.
 * This class let the players (individuals) try to solve the sudoku board,
 * if at least one succeed then the program will end
 * if NO one succeed to solve the sudoku board then it will evolve the next generation,
 * to try solve the sudoku board... etc
 * Since the program should NOT run forever the program will stop evolve
 * new generation after reaching {@link #maxGenerations maxGenerations} of attempts.
 * While we trying to solve the sudoku and evolve new generations, we log some information
 * into report file (.csv file) with {@link #createReportFile() createReportFile} and
 * {@link #writeGenerationData(int) writeGenerationData} methods, the report
 * will created in reports directory.
 * In this class we manage the main flow of the program.
 * @author Abedalrhman Nsasra
 * @version 1.0
 * @see Population
 * @see CSV_Writer
 * @see <a href="https://en.wikipedia.org/wiki/Genetic_programming">Genetic Programming</a>
 */
public class Evolution {

    /** Represent the players (individuals) that trying to solve the sudoku board */
    private final Population population;


    /**
     * Max of attempts to create generations that will try
     * to solve the sudoku board until we quit trying.
     */
    private final int maxGenerations;


    /** We use it to logging the evolution progress into report file */
    private CSV_Writer reportGenerator;


    /**
     * Initialize the fields of the instance, and determine the max
     * attempts to generate new generations that trying to solve
     * the sudoku board before quit trying.
     * @param population represent the collection of players (individuals) that trying to solve
     *                   the sudoku board, this instance can generate the next generation
     * @param maxGenerations is the max allowed attempts to create new generations that will try
     *                       to solve the sudoku board
     */
	public Evolution(Population population, int maxGenerations) {
		this.population = population;
		this.maxGenerations = maxGenerations;
	}


    /**
     * Gets the best player (individual) that have lowest fitness,
     * player with lowest fitness is the player that have the lowest
     * amount of empty cells before he could NOT progress without having
     * conflicts in the board solution.
     * In other words the best individual is the one who progressed the most.
     * We need the best player in this class only for logging and create the report file.
     * @return the best player (individual) at the population in the current generation
     */
	private Individual getBest() {
		return population.getBest();
	}


    /**
     * Gets the worst player (individual) that have highest fitness,
     * player with highest fitness is the player that have the highest
     * amount of empty cells before he could NOT progress without having
     * conflicts in the board solution (a kind of loser).
     * We need the worst player in this class only for logging and create the report file.
     * @return the worst player (individual) at the population in the current generation
     */
	private Individual getWorst() {
		return population.getWorst();
	}


    /**
     * {@code evolve} method is the heart operation of the engine,
     * in this method we search the player who can solve the sudoku board,
     * and if we find him we stop the engine and show the solution,
     * and if we don't find a one who can solve the sudoku board,
     * then we generate a better new generation, and repeat the search...etc
     * We repeat this operations until we reach the {@link #maxGenerations max}
     * allowed attempts, and then quit anyway, since we can't run forever.
     */
	public void evolve() {
		int gen;
		createReportFile();
		for (gen = 0;  gen < maxGenerations;  ++gen) {
			writeGenerationData(gen);
			System.out.println("Generation " + gen + ": \n" + getBest());
			System.out.println("\n");
			
			if (getBest().isIdeal())
				break;

			population.nextGeneration();
		}

		if (gen == maxGenerations)
			System.out.println("Best attempt: \n" + getBest());
		else
			System.out.println("Solution: \n" + getBest());
		
	}


    /**
     * Create a report file with current time name (CurrentTime.csv),
     * and append to him the headers and some basic initial information
     * of the experiment parameters like:
     * Original Empty Cells, Population Size, Max Generations
     * Crossover Probability, Mutation Probability, Primitive Set, Terminal Set...
     */
	private void createReportFile(){

		reportGenerator = new CSV_Writer(new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss")
                .format(new Date()) + ".csv");
		reportGenerator.createCsvFile();
		reportGenerator.appendCsvFile(new String[]{"Experiment Parameters:"});
		reportGenerator.appendCsvFile(new String[]{"Original Empty Cells:",
				Integer.toString(population.countEmptyCellInOriginalSudoku())});
		reportGenerator.appendCsvFile(new String[]{"Population Size:",
                Integer.toString(population.getPopulationSize())});
		reportGenerator.appendCsvFile(new String[]{"Max Generations:",
                Integer.toString(maxGenerations)});
		reportGenerator.appendCsvFile(new String[]{"Crossover Probability:",
                Double.toString(population.getCrossoverProb())});
		reportGenerator.appendCsvFile(new String[]{"Mutation Probability:",
                Double.toString(population.getMutationProb())});
		reportGenerator.appendCsvFile(new String[]{"Percent of good individuals from population:",
                Double.toString(population.getGoodPopulationPercent())});
		/* Just add a line separator */
		reportGenerator.appendCsvFile(new String[]{""});

		
		/* Write the function and terminal sets which were used in the experiment */
		reportGenerator.appendCsvFile(new String[]{"Primitive Set:"});
		String[] primitiveArr = new String[Individual.operators.size()];
		primitiveArr = Individual.operators.toArray(primitiveArr);
		reportGenerator.appendCsvFile(primitiveArr);
		reportGenerator.appendCsvFile(new String[]{"Terminal Set:"});	
		String[] terminalArr = new String[Individual.functions.size()];
		terminalArr = Individual.functions.toArray(terminalArr);
		reportGenerator.appendCsvFile(terminalArr);
		/* Just add a line separator */
		reportGenerator.appendCsvFile(new String[]{""});
		/* Just add a line separator */
		reportGenerator.appendCsvFile(new String[]{""});
		reportGenerator.appendCsvFile(new String[]{"Generation","Worst Individual Fitness"
                ,"Best Individual Fitness", "Average Fitness", "Best Individual Tree - Prefix"
                ,"Best Individual Tree - Infix"});
	}


    /**
     * Append the current generation data into the report file:
     * generation number, best individual's fitness, best individual's fitness, average fitness,
     * best individual's tree-based GP as infix and prefix expression.
     * @param gen is the generation number that we reach, and it's the row number that we append
     */
	private void writeGenerationData(int gen){
		reportGenerator.appendCsvFile(new String[]{
				Integer.toString(gen),
				Double.toString(getWorst().getFitness()),
				Double.toString(getBest().getFitness()),
				Double.toString(population.getAvgPopulationFitness()),
				getBest().treeAsPrefixExpression(),
				getBest().treeAsInfixExpression()
				});
	}

	
}