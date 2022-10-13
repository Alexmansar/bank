package org.example.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;

@Slf4j
public class FileUtils {
    public static final String LOG_FILE = "LOG.txt";

    public static void writeFile(String text, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write("\n" + text);
        } catch (IOException e) {
            writeFile("ERROR: " + e.getMessage(), LOG_FILE);
        }
    }

    public static void clearFile(String string) {
        try {
            new FileOutputStream(string).close();
        } catch (IOException e) {
            writeFile("ERROR: " + e.getMessage(), LOG_FILE);
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<?> readFile(String fileName, ArrayList<?> list) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            list = (ArrayList<?>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.info("ERROR: " + e.getMessage());
            System.out.println("ERROR: " + e.getMessage());
        }
        return list;
    }

    public static void readFile(String fileName) {
        try (BufferedReader inputStream = new BufferedReader(new FileReader(fileName))) {
            inputStream.lines().forEach(System.out::println);
        } catch (IOException e) {
            log.error("ERROR: " + e.getMessage());
        }
    }
    public static void saveFail(ArrayList<?> list, String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(list);
        } catch (IOException e) {
            log.info("ERROR: " + e.getMessage());
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}