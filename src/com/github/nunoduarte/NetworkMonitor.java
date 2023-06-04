package com.github.nunoduarte;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class NetworkMonitor {
    private JTextArea textArea;
    private JTextField urlField;
    private JPanel statusPanel;

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
        frame.add(statusPanel, BorderLayout.NORTH);

        // Create a JTextArea to display the network status
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Create a JPanel for the control panel
        JPanel controlPanel = new JPanel();
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> startMonitoring());
        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> stopMonitoring());
        urlField = new JTextField("http://www.google.com", 20);
        controlPanel.add(urlField);
        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        frame.add(controlPanel, BorderLayout.SOUTH);

        // Make the frame visible
        frame.setVisible(true);
    }

    private volatile boolean running = true;

    public void startMonitoring() {
        new Thread(() -> {
            while (running) {
                // Check the network status every second
                boolean status = checkNetworkStatus();
                SwingUtilities.invokeLater(() -> {
                    statusPanel.setBackground(status ? Color.GREEN : Color.RED);
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
        running = false;
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
            return false;  // Could not connect
        }
    }

    public static void main(String[] args) {
        // Start the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(NetworkMonitor::new);
    }
}


