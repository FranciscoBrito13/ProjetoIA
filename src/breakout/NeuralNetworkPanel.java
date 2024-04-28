package breakout;

import utils.NeuralNetwork;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NeuralNetworkPanel extends JPanel {

    private static final int NEURON_SIZE = 20; // Diameter of each neuron
    private static final int SPACE_BETWEEN_NEURONS = 40; // Space between neurons
    private static final int LAYER_GAP = 100; // Space between layers
    private static final Color NEURON_COLOR = Color.BLACK;

    private int inputLayerSize;
    private int hiddenLayerSize;
    private int outputLayerSize;

    public NeuralNetworkPanel(int inputLayerSize, int hiddenLayerSize, int outputLayerSize) {
        this.inputLayerSize = inputLayerSize;
        this.hiddenLayerSize = hiddenLayerSize;
        this.outputLayerSize = outputLayerSize;
        setPreferredSize(new Dimension(800, 600));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int inputLayerX = 100;
        int hiddenLayerX = inputLayerX + 200;
        int outputLayerX = hiddenLayerX + 200;

        drawNeurons(g2d, inputLayerSize, inputLayerX, LAYER_GAP);

        drawNeurons(g2d, hiddenLayerSize, hiddenLayerX, LAYER_GAP);

        drawNeurons(g2d, outputLayerSize, outputLayerX, LAYER_GAP);
        drawConnections(g2d, inputLayerSize, 100, hiddenLayerSize, 300);

        drawConnections(g2d, hiddenLayerSize, 300, outputLayerSize, 500);
    }

    private void drawNeurons(Graphics2D g2d, int layerSize, int x, int layerGap) {
        int startY = (getHeight() - layerSize * SPACE_BETWEEN_NEURONS) / 2;
        int startX = x;

        for (int i = 0; i < layerSize; i++) {
            int centerY = startY + i * SPACE_BETWEEN_NEURONS + NEURON_SIZE / 2;
            g2d.setColor(NEURON_COLOR);
            g2d.drawOval(startX, centerY - NEURON_SIZE / 2, NEURON_SIZE, NEURON_SIZE);
        }
    }
    private void drawConnections(Graphics2D g2d, int fromLayerSize, int fromX, int toLayerSize, int toX) {
        int fromStartY = (getHeight() - fromLayerSize * SPACE_BETWEEN_NEURONS) / 2;
        int toStartY = (getHeight() - toLayerSize * SPACE_BETWEEN_NEURONS) / 2;

        for (int i = 0; i < fromLayerSize; i++) {
            int fromY = fromStartY + i * SPACE_BETWEEN_NEURONS + NEURON_SIZE / 2;

            for (int j = 0; j < toLayerSize; j++) {
                int toY = toStartY + j * SPACE_BETWEEN_NEURONS + NEURON_SIZE / 2;

                g2d.setColor(Color.BLACK);
                g2d.drawLine(fromX + NEURON_SIZE, fromY, toX, toY);
            }
        }
    }
}