package com.github.nunoduarte;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class NetworkMonitor extends JFrame {

    private ControlPanel controlPanel;
    private StatusPanel statusPanel;
    private JTextArea textArea;
    private NetworkStatusChecker networkStatusChecker;
    private LoggerSetup loggerSetup;

    public NetworkMonitor() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);

        controlPanel = new ControlPanel(this);
        statusPanel = new StatusPanel();
        textArea = new JTextArea();
        networkStatusChecker = new NetworkStatusChecker(controlPanel.getUrlText());
        loggerSetup = new LoggerSetup();

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
                boolean status = networkStatusChecker.checkStatus(controlPanel.getProtocolText());
                SwingUtilities.invokeLater(() -> {
                    statusPanel.setStatus(status);
                    textArea.append(new SimpleDateFormat("HH:mm:ss").format(new Date()) + ": Network is " +
                            (status ? "up" : "down") + "\n");
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

