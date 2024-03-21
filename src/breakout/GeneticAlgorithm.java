package breakout;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GeneticAlgorithm {
    private static final int NUM_POPULATION = 100;
    private static final double MUTATION_RATE = 0.10;

    private static final double CHANGE_RATE = 0.50;
    private static final double NUM_GENERATIONS = 1200;
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
                    newPopulation[z] = mutation(newPopulation[z]);
                }
            }
            population = newPopulation;
        }
        System.out.println(bestNN.getCachedFitness());
        return bestNN;
    }

    private BreakoutNeuralNetwork selectParent() {
        double totalFitness = 0.0;
        for (BreakoutNeuralNetwork nn : population) {
            totalFitness += nn.getCachedFitness();
        }

        double targetSelection = Math.random() * totalFitness;
        double accumulatedFitness = 0.0;

        for (BreakoutNeuralNetwork nn : population) {
            accumulatedFitness += nn.getCachedFitness();
            if (accumulatedFitness >= targetSelection) {
                return nn;
            }
        }
        return population[population.length - 1];
    }

    private BreakoutNeuralNetwork crossOver(BreakoutNeuralNetwork a, BreakoutNeuralNetwork b) {
        BreakoutNeuralNetwork childNetwork = new BreakoutNeuralNetwork();

        int crossoverPoint = (int) (Math.random() * (a.getInputLayerDim() + a.getHiddenLayerDim() + a.getOutputLayerDim()));

        int currentPoint = 0;

        // Crossover for hidden weights
        for (int i = 0; i < a.getInputLayerDim(); i++) {
            for (int j = 0; j < a.getHiddenLayerDim(); j++) {
                childNetwork.getHiddenWeights()[i][j] = currentPoint < crossoverPoint
                        ? a.getHiddenWeights()[i][j]
                        : b.getHiddenWeights()[i][j];
                currentPoint++;
            }
        }

        // Crossover for output weights
        for (int i = 0; i < a.getHiddenLayerDim(); i++) {
            for (int j = 0; j < a.getOutputLayerDim(); j++) {
                childNetwork.getOutputWeights()[i][j] = currentPoint < crossoverPoint
                        ? a.getOutputWeights()[i][j]
                        : b.getOutputWeights()[i][j];
                currentPoint++;
            }
        }

        // Crossover for biases
        for (int i = 0; i < a.getHiddenLayerDim(); i++) {
            childNetwork.getHiddenBias()[i] = currentPoint < crossoverPoint
                    ? a.getHiddenBias()[i]
                    : b.getHiddenBias()[i];
            currentPoint++;
        }
        for (int i = 0; i < a.getOutputLayerDim(); i++) {
            childNetwork.getOutputBias()[i] = currentPoint < crossoverPoint
                    ? a.getOutputBias()[i]
                    : b.getOutputBias()[i];
            currentPoint++;
        }

        return childNetwork;
    }

    private BreakoutNeuralNetwork mutation(BreakoutNeuralNetwork nn){

        for (int i = 0; i < nn.getInputLayerDim(); i++) {
            for (int j = 0; j < nn.getHiddenLayerDim(); j++) {
                if (Math.random() < CHANGE_RATE) {
                    double value = Math.random();
                    nn.getHiddenWeights()[i][j]=value;
                }
            }
        }

        for (int i = 0; i < nn.getHiddenLayerDim(); i++) {
            if (Math.random() < CHANGE_RATE) {
                double value = Math.random();
                nn.getHiddenBias()[i] = value;
            }
        }

        for (int i = 0; i < nn.getHiddenLayerDim(); i++) {
            for (int j = 0; j < nn.getOutputLayerDim(); j++) {
                if (Math.random() < CHANGE_RATE) {
                    double value = Math.random();
                    nn.getOutputWeights()[i][j]=value;
                }
            }
        }

        for (int i = 0; i < nn.getOutputLayerDim(); i++) {
            if (Math.random() < CHANGE_RATE) {
                double value = Math.random();
                nn.getOutputBias()[i] = value;
            }
        }
        return nn;
    }

}
