package util;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogger implements AutoCloseable {

    //Attributes
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final String filePath;
    private BufferedWriter writer;

    //Methods
    public FileLogger(String filePath) {
        this.filePath = filePath;
        try {
            writer = new BufferedWriter(new FileWriter(filePath, true));
        } catch (IOException e) {
            System.out.println("[FileLogger] Cannot open log file: " + e.getMessage());
            writer = null;
        }
    }

    public void log(String message) {
        if (writer == null) return;
        try {
            writer.write("[" + LocalDateTime.now().format(FMT) + "] " + message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.out.println("[FileLogger] Write error: " + e.getMessage());
        }
    }

    public void printLog() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            System.out.println("── Log: " + filePath + " ──");
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("[FileLogger] File not found: " + filePath);
        } catch (IOException e) {
            System.out.println("[FileLogger] Read error: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        if (writer != null) {
            try { writer.close(); }
            catch (IOException e) {
                System.out.println("[FileLogger] Close error: " + e.getMessage());
            }
        }
    }
}
