package com.github.nunoduarte;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class NetworkMonitor {
    private JTextArea textArea;

    public NetworkMonitor() {
        // Create a new JFrame instance
        JFrame frame = new JFrame("Network Monitor");

        // The application will exit when the frame is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the size of the frame
        frame.setSize(500, 500);

        // Create a JTextArea to display the network status
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Make the frame visible
        frame.setVisible(true);

        // Start the network monitoring
        startMonitoring();
    }

    public void startMonitoring() {
        new Thread(() -> {
            while (true) {
                // Check the network status every second
                String status = checkNetworkStatus() ? "Network is up" : "Network is down";
                textArea.append(status + "\n");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }).start();
    }

    public boolean checkNetworkStatus() {
        // You can replace this with a real check
        try {
            final URL url = new URL("http://www.google.com");
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


