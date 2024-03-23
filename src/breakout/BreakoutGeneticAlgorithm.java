package breakout;
import utils.Commons;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BreakoutGeneticAlgorithm {

    private static final int NUM_POPULATION = 100;
    private static final double MUTATION_RATE = 0.25;

    private static final double CHANGE_RATE = 0.70;
    private static final double NUM_GENERATIONS = 50000;
    private static final int TOP_KEEPERS = 6;
    private BreakoutNeuralNetwork[] population;

    public BreakoutGeneticAlgorithm(){
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
        int notImprovedfor = 0;
        BreakoutNeuralNetwork bestNN = population[0];
        BreakoutNeuralNetwork[] initialPop = population;
        //for (int i = 0; i < NUM_GENERATIONS; i++) {
        int i = 0;
        while(bestNN.getCachedFitness() < 1200000){

            notImprovedfor++;
            BreakoutNeuralNetwork[] newPopulation = new BreakoutNeuralNetwork[NUM_POPULATION];

            if(notImprovedfor > 15000){
                notImprovedfor = 0;
                newPopulation = initialPop;
                System.out.println("Reseted Population");
            }
            List<BreakoutNeuralNetwork> populationList = Arrays.asList(population);

            populationList.sort((nn1, nn2) -> (int) (nn1.getCachedFitness() - nn2.getCachedFitness()));
            populationList.toArray(population);

            BreakoutNeuralNetwork bestInPopulation = population[NUM_POPULATION - 1];
            //BreakoutNeuralNetwork worstInPopulation = population[0];
            //BreakoutNeuralNetwork middleInPopulation = population[NUM_POPULATION/2];
            //System.out.println("Best = " + bestInPopulation.getCachedFitness() + "middle = " + middleInPopulation.getCachedFitness() + "worst = " + worstInPopulation.getCachedFitness());

            if (bestInPopulation.getCachedFitness() > bestNN.getCachedFitness()) {
                System.out.println("Generation Number: " + i);
                notImprovedfor = 0;
                bestNN = bestInPopulation;
                System.out.println("Best neural network updated! Fitness: " + bestNN.getCachedFitness());
            }
            for(int t = 0; t < TOP_KEEPERS; t++){
                newPopulation[t] = population[t];
            }

            for (int j = 0; j < (NUM_POPULATION - TOP_KEEPERS) / 2; j++) {
                BreakoutNeuralNetwork parentOne = selectParent();
                BreakoutNeuralNetwork parentTwo = selectParent();
                BreakoutNeuralNetwork childrenOne = crossOver(parentOne, parentTwo);
                BreakoutNeuralNetwork childrenTwo = crossOver(parentOne, parentTwo);
                newPopulation[j + TOP_KEEPERS] = childrenOne;
                newPopulation[(j + TOP_KEEPERS) + ((NUM_POPULATION- TOP_KEEPERS)/ 2)] = childrenTwo;
            }

            for (int z = 0; z < NUM_POPULATION; z++) {
                double rand = Math.random();
                if (rand < MUTATION_RATE) {
                    newPopulation[z] = mutation(newPopulation[z]);
                }
            }
            population = newPopulation;
            i++;
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
        return population[NUM_POPULATION - 1];
    }

    private BreakoutNeuralNetwork crossOver(BreakoutNeuralNetwork a, BreakoutNeuralNetwork b) {
        BreakoutNeuralNetwork childNetwork = new BreakoutNeuralNetwork();

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
                nn.getHiddenBias()[i] = value * Commons.BIAS_FACTOR;
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
                nn.getOutputBias()[i] = value * Commons.BIAS_FACTOR;
            }
        }
        return nn;
    }

}
