package com.github.nunoduarte;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerSetup {

    private Logger logger;

    public LoggerSetup() {
        logger = Logger.getLogger(NetworkMonitor.class.getName());
        FileHandler fh;
        try {
            fh = new FileHandler("networkMonitor.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public Logger getLogger() {
        return this.logger;
    }
}

