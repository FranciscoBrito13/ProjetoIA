package breakout;

import utils.Commons;
import utils.NeuralNetwork;

public class BreakoutNeuralNetwork extends NeuralNetwork {

    private Double cachedFitness = null;


    public BreakoutNeuralNetwork(){
        super(Commons.BREAKOUT_STATE_SIZE,Commons.BREAKOUT_NUM_ACTIONS,6);
    }
    public BreakoutNeuralNetwork(double[] values) {
        super(Commons.BREAKOUT_STATE_SIZE,Commons.BREAKOUT_NUM_ACTIONS,6,values);
    }

    public double hiddenLayerActivationFunc(double x){
        return sigmoid(x);
    };
    public double ouputLayerActivationFunc(double x){
        return sigmoid(x);
    }

    public double[] normalizeInput(int[] inputValues) {
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

    public int nextMove(int[] currentState) {
        double[] output = feedForward(currentState);

        if (output[0] < output[1]) {
            return 1; // Move-se para a esquerda
        } else {
            return 2; // Move-se para a direita
        }
    }

    public void precomputeFitness() {
        BreakoutBoard bnn = new BreakoutBoard(this, false, Commons.SEED);
        bnn.runSimulation();
        BreakoutBoard bnn2 = new BreakoutBoard(this, false, Commons.SEED + 50);
        bnn2.runSimulation();
        cachedFitness = (bnn.getFitness() + bnn2.getFitness()) / 2;
    }
    @Override
    public double getCachedFitness() {
        if (this.cachedFitness == null) {
            precomputeFitness();
        }
        return this.cachedFitness;
    }




}
