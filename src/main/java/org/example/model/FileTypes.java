package org.example.model;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.NotFileFormatException;

import java.util.Arrays;

@Slf4j
public enum FileTypes {
    JSON, YAML, XML, CSV;

    public static FileTypes chooseFilePath(String filePath) {
        FileTypes fileTypes;
        String message;
        int count = 0;
        for (int i = filePath.length() - 1; i > 0; i--) {
            if (filePath.charAt(i) == '.') {
                count++;
            }
        }
        if (count == 0) {
            message = "Not Valid Format. You don't use '.' ";
            log.error(message);
            throw new NotFileFormatException(message);
        }
        StringBuilder format = new StringBuilder();
        for (int i = filePath.length() - 1; i > 0; i--) {
            if (filePath.charAt(i) != '.') {
                format.append(filePath.charAt(i));
            } else {
                break;
            }
        }
        switch (format.reverse().toString()) {
            case "json" -> fileTypes = JSON;
            case "xml" -> fileTypes = XML;
            case "csv" -> fileTypes = CSV;
            case "yaml" -> fileTypes = YAML;
            default -> {
                message = "Not Valid Format. Please, use ones: " + Arrays.toString(values());
                log.error(message);
                throw new NotFileFormatException(message);
            }
        }
        return fileTypes;
    }
}