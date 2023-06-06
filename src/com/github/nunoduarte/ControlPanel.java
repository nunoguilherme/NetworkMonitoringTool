package com.github.nunoduarte;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


    public class ControlPanel extends JPanel {

        private JTextField urlField;
        private JComboBox<String> protocolBox;
        private JCheckBox smtpCheckBox;
        private JButton startButton;
        private JButton stopButton;
        private boolean running;

        private NetworkMonitor monitor;  // Add this

        public ControlPanel(NetworkMonitor monitor) {
            this.monitor = monitor;  // Add this

            // Initialize components
            urlField = new JTextField("http://www.google.com", 20);
            protocolBox = new JComboBox<>(new String[]{"HTTP", "HTTPS", "FTP", "SMTP"});
            smtpCheckBox = new JCheckBox("SMTP");
            startButton = new JButton("Start");
            stopButton = new JButton("Stop");
            running = false;

            // Remove trafficVisualizer initialization

            // Add components to the panel
            setLayout(new FlowLayout());
            add(new JLabel("URL:"));
            add(urlField);
            add(new JLabel("Protocol:"));
            add(protocolBox);
            add(startButton);
            add(stopButton);

            // Add action listeners
            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    startButton.setEnabled(false);
                    stopButton.setEnabled(true);
                    monitor.startMonitoring();
                    running = true;
                }
            });

            stopButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    startButton.setEnabled(true);
                    stopButton.setEnabled(false);
                    monitor.stopMonitoring();
                    running = false;
                }
            });
        }

        public boolean isRunning() {
            return running;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        public String getUrlText() {
            return urlField.getText();
        }

        public String getSelectedProtocol() {
            return (String) protocolBox.getSelectedItem();
        }

        public boolean isSmtpSelected() {
            return smtpCheckBox.isSelected();
        }

        public String getProtocolText() {
            return (String) protocolBox.getSelectedItem();
        }
    }




