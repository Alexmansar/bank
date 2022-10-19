package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.BankMain;
import org.example.exception.InvalidStatusException;
import org.example.exception.NotFileFormatException;
import org.example.model.BankOperation;
import org.example.model.Currency;
import org.example.model.FileTypes;
import org.example.model.PaymentStatus;
import org.example.utils.*;
import org.example.utils.csvparser.CsvParser;
import org.example.utils.jsonparser.JsonParser;
import org.example.utils.xmlparser.XmlParser;
import org.example.utils.yamlparser.YamlParser;
import org.example.view.BankView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class BankController {
    BankView bankView = new BankView();
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
            String filePath = bankView.enterFilePath();
            FileTypes fileTypes = chooseFileType(filePath);
            File file = chooseFile(filePath);
            try {
                switch (fileTypes) {
                    case JSON -> {
                        BANK_OPERATIONS = (ArrayList<BankOperation>) JSON_PARSER.toObjectList(file);
                        makeActionWithFile(fileTypes, file);
                    }
                    case YAML -> {
                        BANK_OPERATIONS = (ArrayList<BankOperation>) YAML_PARSER.toObjectList(file);
                        makeActionWithFile(fileTypes, file);
                    }
                    case XML -> {
                        BANK_OPERATIONS = (ArrayList<BankOperation>) XML_PARSER.toObjectList(file);
                        makeActionWithFile(fileTypes, file);
                    }
                    case CSV -> {
                        BANK_OPERATIONS = (ArrayList<BankOperation>) CSV_PARSER.toObjectList(file);
                        makeActionWithFile(fileTypes, file);
                    }
                }
            } catch (FileNotFoundException e) {
                log.info(e.getMessage());
                System.out.println(e.getMessage());
            }
            System.out.println("Do you want to continue work with app? Enter Yes or no ");
        } while (Validator.Action.validateAction().equals(Validator.Action.YES));
        log.info("APP '{}' finished success", appName.toUpperCase());
    }

    private void createTransaction(FileTypes types, File file) throws IOException {
        System.out.println("Please, enter your card number");
        String sendCardNumber = bankView.createCardNumber();
        System.out.println("now, please, enter card number to transfer");
        String getCardNumber = bankView.createCardNumber();
        System.out.println("enter transfer sum ");
        float transferSum = bankView.getTransferSum();
        System.out.println("choose currency");
        Currency currency = Currency.chooseCurrency();
        System.out.println("keep a description of the payment");
        String paymentPurpose = bankView.getPaymentPurpose();
        int newId = BANK_OPERATIONS.size();
        BankOperation operation = new BankOperation(newId, sendCardNumber, getCardNumber, transferSum, currency, paymentPurpose);
        BANK_OPERATIONS.add(operation);
        writeToFile(types, file);
        log.info("new operation success create {}", operation);
    }

    private void updateTransaction(FileTypes types, File file) throws IOException {
        BankOperation operation;
        int id = chooseOrderId();
        if (!Validator.checkId(id, BANK_OPERATIONS)) {
            createTransaction(types, file);
            return;
        }
        operation = findOperationById(id);
        changeOperation(operation);
        writeToFile(types, file);
        log.info("operation update {}", operation);
    }

    private void deleteTransaction(FileTypes types, File file) throws IOException {
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
        writeToFile(types, file);
        log.info("operation remove {}", operation);
    }

    public void changeOperation(BankOperation operation) {
        do {
            bankView.printSetParameters();
            int choose = ConsoleUtils.getInt();
            try {
                switch (choose) {
                    case 1 -> operation.setSendCardNumber(bankView.createCardNumber());
                    case 2 -> operation.setGetCardNumber(bankView.createCardNumber());
                    case 3 -> operation.setSendSum(bankView.getTransferSum());
                    case 4 -> operation.setCurrency(Currency.chooseCurrency());
                    case 5 -> operation.setPaymentPurpose(bankView.getPaymentPurpose());
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

    private void makeActionWithFile(FileTypes fileTypes, File file) {
        Action.chooseAction();
        Action action = Action.validateAction();
        try {
            switch (action) {
                case CREATE -> createTransaction(fileTypes, file);
                case UPDATE -> updateTransaction(fileTypes, file);
                case REMOVE -> deleteTransaction(fileTypes, file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public FileTypes chooseFileType(String filePath) {
        FileTypes fileType;
        try {
            fileType = FileTypes.chooseFilePath(filePath);
        } catch (NotFileFormatException e) {
            filePath = bankView.enterFilePath();
            log.error(e.getMessage());
            System.out.println(e.getMessage());
            return chooseFileType(filePath);
        }
        return fileType;
    }

    public File chooseFile(String string) {
        return new File(string);
    }


    private void writeToFile(FileTypes types, File file) throws IOException {
        switch (types) {
            case JSON -> JSON_PARSER.toFile(BANK_OPERATIONS, file);
            case YAML -> YAML_PARSER.toFile(BANK_OPERATIONS, file);
            case XML -> XML_PARSER.toFile(BANK_OPERATIONS, file);
            case CSV -> CSV_PARSER.toFile(BANK_OPERATIONS, file);
        }
    }

    public void printTransaction() {
        BANK_OPERATIONS.forEach(System.out::println);
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
}