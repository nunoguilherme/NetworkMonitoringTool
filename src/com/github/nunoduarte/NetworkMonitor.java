package com.github.nunoduarte;

import javax.swing.*;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class NetworkMonitor extends JFrame {

    private ControlPanel controlPanel;
    private StatusPanel statusPanel;
    private JTextArea textArea;
    private NetworkStatusChecker networkStatusChecker;
    private LoggerSetup loggerSetup;
    private TrafficVisualizer trafficVisualizer;

    public NetworkMonitor() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);

        controlPanel = new ControlPanel(this);
        statusPanel = new StatusPanel();
        textArea = new JTextArea();
        networkStatusChecker = new NetworkStatusChecker(controlPanel.getUrlText());
        loggerSetup = new LoggerSetup();
        trafficVisualizer = new TrafficVisualizer(this);

        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        this.add(controlPanel, BorderLayout.SOUTH);
        this.add(statusPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);

        this.setVisible(true);
    }

    public void startMonitoring() {
    new Thread(() -> {
        while (controlPanel.isRunning()) {
            long responseTime = networkStatusChecker.checkStatus(controlPanel.getProtocolText());
            boolean status = responseTime >= 0; // We are assuming that a non-negative response time indicates a successful network request.
            SwingUtilities.invokeLater(() -> {
                statusPanel.setStatus(status);
                String statusText = status ? "up" : "down";
                String message = new SimpleDateFormat("HH:mm:ss").format(new Date()) + ": Network is " + statusText + ", response time: " + responseTime + " ms\n";
                textArea.append(message);
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }).start();
}


    public void stopMonitoring() {
        controlPanel.setRunning(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NetworkMonitor::new);
    }
}



