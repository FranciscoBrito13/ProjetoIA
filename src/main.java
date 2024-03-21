import breakout.Breakout;
import breakout.BreakoutBoard;
import breakout.BreakoutNeuralNetwork;
import breakout.GeneticAlgorithm;
import utils.Commons;

public class main {
    public static void main(String[] args){
        GeneticAlgorithm ga = new GeneticAlgorithm();
        BreakoutNeuralNetwork nn = ga.startSearch();

        Breakout b = new Breakout(nn, Commons.SEED);
    }
}

