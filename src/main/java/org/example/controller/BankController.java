package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.BankMain;
import org.example.exception.InvalidStatusException;
import org.example.model.BankOperation;
import org.example.model.Currency;
import org.example.model.FileTypes;
import org.example.model.PaymentStatus;
import org.example.utils.*;
import org.example.utils.csvparser.CsvParser;
import org.example.utils.jsonparser.JsonParser;
import org.example.utils.xmlparser.XmlParser;
import org.example.utils.yamlparser.YamlParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class BankController {

    public static final String LOG_FILE = "logger.log";
    Parser CSV_PARSER = new CsvParser();
    Parser JSON_PARSER = new JsonParser();
    Parser XML_PARSER = new XmlParser();
    Parser YAML_PARSER = new YamlParser();
    ArrayList<BankOperation> BANK_OPERATIONS;

    /*  {
          bankOperations = new ArrayList<>();
          bankOperations.add(new BankOperation("1111-2222-3333-4444", "1122-3344-5566-7788",
                  123f, Currency.UAH, " for rent"));
          bankOperations.add(new BankOperation("1234-4567-9874-1235", "2211-4433-6655-8877",
                  118.59f, Currency.USD, " for present"));
          bankOperations.add(new BankOperation("1111-2222-3333-4444", "1122-3344-5566-7788",
                  12000f, Currency.EUR, " for auto"));
      }
  */
    public void run() throws IOException {
        FileUtils.clearFile(LOG_FILE);
        String appName = BankMain.class.getSimpleName();
        log.info("APP '{}' start success", appName.toUpperCase());
        do {
            System.out.println("Please, enter format file");
            FileTypes.printFileTypes();
            FileTypes fileTypes = FileTypes.validateFileTypes();
            switch (fileTypes) {
                case JSON -> {
                    BANK_OPERATIONS = (ArrayList<BankOperation>) JSON_PARSER.toObjectList();
                    chooseAction(fileTypes);
                }
                case YAML -> {
                    BANK_OPERATIONS = (ArrayList<BankOperation>) YAML_PARSER.toObjectList();
                    chooseAction(fileTypes);
                }
                case XML -> {
                    BANK_OPERATIONS = (ArrayList<BankOperation>) XML_PARSER.toObjectList();
                    chooseAction(fileTypes);
                }
                case CSV -> {
                    BANK_OPERATIONS = (ArrayList<BankOperation>) CSV_PARSER.toObjectList();
                    chooseAction(fileTypes);
                }
            }
            System.out.println("Do you want to continue work with app? Enter Yes or no ");
        } while (Validator.Action.validateAction().equals(Validator.Action.YES));
        log.info("APP '{}' finished success", appName.toUpperCase());
    }

    private void chooseAction(FileTypes fileTypes) throws IOException {
        Action.chooseAction();
        Action action = Action.validateAction();
        switch (action) {
            case CREATE -> createTransaction(fileTypes);
            case UPDATE -> updateTransaction(fileTypes);
            case REMOVE -> deleteTransaction(fileTypes);
        }
    }

    public void updateTransaction(FileTypes types) throws IOException {
        BankOperation operation;
        int id = chooseOrderId();
        if (!Validator.checkId(id, BANK_OPERATIONS)) {
            createTransaction(types);
            return;
        }
        operation = findOperationById(id);
        changeOperation(operation);
        writeToFile(types);
        log.info("operation update {}", operation);
    }

    private void writeToFile(FileTypes types) throws IOException {
        switch (types) {
            case JSON -> JSON_PARSER.toFile(BANK_OPERATIONS);
            case YAML -> YAML_PARSER.toFile(BANK_OPERATIONS);
            case XML -> XML_PARSER.toFile(BANK_OPERATIONS);
            case CSV -> CSV_PARSER.toFile(BANK_OPERATIONS);
        }
    }

    public void deleteTransaction(FileTypes types) throws IOException {
        BankOperation operation;
        int id;
        do {
            id = chooseOrderId();
        }
        while (!Validator.checkId(id, BANK_OPERATIONS));
        operation = findOperationById(id);
        BANK_OPERATIONS.remove(id - 1);
        AtomicInteger finalI = new AtomicInteger();
        BANK_OPERATIONS.forEach(a -> a.setId(finalI.incrementAndGet()));
        writeToFile(types);
        log.info("operation remove {}", operation);
    }

    public void printTransaction() {
        BANK_OPERATIONS.forEach(System.out::println);
    }

    public void createTransaction(FileTypes types) throws IOException {
        System.out.println("Please, enter your card number");
        String sendCardNumber = createCardNumber();
        System.out.println("now, please, enter card number to transfer");
        String getCardNumber = createCardNumber();
        System.out.println("enter transfer sum ");
        float transferSum = getTransferSum();
        System.out.println("choose currency");
        Currency currency = Currency.chooseCurrency();
        System.out.println("keep a description of the payment");
        String paymentPurpose = getPaymentPurpose();
        int newId = BANK_OPERATIONS.size();
        BankOperation operation = new BankOperation(newId, sendCardNumber, getCardNumber, transferSum, currency, paymentPurpose);
        BANK_OPERATIONS.add(operation);
        writeToFile(types);
        log.info("new operation success create {}", operation);
    }

    private String getPaymentPurpose() {
        return ConsoleUtils.getString();
    }

    private String createCardNumber() {
        String cardNumber = ConsoleUtils.getString();
        boolean isValidCardNumber = RegexValidator.pattern(cardNumber);
        if (!isValidCardNumber) {
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

    public int chooseOrderId() {
        System.out.println("Enter number transaction witch you want to choose");
        printTransaction();
        int id = ConsoleUtils.getInt();
        if (id >= 1) {
            log.info("not valid id {}", id);
            return id;
        }
        return chooseOrderId();
    }

    public BankOperation findOperationById(int id) {
        for (BankOperation operation : BANK_OPERATIONS) {
            if (operation.getId() == id) {
                return operation;
            }
        }
        return null;
    }

    public void changeOperation(BankOperation operation) {
        do {
            printSetParameters();
            int choose = ConsoleUtils.getInt();
            try {
                switch (choose) {
                    case 1 -> operation.setSendCardNumber(createCardNumber());
                    case 2 -> operation.setGetCardNumber(createCardNumber());
                    case 3 -> operation.setSendSum(getTransferSum());
                    case 4 -> operation.setCurrency(Currency.chooseCurrency());
                    case 5 -> operation.setPaymentPurpose(getPaymentPurpose());
                    case 6 -> operation.setPaymentStatus(PaymentStatus.changePaymentStatus(operation));
                }
            } catch (InvalidStatusException e) {
                log.error(e.getMessage());
                System.out.println(e.getMessage());
                changeOperation(operation);
            }
            System.out.println("Do you want to continue changes parametrises? Enter Yes or no ");
        } while (Validator.Action.validateAction().equals(Validator.Action.YES));
        operation.setUpdateTime(LocalDateTime.now());
        log.info("operation {} was success update.", operation);
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