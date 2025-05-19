package com.keras1n.core.utils;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerConfig {
    public static void configureLogging(boolean enabled) {
        Logger rootLogger = Logger.getLogger("");
        for (var handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }

        if (enabled) {
            ConsoleHandler handler = new ConsoleHandler();
            handler.setLevel(Level.INFO); //Level.All
            rootLogger.addHandler(handler);
            rootLogger.setLevel(Level.INFO);
        } else {
            rootLogger.setLevel(Level.OFF);
        }
    }
}
