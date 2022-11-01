package org.example.parser.csvparser;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.NotValidSizeException;
import org.example.model.BankOperation;
import org.example.model.Currency;
import org.example.model.PaymentStatus;
import org.example.utils.FileUtils;
import org.example.parser.Parser;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CsvParser implements Parser {

    @Override
    public void toFile(List<BankOperation> operation, File file) {
        FileUtils.clearFile(file);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            for (BankOperation bankOperations : operation) {
                writer.write(scvToString(bankOperations) + "\n");
            }
        } catch (IOException e) {
            log.info("ERROR: " + e.getMessage());
        }
    }

    @Override
    public List<BankOperation> toObjectList(File file) {
        List<BankOperation> bankOperations = new ArrayList<>();
        String stringOrder;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                stringOrder = reader.readLine();
                BankOperation operation = createOperationFromString(stringOrder);
                bankOperations.add(operation);
            }
        } catch (IOException | NotValidSizeException e) {
            log.info("ERROR: " + e.getMessage());
        }
        return bankOperations;
    }

    public static BankOperation createOperationFromString(String orderString) {
        String[] strings = orderString.split(",");
        if (strings.length != 9) {
            log.info("array is empty. or more that array length");
            throw new NotValidSizeException();
        }
        int id = Integer.parseInt((strings[0]));
        String sendCardNumber = String.valueOf(strings[1]);
        String getCardNumber = String.valueOf(strings[2]);
        Float sendSum = Float.valueOf(strings[3]);
        Currency currency = Currency.valueOf(strings[4]);
        LocalDateTime sendDateTime = LocalDateTime.parse(strings[5]);
        LocalDateTime updateTime = LocalDateTime.parse(strings[6]);
        String paymentPurpose = String.valueOf(strings[7]);
        PaymentStatus paymentStatus = PaymentStatus.valueOf(strings[8]);
        return new BankOperation(id, sendCardNumber, getCardNumber, sendSum, currency, sendDateTime, updateTime, paymentPurpose, paymentStatus);
    }

    public String scvToString(BankOperation operation) {
        return operation.getId() + "," + operation.getSendCardNumber() + "," + operation.getGetCardNumber() + ","
                + operation.getSendSum() + "," + operation.getCurrency() + "," + operation.getSendDateTime() + ","
                + operation.getUpdateTime() + "," + operation.getPaymentPurpose() + "," + operation.getPaymentStatus();
    }
}