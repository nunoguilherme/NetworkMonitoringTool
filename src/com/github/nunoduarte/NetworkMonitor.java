package com.github.nunoduarte;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import java.awt.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class NetworkMonitor {

    private JTextArea textArea;
    private JTextField urlField;
    private JTextField intervalField;
    private JPanel statusPanel;
    private Logger logger;
    private int checkInterval = 1000;
    private JComboBox<String> protocolBox;
    private JLabel statusLabel;
    private JTabbedPane tabbedPane;

    public NetworkMonitor() {
        // Create a new JFrame instance
        JFrame frame = new JFrame("Network Monitor");

        // The application will exit when the frame is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the size of the frame
        frame.setSize(500, 500);

        // Create a JPanel for the status panel
        statusPanel = new JPanel();
        statusPanel.setBackground(Color.RED);
        statusLabel = new JLabel("Disconnected");
        statusPanel.add(statusLabel);
        frame.add(statusPanel, BorderLayout.NORTH);

        // Create a JTextArea to display the network status
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Create a JPanel for the control panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> startMonitoring());
        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> stopMonitoring());
        urlField = new JTextField("http://www.google.com", 20);
        intervalField = new JTextField("1000", 5);
        protocolBox = new JComboBox<>(new String[]{"HTTP", "HTTPS", "FTP"});
        controlPanel.add(new JLabel("URL:"));
        controlPanel.add(urlField);
        controlPanel.add(new JLabel("Interval (ms):"));
        controlPanel.add(intervalField);
        controlPanel.add(new JLabel("Protocol:"));
        controlPanel.add(protocolBox);
        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        frame.add(controlPanel, BorderLayout.SOUTH);

        // Set up logging
        try {
            logger = Logger.getLogger("NetworkMonitorLog");
            FileHandler fh = new FileHandler("NetworkMonitor.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Make the frame visible
        frame.setVisible(true);
    }

    private volatile boolean running = true;

    public void startMonitoring() {
        running = true;
        new Thread(() -> {
            while (running) {
                // Check the network status every second
                boolean status = checkNetworkStatus();
                SwingUtilities.invokeLater(() -> {
                    statusPanel.setBackground(status ? Color.GREEN : Color.RED);
                    statusLabel.setText(status ? "Connected" : "Disconnected");
                    textArea.append(new SimpleDateFormat("HH:mm:ss").format(new Date()) + ": Network is " + (status ? "up" : "down") + "\n");
                    logger.info("Network is " + (status ? "up" : "down"));
                });
                try {
                    Thread.sleep(checkInterval);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }).start();
    }

    public void stopMonitoring() {
    running = false;
    SwingUtilities.invokeLater(() -> {
        statusPanel.setBackground(Color.RED);
        statusLabel.setText("Disconnected");
    });
}

    public boolean checkNetworkStatus() {
        // You can replace this with a real check
        try {
            final URL url = new URL(urlField.getText());
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false; // Could not connect
        }
    }

    public static void main(String[] args) {
        // Start the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(NetworkMonitor::new);
    }
}
