package sudokuGame;

/**
 * {@code TournamentSelection} represent the methodology that we decide
 * to crossover (matting) or mutate.
 * In this class we determine the probability to apply crossover or mutate,
 * and the percent of the good population that we apply crossover with them,
 * and the methodology that we apply those operation in
 * {@link #reproduce(Individual[], Individual)}  {@link #reproduce(Individual[])}
 * methods.
 * @author Abedalrhman Nsasra
 * @version 1.0
 * @see Selection
 * @see Population
 * @see <a href="https://en.wikipedia.org/wiki/Genetic_programming">Genetic Programming</a>
 */
public class TournamentSelection implements Selection {

    /** is the probability to apply mutation */
	private final double mutationProb;

    /** is the probability to apply crossover */
	private final double crossoverProb;

    /** is the percent of the population that we matting with it in crossover method*/
	private final double goodPopulationPercent;


    /**
     * Initialize the class member:
     * @param mutationProb is the probability to apply mutation (randomly change in tree-based GP)
     * @param crossoverProb is the probability to apply crossover (matting)
     * @param goodPopulationPercent is the percent of the population that
     *                              we matting with it in crossover method
     */
	public TournamentSelection(double mutationProb, double crossoverProb,double goodPopulationPercent) {
		this.mutationProb  = mutationProb;
		this.crossoverProb = crossoverProb;
		this.goodPopulationPercent=goodPopulationPercent;
	}


    /**
     * Get the probability to apply mutation on any individual.
     * 0.3 is consider as a good choice.
     * @return the probability to apply mutation on the individuals.
     */
	public double getMutationProb() {
		return mutationProb;
	}


    /**
     * Get the probability to apply crossover (matting) on any individual (player)
     * 0.7 consider as good choice.
     * @return the probability to apply crossover on the individual.
     */
	public double getCrossoverProb() {
		return crossoverProb;
	}


    /**
     * Get the percent of what we consider as good individuals to apply with them
     * crossover (matting), it's NOT good to choose to much smaller percent or
     * to much larger percent.
     * Choosing smaller percent (something like 15%) cause that all the individuals
     * will be similar to each other after few generation and that cause that the system
     * failed to find the individuals that solve the sudoku.
     * As well choosing to much large percent (something like 80%) cause that the system
     * evolute slowly, and the end the system fail to find the solution.
     * 0.4 consider as good choice.
     * @return the percent of what we consider as a good individuals,
     * that almost reach the solution.
     */
	public double getGoodPopulationPercent() {
		return goodPopulationPercent;
	}



    /**
     * This method represent the  methodology that we evaluate the player (individual).
     * Here we receive the player (individual) p1, and evaluate him with
     * {@link Individual#crossover(Individual) crossover} and
     * {@link Individual#mutate()} mutate} methods, and applying those methods
     * is probability that we determined in the constructor.
     * Notice: we use {@link #select(Individual[]) select} method that select a
     * random player from the {@link #goodPopulationPercent percent} of the good
     * players (individuals) with the best fitness in a matter to apply crossover
     * with them.
     * @param pop is the collection of the players (population).
     * @param p1 is the player (individual) that we apply to him crossover
     *           with another player.
     * @return a new evaluate player (individual), we apply (maybe) on him
     * crossover and mutation.
     */
	public Individual reproduce(Individual[] pop, Individual p1)
	{
		if (Math.random() < crossoverProb) {
			Individual p2 = select(pop);
			p1 = p1.crossover(p2);
		}
		if (Math.random() < mutationProb) {
			p1 = p1.mutate();
		}
		return p1;
	}


    /**
     * This method represent the  methodology that we evaluate the player (individual).
     * Here we select a random player (from the percent of good players),
     * and apply (maybe) on him crossover with another random player,
     * and maybe apply on him mutation.
     * Notice: we use {@link #select(Individual[]) select} method that select a
     * random player from the {@link #goodPopulationPercent percent} of the good
     * players (individuals) with the best fitness in a matter to apply crossover
     * with them.
     * @param pop is the collection of the players (population).
     * @return a new random evaluate player (individual), we apply (maybe) on him
     * crossover and mutation.
     */
	@Override
	public Individual reproduce(Individual[] pop) {
		Individual p1 = select(pop);
		
		if (Math.random() < crossoverProb) {
			Individual p2 = select(pop);
			p1 = p1.crossover(p2);
		}
		if (Math.random() < mutationProb)
			p1 = p1.mutate();
		
		return p1;
	}


    /**
     * Select a random player from the {@link #goodPopulationPercent percent} of the good
     * players (individuals) with the best fitness in a matter to apply crossover
     * with them.
     * @param pop is the collection of the players (population).
     * @return a random player (individual) from the percent of the good players
     */
	private Individual select(Individual[] pop) {
		
		return pop[randomIndex((int)(pop.length * goodPopulationPercent))];
	}



    /**
     * Get a random number between 0 to max, included both.
     * @param max is the top range of the random numbers that we want to get.
     * @return a random number between 0 to max
     */
	private int randomIndex(int max) {
		return (int)(Math.random() * max);
	}
}
