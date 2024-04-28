package pacman;

import breakout.NeuralNetworkGUI;
import utils.Commons;
import utils.NeuralNetwork;

public class PacmanNeuralNetwork extends NeuralNetwork {
    private NeuralNetworkGUI neuralNetworkGUI;


    public PacmanNeuralNetwork(boolean activateGUI) {
        super(Commons.PACMAN_STATE_SIZE,Commons.PACMAN_NUM_ACTIONS,Commons.HIDDEN_NEURONS_PACMAN);
        if(activateGUI) {
            activateGUI();
        }
    }

    public PacmanNeuralNetwork(double[] values, boolean activateGUI) {
        super(Commons.PACMAN_STATE_SIZE,Commons.PACMAN_NUM_ACTIONS,Commons.HIDDEN_NEURONS_PACMAN, values);
        if(activateGUI) {
            activateGUI();
        }
    }

    private void activateGUI() {
        neuralNetworkGUI = new NeuralNetworkGUI(this);
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
