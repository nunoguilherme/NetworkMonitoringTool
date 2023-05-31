package com.github.nunoduarte;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkMonitor {

    private static final int PORT = 8000;

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Network monitor started. Listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("Network monitor encountered an error: " + e.getMessage());
        }
    }

    private void handleClient(Socket clientSocket) {
        System.out.println("Accepted connection from " + clientSocket.getInetAddress());
        // TODO: Implement logic for handling the client
    }
}

