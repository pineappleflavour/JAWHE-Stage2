package com.airline.logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;

// Singleton class for logging messages with different log levels
public class Logger {
    private static Logger instance;
    private Queue<String> logArchive = new LinkedList<>();
    private LogLevel logLevel;

    // Private constructor to enforce singleton pattern
    private Logger() {
        logLevel = LogLevel.INFO;
    }

    // Method to get the singleton instance of Logger
    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    // Setter for log level
    public void setLogLevel(LogLevel level) {
        this.logLevel = level;
    }

    // Method to log a message with a specified log level
    public void log(LogLevel level, String message) {
        if (level.ordinal() >= logLevel.ordinal()) {
            String formattedMessage = String.format("[%s] [%s] %s", LocalDateTime.now(), level, message);
            System.out.println(formattedMessage);
            appendLog(formattedMessage);
        }
    }

    // Method to append log messages to the archive
    private synchronized void appendLog(String message){
        logArchive.add(message);
    }

    // Convenience methods for logging with different log levels
    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    public void warn(String message) {
        log(LogLevel.WARNING, message);
    }

    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    // Method to print all logs to a file
    public synchronized void printAllLogs(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            while (!logArchive.isEmpty()) {
                writer.write(logArchive.poll());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
