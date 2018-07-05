# Sudoku-Solver
Solve sudoku boards with Genetic Programming (GP) technique, GP is a collection of evolutionary computation techniques
that allow computers to solve problems automatically.
Since its inception twenty years ago, GP has been used to solve a wide range of practical problems,
producing a number of human-competitive results and even patentable new inventions.
Like many other areas of computer science, GP is evolving rapidly, with new ideas, techniques and applications 
being constantly proposed. While this shows how wonderfully prolific GP is, it also makes it difficult for newcomers 
to become acquainted with the main ideas in the field, and form a mental map of its different branches.
Even for people who have been interested in GP for a while, it is difficult to keep up with the pace of new developments.
You can read about the topic in depth in 
[A Field Guide to Genetic Programming](https://www.amazon.com/Field-Guide-Genetic-Programming/dp/1409200736) book,
or read the full [documentation](https://abdulrhmanil.github.io/Sudoku-Solver/) of the application, 
generated with Javadoc tool.

### Sudoku Description
Sudoku is a logic-based, combinatorial number-placement puzzle. The objective is to fill a 9×9 grid with digits so that 
each column, each row, and each of the nine 3×3 sub grids that compose the grid 
(also called "boxes", "blocks", or "regions") contains all of the digits from 1 to 9. 
The puzzle setter provides a partially completed grid, which for a well-posed puzzle has a single solution.
Completed games are always a type of Latin square with an additional constraint on the contents of individual regions.
For example, the same single integer may not appear twice in the same row, column,
or any of the nine 3×3 sub regions of the 9x9 playing board.  
You can read more in: <https://en.wikipedia.org/wiki/Sudoku>  
Note: the application designed to support any sudoku board with size NxN, when the sqrt(N) is a natural number.

### Getting Started  
To run the application you just need to run the main method in Driver class,
and the application will choose a random real sudoku 
from the [realBoards.txt](https://github.com/Abdulrhmanil/Sudoku-Solver/blob/master/boards/realBoards.txt) file,
and then will try to solve it with GP.

### Preferences
The application have few preference that you can change via the main method that can change the application's behavior.

1. The size of the population:  
`int popSize = 100;`  
Population size is the amount of the individuals in the population, in every generation we generate, increasing the size
will increase the chances to find an individual that can solve the sudoku board, as will increase the run time of the
application, because the application has more individuals that every one try to solve the sudoku board.
Setting popSize between 100 and 200 is a good choice to increase the probability to find the solution while
save a good performance of the application.

2. The max attempts of creating new generations to solve the sudoku:  
`int maxGenerations = 100;`  
Max generations is the max attempts of creating new generations to solve the sudoku, if the current generation failed to
solve the sudoku we generate a new one, based on the best individuals of the current generation. we continue to apply
these steps until we find an individual that can solve the sudoku board or we reach the maxGenerations, of curse increasing 
the attempts will increase the chances to find a solution, but any way you can't run forever you must stop at some point.
Setting maxGenerations between 50 and 100 is a good choice to increase the probability to find the solution.

3. The probability to apply mutation:  
`double mutationProb = 0.3;`  
While we generate new generations we use two methods, one of them is **mutation**, and there are probability to
apply **mutation**, there are NO guarantee to apply or NOT to apply **mutation**, we let a random method to decide
according to the mutationProb, higher probability is higher chance to apply **mutation**.
We recommend to choose mutationProb less than 0.5, and the reason you don't want to see that the average of 
the individuals fitness between two sequential generations has a huge gaps, you want to see that the average of
the individuals fitness getting better in consecutive way. Setting the mutationProb 0.3 is a good choice.

4. The probability to apply crossover (matting):  
`double crossoverProb = 0.7;`  
The other method to generate individuals while generating a new generation is **crossover**, there are probability
to apply or NOT to apply **crossover**, in the most cases we want to apply **crossover**, but any way you can choose
the probability that you wish. I recommend you to choose between 0.5 to 0.9, because you want fitness improvement
in consecutive way.

5. Percent of the population that considered as a good individuals:  
`double goodPopulationPercent = 0.4;`  
The percent of the population that we matting while we applying crossover (matting) to create the new generation.
When we create the new generation and we determine to apply crossover for the current individual,
we applying crossover (matting) with one individual that consider as good individual. One individual from the
**goodPopulationPercent** percent of the individual that have the highest fitness. So you must set the percent of what
consider as a good individuals, you can choose the whole individuals set _100%_, or just _10%_ .
I recommend you to choose between 0.3 to 0.7, you don't want choose to much small percent and cause the app failed 
to find the solution cause after few generation all the individuals in the generation will be similar, and you don't 
want to choose to much small percent and cause the app **NOT** get improved in consecutive way. Setting the 
goodPopulationPercent 0.4 is a good choice.

6. The height of the trees in the individuals:  
`height = 5;`   
The height of the decision tree in each individual, **MUST** be too careful when choosing the height of the tree,
large height can cause bad performance in the best cases and OutOfMemoryError in the worst cases cause it take too
much memory. And choosing to small height can cause the app to be a kind of dumb and unable to solve the sudoku board.
We recommend to choose 5 to 10 as a height of the tree.     

7. Sudoku dimensions, the sudoku board dimensions:  
`sudokuDimensions = 9;`     
The dimensions of the sudoku board, the app written in general way that can solve any sudoku board with size NxN,
when the sqrt(N) is a natural number. So you must set the dimensions of the board so the app can depend on this
parameter when read the sudoku board from the file.

8. File path that contain the sudoku boards, in specific format:    
`filePath="boards/realBoards.txt";`     
The path of the file that contain the sudoku boards, that we designed in specific way, you can choose different file.

### Experiments
We ran some experiments to test how the app behave in different preferences and summarize