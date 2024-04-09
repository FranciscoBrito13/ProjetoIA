package pacman;
import utils.Commons;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PacmanGeneticAlgorithm {
    private static final int NUM_POPULATION = 100;
    private static final double MUTATION_RATE = 0.25;

    private static final double CHANGE_RATE = 0.70;
    private static final double NUM_GENERATIONS = 100;
    private static final int TOP_KEEPERS = 6;
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
        int i = 0;
        int notImprovedfor = 0;
        PacmanNeuralNetwork[] initialPop = population;
        //for (int i = 0; i < NUM_GENERATIONS; i++) {
        while ((bestNN.getFitness(Commons.SEED) + bestNN.getFitness(Commons.SEED + 1)) / 2 < 40000) {
            PacmanNeuralNetwork[] newPopulation = new PacmanNeuralNetwork[NUM_POPULATION];

            if(notImprovedfor > 2500){
                notImprovedfor = 0;
                newPopulation = initialPop;
                System.out.println("Reseted Population");
            }
            List<PacmanNeuralNetwork> populationList = Arrays.asList(population);

            populationList.sort((nn1, nn2) -> (int) (((nn1.getFitness(Commons.SEED) + nn1.getFitness(Commons.SEED + 1)) / 2) - ((nn2.getFitness(Commons.SEED) + nn2.getFitness(Commons.SEED + 1)) / 2)));
            populationList.toArray(population);

            PacmanNeuralNetwork bestInPopulation = population[NUM_POPULATION - 1];

            if(i % 50 == 0)
                System.out.println("Generation Number: " + i);

            if ((bestInPopulation.getFitness(Commons.SEED) + bestInPopulation.getFitness(Commons.SEED + 1)) / 2 > (bestNN.getFitness(Commons.SEED) + bestNN.getFitness(Commons.SEED + 1)) / 2) {
                bestNN = bestInPopulation;
                System.out.println("Generation Number: " + i);
                System.out.println("Best neural network updated! Fitness: " + (bestNN.getFitness(Commons.SEED) + bestNN.getFitness(Commons.SEED + 1)) / 2);
            }

            for(int t = 0; t < TOP_KEEPERS; t++){
                newPopulation[t] = population[t];
            }

            for (int j = 0; j < (NUM_POPULATION - TOP_KEEPERS) / 2; j++) {
                PacmanNeuralNetwork parentOne = selectParent();
                PacmanNeuralNetwork parentTwo = selectParent();
                PacmanNeuralNetwork childrenOne = crossOver(parentOne, parentTwo);
                PacmanNeuralNetwork childrenTwo = crossOver(parentOne, parentTwo);
                newPopulation[j + TOP_KEEPERS] = childrenOne;
                newPopulation[(j + TOP_KEEPERS) + ((NUM_POPULATION- TOP_KEEPERS)/ 2)] = childrenTwo;
            }

            for (int z = 0; z < NUM_POPULATION; z++) {
                double rand = Math.random();
                if (rand < MUTATION_RATE) {
                    newPopulation[z] = mutation(newPopulation[z]);
                }
            }
            notImprovedfor++;
            i++;
            population = newPopulation;
        }
        System.out.println((bestNN.getFitness(Commons.SEED) + bestNN.getFitness(Commons.SEED + 1)) / 2);
        return bestNN;
    }


    private PacmanNeuralNetwork selectParent() {
        double totalFitness = 0.0;
        for (PacmanNeuralNetwork nn : population) {
            totalFitness += (nn.getFitness(Commons.SEED)+ nn.getFitness(Commons.SEED + 1)) / 2;
        }

        double targetSelection = Math.random() * totalFitness;
        double accumulatedFitness = 0.0;

        for (PacmanNeuralNetwork nn : population) {
            accumulatedFitness += (nn.getFitness(Commons.SEED) + nn.getFitness(Commons.SEED + 1) / 2);
            if (accumulatedFitness >= targetSelection) {
                return nn;
            }
        }
        return population[population.length - 1];
    }

    private PacmanNeuralNetwork crossOver(PacmanNeuralNetwork a, PacmanNeuralNetwork b) {
        PacmanNeuralNetwork childNetwork = new PacmanNeuralNetwork();

        // Crossover for hidden weights
        for (int i = 0; i < a.getInputLayerDim(); i++) {
            for (int j = 0; j < a.getHiddenLayerDim(); j++) {
                childNetwork.getHiddenWeights()[i][j] = (a.getHiddenWeights()[i][j] + b.getHiddenWeights()[i][j]) / 2;
            }
        }

        // Crossover for output weights
        for (int i = 0; i < a.getHiddenLayerDim(); i++) {
            for (int j = 0; j < a.getOutputLayerDim(); j++) {
                childNetwork.getOutputWeights()[i][j] = (a.getOutputWeights()[i][j] + b.getOutputWeights()[i][j]) / 2;
            }
        }

        // Crossover for biases
        for (int i = 0; i < a.getHiddenLayerDim(); i++) {
            childNetwork.getHiddenBias()[i] = (a.getHiddenBias()[i] + b.getHiddenBias()[i]) / 2;
        }
        for (int i = 0; i < a.getOutputLayerDim(); i++) {
            childNetwork.getOutputBias()[i] = (a.getOutputBias()[i] + b.getOutputBias()[i]) / 2;
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
                nn.getHiddenBias()[i] = value * Commons.BIAS_FACTOR_BREAKOUT;
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
                nn.getOutputBias()[i] = value * Commons.BIAS_FACTOR_BREAKOUT;
            }
        }
        return nn;
    }

}
