import pacman.Pacman;
import pacman.PacmanGeneticAlgorithm;
import pacman.PacmanNeuralNetwork;
import utils.Commons;

public class main {
    /*
    public static void main(String[] args){
        GeneticAlgorithm ga = new GeneticAlgorithm();
        BreakoutNeuralNetwork nn = ga.startSearch();

        Breakout b = new Breakout(nn, Commons.SEED);
    }
     */
    public static void main(String[] args){
        PacmanGeneticAlgorithm g = new PacmanGeneticAlgorithm();
        PacmanNeuralNetwork nn = g.startSearch();

        Pacman p = new Pacman(nn, true, Commons.SEED);
    }

}

