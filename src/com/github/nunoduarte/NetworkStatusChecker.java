package com.github.nunoduarte;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.atomic.AtomicLong;


public class NetworkStatusChecker {
    private String url;
    private AtomicLong bytesReceived = new AtomicLong(0);

    public NetworkStatusChecker(String url) {
        this.url = url;
    }

    public long checkStatus() {
        long startTime = System.currentTimeMillis();
        HttpURLConnection conn = null;
        try {
            URL url = new URL(this.url);
            conn = (HttpURLConnection) url.openConnection();

            bytesReceived.set(0);

            InputStream in = conn.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                bytesReceived.addAndGet(bytesRead);
            }

            long responseTime = System.currentTimeMillis() - startTime;

            bytesReceived.set(0);

            return responseTime;
        } catch (IOException e) {
            return -1;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public long getBytesReceived() {
        return bytesReceived.get();
    }

    public void setUrl(String url) {
        this.url = url;
    }
}







