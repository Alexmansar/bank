package org.example.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.utils.Validator;

import java.util.Arrays;

@Slf4j
@Getter
public enum PaymentStatus {

    IN_PROCESSING(1), PENDING(2), SUCCESSFUL(3), DECLINED(4);
    private final int priority;

    PaymentStatus(int priority) {
        this.priority = priority;
    }


    public static void printPaymentStatuses() {
        Arrays.stream(PaymentStatus.values()).forEach(System.out::println);
    }



    public static PaymentStatus choosePaymentStatus() {
        PaymentStatus.printPaymentStatuses();
        return Validator.validateStatus();
    }

    public static PaymentStatus changePaymentStatus(BankOperation operation) {
        PaymentStatus status = choosePaymentStatus();
        Validator.checkNewStatus(operation.getPaymentStatus(), status);
        operation.setPaymentStatus(status);
        return status;
    }
}