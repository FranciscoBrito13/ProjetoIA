import breakout.Breakout;
import breakout.BreakoutGeneticAlgorithm;
import breakout.BreakoutNeuralNetwork;
import pacman.Pacman;
import pacman.PacmanGeneticAlgorithm;
import pacman.PacmanNeuralNetwork;
import utils.Commons;
import utils.GameController;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class main {
    /*public static void main(String[] args){
//      //Find best nn
        //BreakoutGeneticAlgorithm ga = new BreakoutGeneticAlgorithm();
        //BreakoutNeuralNetwork nn = ga.startSearch();
        //double[] values = nn.getNeuralNetwork();

        //Write values in txt
        //writeValuesToFile(values, "BreakoutGoodValues.txt", nn);

       //Runs the best nn
        //BreakoutNeuralNetwork nn2 = new BreakoutNeuralNetwork(values);
        //Breakout b = new Breakout(nn2, Commons.SEED);


        //CORRIGIR ISTO PARA O SEGUNDO +++ BLOCOS Está a saltar um valor
        //BreakoutNeuralNetwork nn2 = new BreakoutNeuralNetwork(readValuesFromFile("BreakoutGoodValues.txt",2));
        //System.out.println(nn2.toString());
        //Breakout b = new Breakout(nn2, Commons.SEED);


    }
    */

    public static void main(String[] args){
        PacmanGeneticAlgorithm g = new PacmanGeneticAlgorithm();
        PacmanNeuralNetwork nn = g.startSearch();
        double[] values = nn.getNeuralNetwork();

        writeValuesToFile(values, "PacmanGoodValues.txt", nn);

        Pacman p = new Pacman(nn, true, Commons.SEED);
    }


    private static void writeValuesToFile(double[] values, String fileName, GameController nn) {
        try {
            FileWriter writer = new FileWriter(fileName, true);
            writer.write("Starting here\n");
            writer.write("Fitness value = " + nn.getCachedFitness() + "\n");
            for (double value : values) {
                writer.write(Double.toString(value) + "\n");
            }
            writer.write("*\n");
            writer.close();
            System.out.println("Values have been appended");
        } catch (IOException e) {
            System.out.println("An error occurred while appending the values");
            e.printStackTrace();
        }
    }

    private static double[] readValuesFromFile(String fileName, int blockIndex) {
        ArrayList<Double> valuesList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("*")) {
                    if (blockIndex >= 0){
                        blockIndex--;
                        continue;
                    }else {
                        break;
                    }
                }
                if (blockIndex < 0){
                    try {
                        valuesList.add(Double.parseDouble(line));
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file");
            e.printStackTrace();
        }
        double[] values = new double[valuesList.size()];
        for(int i = 0; i < values.length; i++){
            values[i] = valuesList.get(i);
        }
        return values;
    }

}

