package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.InvalidStatusException;
import org.example.exception.NotFileFormatException;
import org.example.model.BankOperation;
import org.example.model.FileType;
import org.example.model.PaymentStatus;

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


    public static void checkDotsInPath(String filePath) throws NotFileFormatException{
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

    public static void checkFileType(String path) throws NotFileFormatException {
        if (!Arrays.toString(FileType.values()).contains(path.toUpperCase())){
            throw new NotFileFormatException(path + " Not valid format. Use only :" + Arrays.toString(FileType.values()));
        }
    }

    public static void checkNewStatus(PaymentStatus oldStatus, PaymentStatus newStatus) {
        String message = "Invalid status. The new status cannot be the previous status or the same." +
                "old status: " + oldStatus.toString().toUpperCase() + " new status: " +
                newStatus.toString().toUpperCase();
        if (oldStatus == PaymentStatus.DECLINED && newStatus == oldStatus) {
            return;
        }
        if (oldStatus.getPriority() >= newStatus.getPriority()) {
            log.info(message);
            throw new InvalidStatusException(message);
        }
    }

    public static PaymentStatus validateStatus() {
        PaymentStatus paymentStatus;
        try {
            paymentStatus = PaymentStatus.valueOf(Validator.validateInputText().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            System.out.println("Enter from the list : ");
            PaymentStatus.printPaymentStatuses();
            log.error(exception.getMessage());
            return validateStatus();
        }
        return paymentStatus;
    }
}