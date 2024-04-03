package com.airline.logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;

public class Logger {
    private static Logger instance;
    private Queue<String> logArchive = new LinkedList<>();
    private LogLevel logLevel;

    private Logger() {
        logLevel = LogLevel.INFO;
    }

    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void setLogLevel(LogLevel level) {
        this.logLevel = level;
    }

    public void log(LogLevel level, String message) {
        if (level.ordinal() >= logLevel.ordinal()) {
            String formattedMessage = String.format("[%s] [%s] %s", LocalDateTime.now(), level, message);
            System.out.println(formattedMessage);
            appendLog(formattedMessage);
        }
    }

    private synchronized void appendLog(String message){
        logArchive.add(message);
    }

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
