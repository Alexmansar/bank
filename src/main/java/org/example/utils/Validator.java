package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.NotFileFormatException;
import org.example.model.BankOperation;
import org.example.model.FileType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

@Slf4j
public class Validator {
    public static String validateInputText() {
        String str = ConsoleUtils.getString();
        if (str.trim().isEmpty()) {
            log.error("empty string");
            System.out.println("You have not entered any value. Please, try again ");
            return validateInputText();
        }
        return str;
    }

    public static boolean checkId(int id, ArrayList<BankOperation> bankOperations) {
        for (BankOperation operation : bankOperations) {
            if (operation.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPositive(float number) {
        return number > ((number + 1) % number);
    }

    public enum Action {
        YES, NO;

        public static void printAction() {
            Arrays.stream(Action.values()).forEach(System.out::println);
        }

        public static Action validateAction() {
            Action action;
            try {
                action = Action.valueOf(validateInputText().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException exception) {
                System.out.println("Enter from the list : ");
                printAction();
                log.error("not valid value {}", exception.getMessage());
                return validateAction();
            }
            return action;
        }
    }


    public static void checkDotsInPath(String filePath) {
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
    }

    public static void checkFileType(String path) {
        if (!Arrays.toString(FileType.values()).contains(path.toUpperCase())){
            throw new NotFileFormatException(path + " Not valid format. Use only :" + Arrays.toString(FileType.values()));

        }
    }
}