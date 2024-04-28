package breakout;


import utils.NeuralNetwork;
import javax.swing.*;
import java.awt.*;

public class NeuralNetworkGUI {

    private JFrame frame;
    private NeuralNetworkPanel neuralNetworkPanel;

    public NeuralNetworkGUI(NeuralNetwork neuralNetwork) {
        frame = new JFrame("Neural Network");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);

        neuralNetworkPanel = new NeuralNetworkPanel(7,6,2);
        frame.add(neuralNetworkPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}