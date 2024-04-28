package breakout;

import utils.Commons;
import utils.NeuralNetwork;

public class BreakoutNeuralNetwork extends NeuralNetwork {

    public BreakoutNeuralNetwork(){
        super(Commons.BREAKOUT_STATE_SIZE,Commons.BREAKOUT_NUM_ACTIONS,6);
    }
    public BreakoutNeuralNetwork(double[] values) {
        super(Commons.BREAKOUT_STATE_SIZE,Commons.BREAKOUT_NUM_ACTIONS,6,values);
    }

    protected double hiddenLayerActivationFunc(double x){
        return sigmoid(x);
    };
    protected double ouputLayerActivationFunc(double x){
        return sigmoid(x);
    }

    public int nextMove(int[] currentState) {
        double[] output = feedForward(currentState);

        if (output[0] < output[1]) {
            return 1; // Move-se para a esquerda
        } else {
            return 2; // Move-se para a direita
        }
    }

    protected void precomputeFitness() {
        BreakoutBoard bnn = new BreakoutBoard(this, false, Commons.SEED);
        bnn.runSimulation();
        BreakoutBoard bnn2 = new BreakoutBoard(this, false, Commons.SEED + 50);
        bnn2.runSimulation();
        cachedFitness = (bnn.getFitness() + bnn2.getFitness()) / 2;
    }




}
