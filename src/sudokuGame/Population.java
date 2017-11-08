package sudokuGame;

import java.util.Arrays;

/**
 * This class representing our population, the population is a collection of individuals
 * that every one of them will try to solve the sudoku board.
 * {@code Population} class will generate the first generation,
 * and it will be responsible to create the next generation according to selection interface.
 * The next generation base is the current generation but we do
 * {@link Individual#crossover(Individual) crossover} with the good individuals
 * and do {@link Individual#mutate() mutate} for some of the individuals.
 * In this class we manage our individuals in an encapsulation class.
 * @author Abedalrhman Nsasra
 * @version 1.0
 * @see Individual
 * @see Selection
 * @see TournamentSelection
 * @see <a href="https://en.wikipedia.org/wiki/Genetic_programming">Genetic Programming</a>
 */
public class Population {

    /** Represent a collection of players (individuals) that will try to solve the sudoku board*/
    protected Individual[] individuals;


    /** Represent the methodology of creation the next generation */
    private final Selection selection;


    /**
     * Initialize the fields and generate the first generation,
     * the first generation is generated randomly.
     * @param popSize is the size of the required population
     * @param prototype is just a prototype to help as to generate the first generation
     * @param selection is class that implement {@link Selection Selection} interface,
     *                  and determined the methodology of creation the next generation.
     */
	public Population(int popSize, Individual prototype, Selection selection) {
		individuals = new Individual[popSize];
		for (int i = 0;  i < popSize;  ++i)
		{
			individuals[i] = prototype.clone();
			individuals[i].reGenerateFullTree();
		}
		sort();
		this.selection=selection;
	}


    /**
     * Return the best player (individual), the best player is the one with the lowest fitness.
     * Since we sorting our players Ascending according to their fitness,
     * the best one (lowest fitness) is the first one.
     * @return the individual with the lowest fitness (the best one).
     */
	public Individual getBest() {
		return individuals[0];
	}


    /**
     * Return the worst player (individual), the worst player is the one with the highest fitness.
     * Since we sorting our players ascending according to their fitness,
     * the worst one (highest fitness) is the last one.
     * @return the individual with the highest fitness (the worst one).
     */
	public Individual getWorst() {
		return individuals[individuals.length-1];
	}



    /**
     * Generate the next generation according to {@link Selection Selection} methodology.
	 * If the current generation failed to solve the sudoku board,
     * then we generate a new generation by mutation and crossover.
     * The property to apply mutation and crossover is determined in {@link Selection Selection},
     * and the methodology to apply the mutation and crossover is determined in
     * {@link Selection#reproduce(Individual[], Individual) reproduce} method.
     * There are many different ways to apply mutation and crossover (matting),
     * so we are flexible about it, and who implement the {@link Selection Selection}
     * decide how to do it.
     * In the end after creating the new generation we sort the individuals
     * form the best to the worst according to their fitness.
     */
	public void nextGeneration() {
		Individual[] newPop = new Individual[individuals.length];
		for (int index = 0;  index < newPop.length;  ++index) {
            newPop[index] = selection.reproduce(individuals, individuals[index]);

            /* Other way to apply crossover and mutation */
            //newPop[index] = selection.reproduce(individuals);
        }
		individuals = newPop;
		sort();
	}


    /**
     * Just sort the players (individuals) ascending according to there fitness,
     * form the best player (with lower fitness) to the worst (with higher fitness).
     * We use the static method {@link Arrays#sort(Object[]) sort} from {@link Arrays} class.
     */
	private void sort() {
		Arrays.sort(individuals);
	}


    /**
     * Returns the probability to apply mutation to any player (individual).
     * The implementation of {@link Selection Selection} interface will determine
     * this probability, 0.3 is a good choice.
     * @return the mutation probability (the probability to apply mutation to any player)
     */
	public double getMutationProb() {
		return selection.getMutationProb();
	}


    /**
     * Returns the probability to apply crossover to any player (individual).
     * The implementation of {@link Selection Selection} interface will determine
     * this probability, 0.7 is a good choice.
     * @return the crossover probability (the probability to apply crossover to any player,
     * with one of the good individuals)
     */
	public double getCrossoverProb() {
		return selection.getCrossoverProb();
	}


    /**
     * Returns what we consider as good population percent,
     * when we create the next generation by matting (crossover),
     * we matting with this percent of the best individuals (with lowest fitness)
     * in the population.
     * @return the the percent of what we consider a good population.
     */
	public double getGoodPopulationPercent()
	{
		return selection.getGoodPopulationPercent();
	}


    /**
     * Return the size of the population (the amount of the individuals).
     * @return the size of population (the amount of the individuals).
     */
	public int getPopulationSize()
	{
		return individuals.length;
	}


    /**
     * Count the empty cells in the original sudoku board,
     * we need this method only for creating a report.
     * @return the amount of the empty cells in the sudoku board.
     */
	public int countEmptyCellInOriginalSudoku()
	{
		return ((BoardIndividual)getBest()).countEmptyCellInOriginalSudoku();
	}


    /**
     * Calculate the average of the fitness of the individuals and return it.
     * We need this method only to create a report.
     * @return the average of the individuals's fitness.
     */
	public double getAvgPopulationFitness() {
		double sum=0;
		for (int i = 0; i < individuals.length; i++) {
			sum+=individuals[i].getFitness();
		}
		return sum/(double)individuals.length;
	}
}
