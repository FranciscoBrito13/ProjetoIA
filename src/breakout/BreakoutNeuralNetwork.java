package breakout;

import utils.Commons;
import utils.GameController;

public class BreakoutNeuralNetwork implements GameController {

    //Defines the dimensions of the input, hidden, and output layers
    private final int inputLayerDim;
    private final int outputLayerDim;
    private final int hiddenLayerDim;

    //Matrix that defines the weights of the input and hiddent layers
    private double[][] hiddenWeights;
    private double[][] outputWeights;

    //Arrays to define the bias of the hidden layer and the output layer
    private double[] hiddenBias;
    private double[] outputBias;


    public BreakoutNeuralNetwork(){

        this.inputLayerDim = Commons.BREAKOUT_STATE_SIZE;
        this.outputLayerDim = Commons.BREAKOUT_NUM_ACTIONS;
        this.hiddenLayerDim = (int) Math.round(Math.sqrt(inputLayerDim * outputLayerDim)); //Rule of thumb for number of nodes

        initializeRandomParameters();

    }
    //This method will set random values between 0 and 0.5 for the weights and the bias of the NN
    private void initializeRandomParameters() {
        //Deals with the input weights
        //W11 W21 W31   Wij, i = input neuron, j = hidden neuron
        //W12 W22 W32
        //W13 W23 W33
        double weightRange = 0.5;
        hiddenWeights = new double[inputLayerDim][hiddenLayerDim];
        for(int i = 0; i < inputLayerDim; i++){
            for(int j = 0; j < hiddenLayerDim; j++){
                hiddenWeights[i][j] = Math.random();
            }
        }

        //Deal with the hidden Bias
        hiddenBias = new double[hiddenLayerDim];
        for(int i = 0; i < hiddenLayerDim; i++){
            hiddenBias[i] = Math.random();
        }
        //Deals with the hidden weights
        outputWeights = new double[hiddenLayerDim][outputLayerDim];
        for(int i = 0; i < hiddenLayerDim; i++){
            for(int j = 0; j < outputLayerDim; j++){
                outputWeights[i][j] = Math.random();
            }
        }
        //Deal with the output Bias
        outputBias = new double[outputLayerDim];
        for(int i = 0; i < outputLayerDim; i++){
            outputBias[i] = Math.random();
        }

    }

    private double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    } // Sigmoid activation function
    private double relu(double x) {
        return Math.max(0,x);
    } // relu activation function
    public double[] feedForward(int[] inputValues){
       //for(int value : inputValues){
        //    System.out.println(value);
        //}

        if(inputValues.length != Commons.BREAKOUT_STATE_SIZE)
            throw new IllegalArgumentException("Wrong amount of input values");

        double[] hiddenLayerOutput = new double[hiddenLayerDim];

        for (int i = 0; i < hiddenLayerDim; i++) {
            double neuronSum = 0.0;
            for (int j = 0; j < inputLayerDim; j++) {
                neuronSum += inputValues[j] * hiddenWeights[j][i];
            }
            neuronSum += hiddenBias[i];
            hiddenLayerOutput[i] = sigmoid(neuronSum);
        }
        double[] outputLayerOutput = new double[outputLayerDim];
        for (int i = 0; i < outputLayerDim; i++) {
            double neuronSum = 0.0;
            for (int j = 0; j < hiddenLayerDim; j++) {
                neuronSum += hiddenLayerOutput[j] * outputWeights[j][i];
            }
            neuronSum += outputBias[i];
            outputLayerOutput[i] = sigmoid(neuronSum);
        }
        return outputLayerOutput;
    }

    @Override
    public int nextMove(int[] currentState) {
        double[] output = feedForward(currentState);

        double sum = output[0] + output[1];
        output[0] /= sum;
        output[1] /= sum;
        //System.out.println("Value 1: " + output[0]);
        //System.out.println("Value 2: " + output[1]);
        double randomValue = Math.random(); //
        if (randomValue < output[1]) {
            return 1; // goes to the left
        } else {
            return 2; // goes to the right
        }
    }

    public int getInputLayerDim() {
        return inputLayerDim;
    }

    public int getOutputLayerDim() {
        return outputLayerDim;
    }

    public int getHiddenLayerDim() {
        return hiddenLayerDim;
    }

    public double[][] getHiddenWeights() {
        return hiddenWeights;
    }

    public double[][] getOutputWeights() {
        return outputWeights;
    }

    public double[] getHiddenBias() {
        return hiddenBias;
    }

    public double[] getOutputBias() {
        return outputBias;
    }

}
