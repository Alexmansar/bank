package org.example.view;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.NotFileFormatException;
import org.example.utils.ConsoleUtils;
import org.example.utils.RegexValidator;
import org.example.utils.Validator;

import java.io.File;

@Slf4j
public class BankView {

    public String enterFilePath() {
        String filePath;
        System.out.println("Enter disk name");
        filePath = ConsoleUtils.getString();
        return filePath.replace('/', File.separatorChar).replace('\\', File.separatorChar);
    }

    public String chooseFilePath(String filePath) throws NotFileFormatException {
        Validator.checkDotsInPath(filePath);
        StringBuilder format = new StringBuilder();
        for (int i = filePath.length() - 1; i > 0; i--) {
            if (filePath.charAt(i) != '.') {
                format.append(filePath.charAt(i));
            } else {
                break;
            }
        }
        return format.reverse().toString();
    }

    public String getPaymentPurpose() {
        return ConsoleUtils.getString();
    }

    public String createCardNumber() {
        String cardNumber = ConsoleUtils.getString();
        boolean isValidCardNumber = RegexValidator.pattern(cardNumber);
        if (!isValidCardNumber) {
            System.out.println("Try again. Use next format : ****-****-****-**** or **** **** **** ****");
            log.info("Not valid number card {}", cardNumber);
            return createCardNumber();
        }
        return cardNumber;
    }

    public float getTransferSum() {
        float transferSum = ConsoleUtils.getFloat();
        if (transferSum == 0) {
            System.out.println(transferSum + " can't by zero. Try again");
            log.info("number {} - is zero", transferSum);
            return getTransferSum();
        }
        if (!Validator.isPositive(transferSum)) {
            System.out.println(transferSum + " is not positive. Try again");
            log.info("not positive {}", transferSum);
            return getTransferSum();
        }
        return transferSum;
    }

    public void printSetParameters() {
        System.out.println("Choose parameter would you like to change");
        System.out.println("""      
                1 - sender's card
                2 - recipient's card
                3 - sum
                4 - currency
                5 - purpose
                6 - status""");
    }
}