package pacman;

import utils.GameController;

public class PacmanNeuralNetwork implements GameController {

    private final int inputLayerDim;
    private final int outputLayerDim;
    private final int hiddenLayerDim;

    //Matrix that defines the weights of the input and hiddent layers
    private double[][] hiddenWeights;
    private double[][] outputWeights;

    //Arrays to define the bias of the hidden layer and the output layer
    private double[] hiddenBias;
    private double[] outputBias;

    private Double cachedFitness = null;

    public PacmanNeuralNetwork(int inputLayerDim, int outputLayerDim, int hiddenLayerDim) {
        this.inputLayerDim = inputLayerDim;
        this.outputLayerDim = outputLayerDim;
        this.hiddenLayerDim = hiddenLayerDim;
    }

    @Override
    public int nextMove(int[] currentState) {
        return 0;
    }
}
