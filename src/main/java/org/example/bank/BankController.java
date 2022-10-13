package org.example.bank;

import lombok.extern.slf4j.Slf4j;
import org.example.BankMain;
import org.example.utils.ConsoleUtils;
import org.example.utils.FileUtils;
import org.example.utils.RegexValidator;
import org.example.utils.Validator;

import java.util.ArrayList;

@Slf4j
public class BankController {

    public static final String LOG_FILE = "logger.log";

    ArrayList<BankOperation> bankOperations;

    {
        bankOperations = new ArrayList<>();
        bankOperations.add(new BankOperation("1111-2222-3333-4444", "1122-3344-5566-7788",
                123f, Currency.UAH, " for rent"));
        bankOperations.add(new BankOperation("1234-4567-9874-1235", "2211-4433-6655-8877",
                118.59f, Currency.USD, " for present"));
        bankOperations.add(new BankOperation("1111-2222-3333-4444", "1122-3344-5566-7788",
                12000f, Currency.EUR, " for auto"));
    }

    public void run() {
        FileUtils.clearFile(LOG_FILE);
        String appName = BankMain.class.getSimpleName();
        log.info("APP '{}' start success", appName.toUpperCase());
        log.info("APP '{}' finished success", appName.toUpperCase());
        FileUtils.readFile(LOG_FILE);
    }

    public void printTransaction() {
        bankOperations.forEach(System.out::println);
    }

    public BankOperation createNewOperation() {
        System.out.println("Please, enter your card number");
        String sendCardNumber = createCardNumber();
        System.out.println("now, please, enter card number to transfer");
        String getCardNumber = createCardNumber();
        System.out.println("enter transfer sum ");
        float transferSum = getTransferSum();
        System.out.println("choose currency");
        Currency currency = chooseCurrency();
        System.out.println("keep a description of the payment");
        String paymentPurpose = ConsoleUtils.getString();
        BankOperation operation = new BankOperation(sendCardNumber, getCardNumber, transferSum, currency, paymentPurpose);
        bankOperations.add(operation);
        log.info("new operation success create {}", operation);
        return operation;
    }

    private String createCardNumber() {
        String cardNumber = ConsoleUtils.getString();
        boolean isValidEmail = RegexValidator.pattern(cardNumber);
        if (!isValidEmail) {
            System.out.println("Try again. Use next format : ****-****-****-**** or **** **** **** ****");
            log.info("Not valid number card {}", cardNumber);
            return createCardNumber();
        }
        return cardNumber;
    }

    private float getTransferSum() {
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

    private Currency chooseCurrency() {
        Currency.printCurrencies();
        return Currency.validateCurrency();
    }
}