import breakout.Breakout;
import breakout.BreakoutGeneticAlgorithm;
import breakout.BreakoutNeuralNetwork;
import utils.Commons;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class main {
    public static void main(String[] args){
        //Find best nn
        BreakoutGeneticAlgorithm ga = new BreakoutGeneticAlgorithm();
        BreakoutNeuralNetwork nn = ga.startSearch();
        double[] values = nn.getNeuralNetwork();

        //Write values in txt
        writeValuesToFile(values, "BreakoutGoodValues.txt", nn);

        //Runs the best nn
        //BreakoutNeuralNetwork nn2 = new BreakoutNeuralNetwork(values);
        //Breakout b = new Breakout(nn2, Commons.SEED);


        // CORRIGIR ISTO PARA O SEGUNDO +++ BLOCOS Está a saltar um valor
        //BreakoutNeuralNetwork nn2 = new BreakoutNeuralNetwork(readValuesFromFile("BreakoutGoodValues.txt",0));
        //System.out.println(nn2.toString());
        //Breakout b = new Breakout(nn2, Commons.SEED);
        //

    }
    /*
    public static void main(String[] args){
        PacmanGeneticAlgorithm g = new PacmanGeneticAlgorithm();
        PacmanNeuralNetwork nn = g.startSearch();

        Pacman p = new Pacman(nn, true, Commons.SEED);
    }
     */

    private static void writeValuesToFile(double[] values, String fileName, BreakoutNeuralNetwork nn) {
        try {
            FileWriter writer = new FileWriter(fileName, true);
            writer.write("Starting here\n");
            writer.write("Fitness value = " + nn.getCachedFitness() + "\n");
            for (double value : values) {
                writer.write(Double.toString(value) + "\n");
            }

            writer.close();
            System.out.println("Values have been appended");
        } catch (IOException e) {
            System.out.println("An error occurred while appending the values");
            e.printStackTrace();
        }
    }

    private static double[] readValuesFromFile(String fileName, int blockIndex) {
        double[] values = new double[62];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            int count = 0;
            int startLine = blockIndex * 63 ; // Adjusted starting line considering the extra lines
            int lineNumber = 0;
            int linesToSkip = 2; // Number of lines to skip
            while ((line = reader.readLine()) != null && count < 62) {
                if (lineNumber >= startLine) {
                    if (linesToSkip > 0) {
                        linesToSkip--;
                        lineNumber++;
                        continue; // Skip the line
                    }
                    try {
                        values[count] = Double.parseDouble(line);
                        count++;
                    } catch (NumberFormatException e) {
                        // If the line is not a valid double, just continue to the next line
                        continue;
                    }
                }
                lineNumber++;
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file");
            e.printStackTrace();
        }
        return values;
    }

}

