package com.github.nunoduarte;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class NetworkStatusChecker {

    private String url;

    public NetworkStatusChecker(String url) {
        this.url = url;
    }

    public boolean checkStatus(String protocol) {
        try {
            URL url = new URL(this.url);
            URLConnection conn = url.openConnection();

            if (protocol.equalsIgnoreCase("HTTPS")) {
                if (conn instanceof HttpURLConnection) {
                    HttpURLConnection httpConn = (HttpURLConnection) conn;
                    httpConn.setInstanceFollowRedirects(false);
                    httpConn.setRequestMethod("HEAD");
                    httpConn.connect();
                    int responseCode = httpConn.getResponseCode();
                    return (responseCode >= 200 && responseCode < 400);
                } else {
                    return false;
                }
            } else if (protocol.equalsIgnoreCase("HTTP")) {
                if (conn instanceof HttpURLConnection) {
                    HttpURLConnection httpConn = (HttpURLConnection) conn;
                    httpConn.setInstanceFollowRedirects(false);
                    httpConn.setRequestMethod("HEAD");
                    httpConn.setReadTimeout(5000);
                    httpConn.connect();
                    int responseCode = httpConn.getResponseCode();
                    return (responseCode >= 200 && responseCode < 400);
                } else {
                    return false;
                }
            } else if (protocol.equalsIgnoreCase("FTP")) {
                // Add FTP specific logic here
                // Replace the following return statement with your FTP implementation
                return false;
            } else if (protocol.equalsIgnoreCase("SMTP")) {
                // Add SMTP specific logic here
                // Replace the following return statement with your SMTP implementation
                return false;
            } else {
                throw new IllegalArgumentException("Unsupported protocol: " + protocol);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false; // Could not connect
        }
    }
}



