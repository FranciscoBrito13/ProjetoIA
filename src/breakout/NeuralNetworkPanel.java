package breakout;

import utils.Commons;
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

    private int inputLayerSize;
    private int hiddenLayerSize;
    private int outputLayerSize;

    private NeuralNetwork nn;
    private Timer timer;

    public NeuralNetworkPanel(NeuralNetwork nn) {
        this.inputLayerSize = nn.getInputLayerDim();
        this.hiddenLayerSize = nn.getHiddenLayerDim();
        this.outputLayerSize = nn.getOutputLayerDim();
        this.nn = nn;

        initView();

    }

    private void initView() {
        setFocusable(true);
        setPreferredSize(new Dimension(600, 600));
        networkInit();
    }

    private void networkInit() {
        timer = new Timer(Commons.PERIOD, new NeuralNetworkPanel.GameCycle());
        timer.start();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int inputLayerX = 100;
        int hiddenLayerX = inputLayerX + 200;
        int outputLayerX = hiddenLayerX + 200;

        drawNeurons(g2d, inputLayerSize, inputLayerX, LAYER_GAP, nn.getInputValues());

        drawNeurons(g2d, hiddenLayerSize, hiddenLayerX, LAYER_GAP, nn.getHiddenValues());

        drawNeurons(g2d, outputLayerSize, outputLayerX, LAYER_GAP, nn.getOutputValues());

        drawConnections(g2d, inputLayerSize, 100, hiddenLayerSize, 300);

        drawConnections(g2d, hiddenLayerSize, 300, outputLayerSize, 500);

    }

     private void drawNeurons(Graphics2D g2d, int layerSize, int x, int layerGap, double[] values) {
        int startY = (getHeight() - layerSize * SPACE_BETWEEN_NEURONS) / 2;
        int startX = x;

        double minActivation = Double.MAX_VALUE;
        double maxActivation = Double.MIN_VALUE;
        for (double value : values) {
            minActivation = Math.min(minActivation, value);
            maxActivation = Math.max(maxActivation, value);
        }

        Color minColor = Color.CYAN; // Cor para os valores mínimos
        Color maxColor = Color.BLUE; // Cor para os valores máximos


        for (int i = 0; i < layerSize; i++) {
            int centerY = startY + i * SPACE_BETWEEN_NEURONS + NEURON_SIZE / 2;
            double normalizedValue = (values[i] - minActivation) / (maxActivation - minActivation);
            Color color = interpolateColor(minColor, maxColor, normalizedValue);
            g2d.setColor(color);
            g2d.fillOval(startX, centerY - NEURON_SIZE / 2, NEURON_SIZE, NEURON_SIZE);
        }
    }

    private Color interpolateColor(Color c1, Color c2, double fraction) {
        double r = c1.getRed() + fraction * (c2.getRed() - c1.getRed());
        double g = c1.getGreen() + fraction * (c2.getGreen() - c1.getGreen());
        double b = c1.getBlue() + fraction * (c2.getBlue() - c1.getBlue());
        return new Color((int) r, (int) g, (int) b);
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

                g2d.drawString("Azul escuro representa um neurónio com maior ativação", 10 ,10);
                g2d.drawString("Azul claro representa um neurónio com menor ativação", 10 ,30);
            }
        }

    }

    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }


    }

    private void doGameCycle() {
        repaint();
    }
}