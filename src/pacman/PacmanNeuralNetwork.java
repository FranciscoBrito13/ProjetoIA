package pacman;

import breakout.BreakoutBoard;
import utils.Commons;
import utils.NeuralNetwork;

public class PacmanNeuralNetwork extends NeuralNetwork {

    public PacmanNeuralNetwork() {
        super(Commons.PACMAN_STATE_SIZE,Commons.PACMAN_NUM_ACTIONS,10);
    }

    public PacmanNeuralNetwork(double[] values) {
        super(Commons.PACMAN_STATE_SIZE,Commons.PACMAN_NUM_ACTIONS,200, values);
    }

    protected double hiddenLayerActivationFunc(double x){
        return sigmoid(x);
    };
    protected double ouputLayerActivationFunc(double x){
        return sigmoid(x);
    }

    public int nextMove(int[] currentState) {
        double[] output = feedForward(currentState);
        int max = 0;
        for(int i = 0; i < output.length; i++){
            if(output[i] > output[max]){
                max = i;

            }
        }
        return max;
    }

    protected void precomputeFitness() {
        PacmanBoard pnn = new PacmanBoard(this, false, Commons.SEED);
        pnn.runSimulation();
        PacmanBoard pnn2 = new PacmanBoard(this, false, Commons.SEED + 50);
        pnn2.runSimulation();
        cachedFitness = (pnn.getFitness() + pnn2.getFitness()) / 2;
    }

    public double getFitness(){
        PacmanBoard pnn = new PacmanBoard(this, false, Commons.SEED);
        pnn.runSimulation();
        return pnn.getFitness();
    }

}
