package org.example.view;

import lombok.extern.slf4j.Slf4j;
import org.example.utils.ConsoleUtils;
import org.example.utils.RegexValidator;
import org.example.utils.Validator;

import java.io.File;
@Slf4j
public class BankView {


    public String enterFilePath() {
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println("Enter disk name");
        stringBuilder.append(ConsoleUtils.getString()).append(":");
        System.out.println("enter folder");
        do {
            stringBuilder.append(File.separator).append(ConsoleUtils.getString());
            System.out.println("Do you want enter new folder? Enter Yes or no ");
        } while (Validator.Action.validateAction().equals(Validator.Action.YES));
        System.out.println("enter file name with it type");
        stringBuilder.append(File.separator).append(ConsoleUtils.getString());
        return stringBuilder.toString();
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