import breakout.Breakout;
import breakout.BreakoutGeneticAlgorithm;
import breakout.BreakoutNeuralNetwork;
import breakout.NeuralNetworkGUI;
import pacman.Pacman;
import pacman.PacmanGeneticAlgorithm;
import pacman.PacmanNeuralNetwork;
import utils.Commons;
import utils.GameController;

import javax.swing.*;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class main {
    public static void main(String[] args){
        //Find best nn
//        BreakoutGeneticAlgorithm ga = new BreakoutGeneticAlgorithm();
//        BreakoutNeuralNetwork nn = ga.startSearch();
//
//        double[] values = nn.getNeuralNetwork();
//
//        BreakoutNeuralNetwork nn2  = new BreakoutNeuralNetwork(values, true);
//        Breakout b = new Breakout(nn2, Commons.SEED);

        BreakoutNeuralNetwork nn2 = new BreakoutNeuralNetwork(readValuesFromFile("BreakoutGoodValues.txt",2), true);
        System.out.println(nn2.toString());
        Breakout b = new Breakout(nn2, Commons.SEED);


//        PacmanGeneticAlgorithm g = new PacmanGeneticAlgorithm();
//        PacmanNeuralNetwork nn = g.startSearch();
//        writeValuesToFile(nn.getNeuralNetwork(), "PacmanGoodValues.txt", nn);

//        double[] values = readValuesFromFile("PacmanGoodValues.txt", 0);
//        PacmanNeuralNetwork nn = new PacmanNeuralNetwork(values);

       // Pacman p = new Pacman(nn, true, Commons.SEED);
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

