package breakout;
import pacman.PacmanNeuralNetwork;
import utils.Commons;

import java.util.Arrays;
import java.util.List;

public class BreakoutGeneticAlgorithm {

    private static final int NUM_POPULATION = 100;
    private static final double MUTATION_RATE = 0.25;

    private static final double CHANGE_RATE = 0.70;
    private static final double NUM_GENERATIONS = 1000;
    private static final int TOP_KEEPERS = 4;
    private BreakoutNeuralNetwork[] population;

    public BreakoutGeneticAlgorithm(){
        population = new BreakoutNeuralNetwork[NUM_POPULATION];
        createPopulation();
    }
    private void createPopulation() {
        for(int i = 0; i < NUM_POPULATION; i++){
            BreakoutNeuralNetwork nn = new BreakoutNeuralNetwork(false);
            population[i] = nn;
        }
    }

    public BreakoutNeuralNetwork startSearch() {
        int notImprovedfor = 0;
        BreakoutNeuralNetwork bestNN = new BreakoutNeuralNetwork(false);

        int i = 0;
        while(bestNN.getCachedFitness()  < 2000000){
        //for (int i = 0; i < NUM_GENERATIONS; i++){

            notImprovedfor++;
            BreakoutNeuralNetwork[] newPopulation = new BreakoutNeuralNetwork[NUM_POPULATION];

            if(notImprovedfor > 4000){
                notImprovedfor = 0;
                createPopulation();
                newPopulation = population;
                System.out.println("Reseted Population");
            }

            //Sort na população pelo fitness
            List<BreakoutNeuralNetwork> populationList = Arrays.asList(population);
            populationList.sort((nn1, nn2) -> (int) ((nn2.getCachedFitness() - nn1.getCachedFitness())));
            populationList.toArray(population);

            //Guarda uma referencia para o melhor da população, na posição 0 do array
            BreakoutNeuralNetwork bestInPopulation = population[0];

            //Caso o melhor da geração seja melhor que a melhor rede atualmente, atualiza essa rede
            if (bestInPopulation.getCachedFitness() > bestNN.getCachedFitness()) {
                notImprovedfor = 0;
                bestNN = new BreakoutNeuralNetwork(bestInPopulation);
                System.out.println("Generation Number: " + i);
                System.out.println("Best neural network updated! Fitness: " + bestNN.getCachedFitness());
            }

            if(i % 50 == 0){
                System.out.println("Geracao = " + i);
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
        return population[0];
    }

    private BreakoutNeuralNetwork crossOver(BreakoutNeuralNetwork a, BreakoutNeuralNetwork b) {
        BreakoutNeuralNetwork childNetwork = new BreakoutNeuralNetwork(false);

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
                    double value = (Math.random() * 2 - 1);
                    nn.getHiddenWeights()[i][j] += value; // ANTES ERA APENAS DEFINIDO ENTRE 0 - 1
                }
            }
        }

        for (int i = 0; i < nn.getHiddenLayerDim(); i++) {
            if (Math.random() < CHANGE_RATE) {
                double value = (Math.random() * 2 - 1);
                nn.getHiddenBias()[i] += value; // ANTES ERA DEFINIDO ENTRE (0 - 1) * BIAS_FACTOR
            }
        }

        for (int i = 0; i < nn.getHiddenLayerDim(); i++) {
            for (int j = 0; j < nn.getOutputLayerDim(); j++) {
                if (Math.random() < CHANGE_RATE) {
                    double value = (Math.random() * 2 - 1);
                    nn.getOutputWeights()[i][j] += value; // ANTES ERA APENAS DEFINIDO ENTRE 0 - 1
                }
            }
        }

        for (int i = 0; i < nn.getOutputLayerDim(); i++) {
            if (Math.random() < CHANGE_RATE) {
                double value = (Math.random() * 2 - 1);
                nn.getOutputBias()[i] += value; // ANTES ERA DEFINIDO ENTRE (0 - 1) * BIAS_FACTOR
            }
        }
        nn.precomputeFitness();
        return nn;
    }

}
