package org.example.model;

import lombok.extern.slf4j.Slf4j;
import org.example.utils.Validator;

import java.util.Arrays;
import java.util.Locale;

@Slf4j
public enum Action {
    CREATE, UPDATE, REMOVE;

    public static void printAction() {
        Arrays.stream(Action.values()).forEach(System.out::println);
    }

    public static Action validateAction() {
        Action action;
        try {
            action = Action.valueOf(Validator.validateInputText().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            System.out.println("Enter from the list : ");
            printAction();
            log.error("not valid value {}", exception.getMessage());
            return validateAction();
        }
        return action;
    }

    public static void chooseAction() {
        System.out.println("Please, enter action:");
        printAction();
    }
}