package sudokuGame;

/**
 * {@code Variable} interface represent the basic operations in the evolution
 * that every individual should able to apply them, and this operation is crossover
 * and mutate.
 * Without these operation can't be evolution, so every player (individual)
 * should implement {@link #crossover(Individual)} {@link #mutate()} methods.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Genetic_programming">Genetic Programming</a>
 * @see <a href="https://en.wikipedia.org/wiki/Mutation">Mutation</a>
 * @see <a href="https://en.wikipedia.org/wiki/Chromosomal_crossover">Chromosomal crossover</a>
 * @see <a href="https://en.wikipedia.org/wiki/Genome">Genome</a>
 */
public interface Variable {

    /**
     * Represent the mutation operation that can happen in genome.
     * @see <a href="https://en.wikipedia.org/wiki/Mutation">Mutation</a>
     * @return a new mutated (player) individual.
     */
	Individual mutate();


    /**
     * Represent the crossover (or crossing over) that can happen between chromosomes.
     * @see <a href="https://en.wikipedia.org/wiki/Chromosomal_crossover">Chromosomal crossover</a>
     * @param other is the individual that we want to apply crossover with them.
     * @return a new recombinant individual as result of the crossover.
     */
	Individual crossover(Individual other);
}
