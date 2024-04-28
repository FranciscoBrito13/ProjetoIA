package utils;

import java.util.Arrays;
import java.util.function.Function;
 public abstract class NeuralNetwork implements GameController {
    //Defines the dimensions of the input, hidden, and output layers
    private final int inputLayerDim;
    private final int outputLayerDim;
    private final int hiddenLayerDim;

    //Matrix that defines the weights of the input and hidden layers
    private double[][] hiddenWeights;
    private double[][] outputWeights;

    //Arrays to define the bias of the hidden layer and the output layer
    private double[] hiddenBias;
    private double[] outputBias;

    protected Double cachedFitness = null;
    private double[] inputValues;
    private double[] hiddenValues;
    private double[] outputValues;


     public double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    } // Sigmoid activation function
    public double relu(double x) {
        return Math.max(0,x);
    } // relu activation function

    protected NeuralNetwork(int inputLayerDim, int outputLayerDim, int hiddenLayerDim){
        this.inputLayerDim = inputLayerDim;
        this.outputLayerDim = outputLayerDim;
        this.hiddenLayerDim = hiddenLayerDim;
        initializeRandomParameters();

    }

    protected NeuralNetwork(int inputLayerDim, int outputLayerDim, int hiddenLayerDim, double[] values){
        this.inputLayerDim = inputLayerDim;
        this.outputLayerDim = outputLayerDim;
        this.hiddenLayerDim = hiddenLayerDim;
        initializeParameters(values);
    }

    private void initializeRandomParameters() {
        hiddenWeights = new double[inputLayerDim][hiddenLayerDim];
        inputValues = new double[inputLayerDim];
        for(int i = 0; i < inputLayerDim; i++){
            for(int j = 0; j < hiddenLayerDim; j++){
                hiddenWeights[i][j] = Math.random();
            }
            inputValues[i] = hiddenWeights[i][0];
        }

        //Deal with the hidden Bias
        hiddenBias = new double[hiddenLayerDim];
        hiddenValues = new double[hiddenLayerDim];
        for(int i = 0; i < hiddenLayerDim; i++){
            hiddenBias[i] = (Math.random() * 2) - 1;
            hiddenValues[i] = hiddenBias[i];
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
        outputValues = new double[outputLayerDim];
        for(int i = 0; i < outputLayerDim; i++){
            outputBias[i] = (Math.random() * 2) - 1;
            outputValues[i] = outputBias[i];
        }

    }

    private void initializeParameters(double[] values) {
        int expectedLength = inputLayerDim * hiddenLayerDim + hiddenLayerDim + hiddenLayerDim * outputLayerDim + outputLayerDim;
        if (values.length != expectedLength) {
            throw new IllegalArgumentException("Wrong amount of arguments");
        }

        hiddenWeights = new double[inputLayerDim][hiddenLayerDim];
        inputValues = new double[inputLayerDim];
        int index = 0;

        for (int i = 0; i < inputLayerDim; i++) {
            for (int j = 0; j < hiddenLayerDim; j++) {
                hiddenWeights[i][j] = values[index];
                index++;
            }
            inputValues[i] = hiddenWeights[i][0];
        }

        hiddenBias = new double[hiddenLayerDim];
        hiddenValues = new double[hiddenLayerDim];
        for (int i = 0; i < hiddenLayerDim; i++) {
            hiddenBias[i] = values[index];
            hiddenValues[i] = hiddenBias[i];
            index++;
        }

        outputWeights = new double[hiddenLayerDim][outputLayerDim];
        for (int i = 0; i < hiddenLayerDim; i++) {
            for (int j = 0; j < outputLayerDim; j++) {
                outputWeights[i][j] = values[index];
                index++;
            }
        }
        outputBias = new double[outputLayerDim];
        outputValues = new double[outputLayerDim];
        for (int i = 0; i < outputLayerDim; i++) {
            outputBias[i] = values[index];
            outputValues[i] = outputBias[i];
            index++;
        }
    }

    abstract protected double hiddenLayerActivationFunc(double x);
    abstract protected double ouputLayerActivationFunc(double x);

    private double[] normalizeInput(int[] inputValues) {
        double[] normalizedInput = new double[inputValues.length];
        double mean = calculateMean(inputValues);

        for (int i = 0; i < inputValues.length; i++) {
            normalizedInput[i] = inputValues[i] - mean;
        }

        return normalizedInput;
    }

    private double calculateMean(int[] inputValues) {
        double sum = 0.0;
        for (int value : inputValues) {
            sum += value;
        }
        return sum / inputValues.length;
    }

    protected double[] feedForward(int[] inputValues){
        for(int i = 0; i < inputValues.length; i++){
            this.inputValues[i] = (double) inputValues[i];
        }
        // Normalize input data
        double[] normalizedInput = normalizeInput(inputValues);

        double[] hiddenLayerOutput = new double[hiddenLayerDim];

        for (int i = 0; i < hiddenLayerDim; i++) {
            double neuronSum = 0.0;
            for (int j = 0; j < inputLayerDim; j++) {
                neuronSum += normalizedInput[j] * hiddenWeights[j][i];
            }
            neuronSum += hiddenBias[i];
            hiddenLayerOutput[i] = hiddenLayerActivationFunc(neuronSum);
        }
        this.hiddenValues = hiddenLayerOutput;
        double[] outputLayerOutput = new double[outputLayerDim];
        for (int i = 0; i < outputLayerDim; i++) {
            double neuronSum = 0.0;
            for (int j = 0; j < hiddenLayerDim; j++) {
                neuronSum += hiddenLayerOutput[j] * outputWeights[j][i];
            }
            neuronSum += outputBias[i];
            outputLayerOutput[i] = ouputLayerActivationFunc(neuronSum);
        }
        this.outputValues = outputLayerOutput;
        //System.out.println(outputLayerOutput[0] + " " + outputLayerOutput[1]);
        return outputLayerOutput;
    }

    @Override
    abstract public int nextMove(int[] currentState);

    abstract  protected void precomputeFitness();
    @Override
    public double getCachedFitness() {
        if (this.cachedFitness == null) {
            precomputeFitness();
        }
        return this.cachedFitness;
    }

    @Override
    public String toString() {
        String result = "Neural Network: \nNumber of inputs: "
                + inputLayerDim + "\n"
                + "Weights between input and hidden layer with " + hiddenLayerDim + " neurons: \n";
        String hidden = "";
        for (int input = 0; input < inputLayerDim; input++) {
            for (int i = 0; i < hiddenLayerDim; i++) {
                hidden += " input" + input + "-hidden" + i + ": "
                        + hiddenWeights[input][i] + "\n";
            }
        }
        result += hidden;
        String biasHidden = "Hidden biases: \n";
        for (int i = 0; i < hiddenLayerDim; i++) {
            biasHidden += " bias hidden" + i + ": " + hiddenBias[i] + "\n";
        }
        result += biasHidden;
        String output = "Weights between hidden and output layer with "
                + outputLayerDim + " neurons: \n";
        for (int hiddenw = 0; hiddenw < hiddenLayerDim; hiddenw++) {
            for (int i = 0; i < outputLayerDim; i++) {
                output += " hidden" + hiddenw + "-output" + i + ": "
                        + outputWeights[hiddenw][i] + "\n";
            }
        }
        result += output;
        String biasOutput = "Ouput biases: \n";
        for (int i = 0; i < outputLayerDim; i++) {
            biasOutput += " bias ouput" + i + ": " + outputBias[i] + "\n";
        }
        result += biasOutput;
        return result;
    }

    public double[] getNeuralNetwork() {
        double[] values = new double[(inputLayerDim * hiddenLayerDim) + hiddenLayerDim + (hiddenLayerDim * outputLayerDim) + outputLayerDim];

        int index = 0;
        for (int i = 0; i < inputLayerDim; i++) {
            for (int j = 0; j < hiddenLayerDim; j++) {
                values[index++] = hiddenWeights[i][j];
            }
        }

        for (int i = 0; i < hiddenLayerDim; i++) {
            values[index++] = hiddenBias[i];
        }

        for (int i = 0; i < hiddenLayerDim; i++) {
            for (int j = 0; j < outputLayerDim; j++) {
                values[index++] = outputWeights[i][j];
            }
        }
        for (int i = 0; i < outputLayerDim; i++) {
            values[index++] = outputBias[i];
        }
        return values;
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

     public double[] getInputValues() {
         return inputValues;
     }

     public double[] getHiddenValues() {
         return hiddenValues;
     }

     public double[] getOutputValues() {
         return outputValues;
     }
 }
