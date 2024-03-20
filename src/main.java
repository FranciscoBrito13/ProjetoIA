import breakout.Breakout;
import breakout.BreakoutBoard;
import breakout.BreakoutNeuralNetwork;
import breakout.GeneticAlgorithm;

public class main {
    public static void main(String[] args){
        GeneticAlgorithm ga = new GeneticAlgorithm();
        BreakoutNeuralNetwork nn = ga.startSearch();
        Breakout b = new Breakout(nn,10);
        //Breakout b = new Breakout(nn, 1);
        //b.setSeed(1);
        //b.runSimulation();
    }
}

