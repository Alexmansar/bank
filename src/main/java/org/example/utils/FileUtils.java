package org.example.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
public class FileUtils {

    public static void clearFile(File file) {
        try {
            new FileOutputStream(file).close();
        } catch (IOException e) {
            log.info("ERROR: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void clearFile(String fileName) {
        try {
            new FileOutputStream(fileName).close();
        } catch (IOException e) {
            log.info("ERROR: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}