package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.BankMain;
import org.example.exception.InvalidStatusException;
import org.example.model.Action;
import org.example.model.BankOperation;
import org.example.model.Currency;
import org.example.model.PaymentStatus;
import org.example.parser.Parser;
import org.example.parser.csvparser.CsvParser;
import org.example.parser.jsonparser.JsonParser;
import org.example.parser.xmlparser.XmlParser;
import org.example.parser.yamlparser.YamlParser;
import org.example.utils.ConsoleUtils;
import org.example.utils.Validator;
import org.example.view.BankView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BankController {
    BankView bankView = new BankView();
    ArrayList<BankOperation> bankOperations;
    Parser parser;
    File file;
    Map<String, Parser> stringParserMap=new HashMap<>();

    {
        stringParserMap.put("json",new JsonParser());
        stringParserMap.put("yaml",new YamlParser());
        stringParserMap.put("xml",new XmlParser());
        stringParserMap.put("csv",new CsvParser());
    }

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
        String appName = BankMain.class.getSimpleName();
        log.info("APP '{}' start success", appName.toUpperCase());
        do {
            String path = bankView.enterFilePath();
            String fileFormat = bankView.chooseFilePath(path);
            Validator.checkFileType(fileFormat);
            file = chooseFile(path);
            parser = createParser(fileFormat);
            bankOperations = fileToObjectList();
            makeActionWithFile();
            System.out.println("Do you want to continue work with app? Enter Yes or no ");
        } while (Validator.Action.validateAction().equals(Validator.Action.YES));
        log.info("APP '{}' finished success", appName.toUpperCase());
    }

    private void createTransaction() throws IOException {
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
        int newId = bankOperations.get(bankOperations.size() - 1).getId();
        BankOperation operation = new BankOperation(newId, sendCardNumber, getCardNumber, transferSum, currency, paymentPurpose);
        bankOperations.add(operation);
        writeToFile();
        log.info("new operation success create {}", operation);
    }

    private void updateTransaction() throws IOException {
        BankOperation operation;
        int id = chooseOrderId();
        if (!Validator.checkId(id, bankOperations)) {
            System.out.println("Not found. Do you want create a new transaction?");
            Validator.Action wantCreate = Validator.Action.validateAction();
            if (wantCreate.equals(Validator.Action.YES)) {
                createTransaction();
            }
            return;
        }
        operation = findOperationById(id);
        changeOperation(operation);
        writeToFile();
        log.info("operation update {}", operation);
    }

    private void deleteTransaction() throws IOException {
        BankOperation operation;
        int id;
        do {
            id = chooseOrderId();
        }
        while (!Validator.checkId(id, bankOperations));
        operation = findOperationById(id);
        bankOperations.remove(operation);
        writeToFile();
        log.info("operation remove {}", operation);
    }

    Parser createParser(String fileFormat) {
        return stringParserMap.get(fileFormat);
    }

    private ArrayList<BankOperation> fileToObjectList() throws IOException {
        return (ArrayList<BankOperation>) parser.toObjectList(file);
    }


    private void makeActionWithFile() {
        Action.chooseAction();
        Action action = Action.validateAction();
        try {
            switch (action) {
                case CREATE -> createTransaction();
                case UPDATE -> updateTransaction();
                case REMOVE -> deleteTransaction();
            }
            log.info("action {} was selected success", action);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeToFile() throws IOException {
        parser.toFile(bankOperations, file);
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

    public File chooseFile(String string) throws FileNotFoundException {
        file = new File(string);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        return file;
    }

    public void printTransaction() {
        bankOperations.forEach(System.out::println);
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
        for (BankOperation operation : bankOperations) {
            if (operation.getId() == id) {
                return operation;
            }
        }
        return null;
    }
}