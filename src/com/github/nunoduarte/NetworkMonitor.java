package com.github.nunoduarte;

import javax.swing.*;


import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NetworkMonitor extends JFrame {
    private ControlPanel controlPanel;
    private StatusPanel statusPanel;
    private JTextArea textArea;
    private NetworkStatusChecker networkStatusChecker;
    private TrafficVisualizer trafficVisualizer;
    private List<Double> trafficData; // A list to store traffic data

    public NetworkMonitor() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);

        controlPanel = new ControlPanel(this);
        statusPanel = new StatusPanel();
        textArea = new JTextArea();
        networkStatusChecker = new NetworkStatusChecker(controlPanel.getUrlText());
        trafficData = new ArrayList<>(); // Initialize trafficData first
        trafficVisualizer = new TrafficVisualizer(this); // Then create TrafficVisualizer

        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        this.add(controlPanel, BorderLayout.SOUTH);
        this.add(statusPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(trafficVisualizer.getChartPanel(), BorderLayout.CENTER);
        this.setVisible(true);
    }

    public void startMonitoring() {
        new Thread(() -> {
            while (controlPanel.isRunning()) {
                long responseTime = networkStatusChecker.checkStatus();
                boolean status = responseTime >= 0; // We are assuming that a non-negative response time indicates a successful network request.

                trafficData.add((double) networkStatusChecker.getBytesReceived()); // Use actual network data here
                if (trafficData.size() > 100) { // keep last 100 data points
                    trafficData.remove(0);
                }

                SwingUtilities.invokeLater(() -> {
                    trafficVisualizer.updateData();
                    statusPanel.setStatus(status);
                    String statusText = status ? "up" : "down";
                    String message = new SimpleDateFormat("HH:mm:ss").format(new Date()) + ": Network is " + statusText + ", response time: " + responseTime + " ms\n";
                    textArea.append(message);
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Monitoring thread was interrupted");
                }
            }
        }).start();
    }

    public void stopMonitoring() {
        controlPanel.setRunning(false);
        statusPanel.setStatus(false);
    }

    public static void main(String[] args) {
        new NetworkMonitor();
    }

    // This method is used by TrafficVisualizer to access traffic data.
    public List<Double> getTrafficData() {
        return trafficData;
    }
}




