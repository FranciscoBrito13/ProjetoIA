package breakout;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GeneticAlgorithm {
    private static final int NUM_POPULATION = 100;
    private static final double MUTATION_RATE = 0.15;
    private static final double NUM_GENERATIONS = 500;
    private static final double SELECTION_TOP = 0.40;
    private BreakoutNeuralNetwork[] population;

    public GeneticAlgorithm(){
        population = new BreakoutNeuralNetwork[NUM_POPULATION];
        createPopulation();
    }
    private void createPopulation() {
        for(int i = 0; i < NUM_POPULATION; i++){
            BreakoutNeuralNetwork nn = new BreakoutNeuralNetwork();
            population[i] = nn;
        }
    }

    public BreakoutNeuralNetwork startSearch() {
        BreakoutNeuralNetwork bestNN = population[0];
        for (int i = 0; i < NUM_GENERATIONS; i++) {

            BreakoutNeuralNetwork[] newPopulation = new BreakoutNeuralNetwork[NUM_POPULATION];

            List<BreakoutNeuralNetwork> populationList = Arrays.asList(population);
            for (BreakoutNeuralNetwork nn : population) {
                nn.precomputeFitness();
            }
            populationList.sort(Comparator.comparingDouble(BreakoutNeuralNetwork::getCachedFitness));
            populationList.toArray(population);

            BreakoutNeuralNetwork bestInPopulation = population[99];
            BreakoutNeuralNetwork worstInPopulation = population[0];
            BreakoutNeuralNetwork middleInPopulation = population[50];

            System.out.println("Generation Number: " + i);
            //System.out.println("Best = " + bestInPopulation.getCachedFitness() + "middle = " + middleInPopulation.getCachedFitness() + "worst = " + worstInPopulation.getCachedFitness());

            if (bestInPopulation.getCachedFitness() > bestNN.getCachedFitness()) {
                bestNN = bestInPopulation;
                System.out.println("Best neural network updated! Fitness: " + bestNN.getCachedFitness());
            }

            for (int j = 0; j < NUM_POPULATION / 2; j++) {
                BreakoutNeuralNetwork parentOne = selectParent();
                BreakoutNeuralNetwork parentTwo = selectParent();
                BreakoutNeuralNetwork childrenOne = crossOver(parentOne, parentTwo);
                BreakoutNeuralNetwork childrenTwo = crossOver(parentOne, parentTwo);
                newPopulation[j] = childrenOne;
                newPopulation[j + NUM_POPULATION / 2] = childrenTwo;
            }

            for (int z = 0; z < NUM_POPULATION; z++) {
                double rand = Math.random();
                if (rand < MUTATION_RATE) {
                    newPopulation[z] = new BreakoutNeuralNetwork();
                }
            }
            population = newPopulation;
        }
        System.out.println(bestNN.getCachedFitness());
        return bestNN;
    }

    private BreakoutNeuralNetwork selectParent() {
        int tournamentSize = (int) (SELECTION_TOP * NUM_POPULATION);
        BreakoutNeuralNetwork bestParent = population[NUM_POPULATION - (int) (Math.random() * tournamentSize) - 1];
        for (int i = 1; i < tournamentSize; i++) {
            BreakoutNeuralNetwork contender = population[(int) (Math.random() * tournamentSize)];
            if (contender.getCachedFitness() > bestParent.getCachedFitness()) {
                bestParent = contender;
            }
        }
        return bestParent;
    }

    private BreakoutNeuralNetwork crossOver(BreakoutNeuralNetwork a, BreakoutNeuralNetwork b) {
        // Creates the child
        BreakoutNeuralNetwork childNetwork = new BreakoutNeuralNetwork();

        // Crossover for hidden weights
        for (int i = 0; i < a.getInputLayerDim(); i++) {
            for (int j = 0; j < a.getHiddenLayerDim(); j++) {
                if (Math.random() < 0.5) {
                    childNetwork.getHiddenWeights()[i][j] = a.getHiddenWeights()[i][j];
                } else {
                    childNetwork.getHiddenWeights()[i][j] = b.getHiddenWeights()[i][j];
                }
            }
        }

        // Crossover for hidden biases
        for (int i = 0; i < a.getHiddenLayerDim(); i++) {
            // Randomly choose parent for the bias
            if (Math.random() < 0.5) {
                childNetwork.getHiddenBias()[i] = a.getHiddenBias()[i];
            } else {
                childNetwork.getHiddenBias()[i] = b.getHiddenBias()[i];
            }
        }

        // Crossover for output weights
        for (int i = 0; i < a.getHiddenLayerDim(); i++) {
            for (int j = 0; j < a.getOutputLayerDim(); j++) {
                // Randomly choose parent for the weight
                if (Math.random() < 0.5) {
                    childNetwork.getOutputWeights()[i][j] = a.getOutputWeights()[i][j];
                } else {
                    childNetwork.getOutputWeights()[i][j] = b.getOutputWeights()[i][j];
                }
            }
        }

        // Crossover for output biases
        for (int i = 0; i < a.getOutputLayerDim(); i++) {
            // Randomly choose parent for the bias
            if (Math.random() < 0.5) {
                childNetwork.getOutputBias()[i] = a.getOutputBias()[i];
            } else {
                childNetwork.getOutputBias()[i] = b.getOutputBias()[i];
            }
        }

        return childNetwork;
    }

}
