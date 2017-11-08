package sudokuGame;

/**
 * {@code Selection} interface represent the abstract methodology that we decide
 * to apply crossover (matting) or mutate, so different classes can implement
 * different methodologies.
 */
public interface Selection {

    /**
     * This method represent an abstract methodology that we evaluate the player (individual).
     * @param pop is the collection of the players (population).
     * @return a new random evaluate player (individual), we apply (maybe) on him
     * crossover and mutation.
     */
	Individual reproduce(Individual[] pop);


    /**
     * This method represent an abstract methodology that we evaluate the player (individual).
     * Here we receive the player (individual) p1, and evaluate him.
     * @param pop is the collection of the players (population).
     * @param p1 is the player (individual) that we apply to him crossover
     *           with another player.
     * @return a new evaluate player (individual), we apply (maybe) on him
     * crossover and mutation.
     */
	Individual reproduce(Individual[] pop, Individual p1);


    /**
     * Get the probability to apply mutation on any individual.
     * 0.3 is consider as a good choice.
     * @return the probability to apply mutation on the individuals.
     */
	double getMutationProb();


    /**
     * Get the probability to apply crossover (matting) on any individual (player)
     * 0.7 consider as good choice.
     * @return the probability to apply crossover on the individual.
     */
	double getCrossoverProb();


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
	double getGoodPopulationPercent();

}
