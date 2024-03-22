package pacman;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PacmanGeneticAlgorithm {
    private static final int NUM_POPULATION = 100;
    private static final double MUTATION_RATE = 0.10;

    private static final double CHANGE_RATE = 0.50;
    private static final double NUM_GENERATIONS = 2;
    private static final double TOP_KEEPERS = 5;
    private PacmanNeuralNetwork[] population;

    public PacmanGeneticAlgorithm(){
        population = new PacmanNeuralNetwork[NUM_POPULATION];
        createPopulation();
    }
    private void createPopulation() {
        for(int i = 0; i < NUM_POPULATION; i++){
            PacmanNeuralNetwork nn = new PacmanNeuralNetwork();
            population[i] = nn;
        }
    }

    public PacmanNeuralNetwork startSearch() {
        PacmanNeuralNetwork bestNN = population[0];
        for (int i = 0; i < NUM_GENERATIONS; i++) {

            PacmanNeuralNetwork[] newPopulation = new PacmanNeuralNetwork[NUM_POPULATION];

            List<PacmanNeuralNetwork> populationList = Arrays.asList(population);

            populationList.sort(Comparator.comparingDouble(PacmanNeuralNetwork::getCachedFitness));
            populationList.sort((nn1, nn2) -> (int) (nn1.getCachedFitness() - nn2.getCachedFitness()));
            populationList.toArray(population);

            PacmanNeuralNetwork bestInPopulation = population[99];
            PacmanNeuralNetwork worstInPopulation = population[0];
            PacmanNeuralNetwork middleInPopulation = population[50];

            System.out.println("Generation Number: " + i);
            //System.out.println("Best = " + bestInPopulation.getCachedFitness() + "middle = " + middleInPopulation.getCachedFitness() + "worst = " + worstInPopulation.getCachedFitness());

            if (bestInPopulation.getCachedFitness() > bestNN.getCachedFitness()) {
                bestNN = bestInPopulation;
                System.out.println("Best neural network updated! Fitness: " + bestNN.getCachedFitness());
            }

            for (int j = 0; j < NUM_POPULATION / 2; j++) {
                PacmanNeuralNetwork parentOne = selectParent();
                PacmanNeuralNetwork parentTwo = selectParent();
                PacmanNeuralNetwork childrenOne = crossOver(parentOne, parentTwo);
                PacmanNeuralNetwork childrenTwo = crossOver(parentOne, parentTwo);
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

    private PacmanNeuralNetwork selectParent() {
        double totalFitness = 0.0;
        for (PacmanNeuralNetwork nn : population) {
            totalFitness += nn.getCachedFitness();
        }

        double targetSelection = Math.random() * totalFitness;
        double accumulatedFitness = 0.0;

        for (PacmanNeuralNetwork nn : population) {
            accumulatedFitness += nn.getCachedFitness();
            if (accumulatedFitness >= targetSelection) {
                return nn;
            }
        }
        return population[population.length - 1];
    }

    private PacmanNeuralNetwork crossOver(PacmanNeuralNetwork a, PacmanNeuralNetwork b) {
        PacmanNeuralNetwork childNetwork = new PacmanNeuralNetwork();

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

    private PacmanNeuralNetwork mutation(PacmanNeuralNetwork nn){

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
