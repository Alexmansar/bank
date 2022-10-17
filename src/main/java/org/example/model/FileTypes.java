package org.example.model;

import lombok.extern.slf4j.Slf4j;
import org.example.utils.Validator;

import java.util.Arrays;
import java.util.Locale;
@Slf4j
public enum FileTypes {
    XML, JSON, YAML, CSV;

    public static void printFileTypes() {
        Arrays.stream(FileTypes.values()).forEach(System.out::println);
    }


    public static FileTypes validateFileTypes() {
        FileTypes fileTypes;
        try {
            fileTypes = FileTypes.valueOf(Validator.validateInputText().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            System.out.println("Enter from the list : ");
            printFileTypes();
            log.error(exception.getMessage());
            return validateFileTypes();
        }
        return fileTypes;
    }
}