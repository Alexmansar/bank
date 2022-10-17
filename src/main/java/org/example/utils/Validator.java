package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.example.model.BankOperation;

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
}