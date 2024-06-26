package utils;

import breakout.Breakout;
import breakout.BreakoutGeneticAlgorithm;
import breakout.BreakoutNeuralNetwork;
import pacman.Pacman;
import pacman.PacmanGeneticAlgorithm;
import pacman.PacmanNeuralNetwork;
import utils.Commons;
import utils.GameController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class main {
    public static void main(String[] args) {
        /*
        // THIS IS THE CODE THAT TESTS THE BREAKOUT NEURAL NETWORK AND GENETIC ALGORITHM
        // TO TEST THE TRAINED NN ALL THE LINES SHOULD BE COMMENTED EXCEPT THE LINE 1 AND LINE 2

        //FIND BEST NN
        BreakoutGeneticAlgorithm ga = new BreakoutGeneticAlgorithm();
        BreakoutNeuralNetwork nn = ga.startSearch();

        //GET THE NN VALUES
        double[] values = nn.getNeuralNetwork();

        //ALLOWS TO SAVE THE NN VALUES ON A TXT FILE
        writeValuesToFile(nn.getNeuralNetwork(), "BreakoutGoodValues.txt", nn);

        // CREATE ANOTHER NN WITH THOSE SAME VALUES
        BreakoutNeuralNetwork nn2 = new BreakoutNeuralNetwork(values, true);

        //OR GET THE NN FROM A TXT FILE, FIRST BLOCK IS 0
        BreakoutNeuralNetwork nn2 = new BreakoutNeuralNetwork(readValuesFromFile("BreakoutGoodValues.txt",1), true); //LINE 1

        //RUN THE GAME CONTROLLED BY THE NN WITH THE GUI ON
        Breakout b = new Breakout(nn2, Commons.SEED); //LINE 2
        */


        // THIS IS THE CODE THAT TESTS THE PACMAN NEURAL NETWORK AND GENETIC ALGORITHM
        // TO TEST THE TRAINED NN ALL THE LINES SHOULD BE COMMENTED EXCEPT THE LINE 1 AND LINE 2

        /*
        //FIND BEST PACMAN NN
        PacmanGeneticAlgorithm g = new PacmanGeneticAlgorithm();
        PacmanNeuralNetwork nn = g.startSearch();

        //GET THE NN VALUES
        double[] values = nn.getNeuralNetwork();

        //ALLOWS TO SAVE THE NN VALUES ON A TXT FILE
        writeValuesToFile(nn.getNeuralNetwork(), "PacmanGoodValues.txt", nn);

        // CREATE ANOTHER NN WITH THOSE SAME VALUES
        PacmanNeuralNetwork nn2 = new PacmanNeuralNetwork(values, false);

        //OR GET THE NN FROM A TXT FILE, FIRST BLOCK IS 0
        PacmanNeuralNetwork nn2 = new PacmanNeuralNetwork(readValuesFromFile("PacmanGoodValues.txt",0), true); //LINE 1

        //RUN THE GAME CONTROLLED BY THE NN WITH THE GUI ON
        Pacman p = new Pacman(nn2, true,  Commons.SEED); //LINE 2
        */
    }


    //FUNCTION THAT WRITES THE VALUES OF A GIVEN GAME CONTROLLER ON A TXT FILE
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

    //FUNCTION THAT READS THOSE VALUES FROM THE FILE
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

