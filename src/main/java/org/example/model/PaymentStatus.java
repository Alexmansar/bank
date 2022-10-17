package org.example.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.InvalidStatusException;
import org.example.utils.Validator;

import java.util.Arrays;
import java.util.Locale;

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

    public static PaymentStatus validateStatus() {
        PaymentStatus paymentStatus;
        try {
            paymentStatus = PaymentStatus.valueOf(Validator.validateInputText().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            System.out.println("Enter from the list : ");
            printPaymentStatuses();
            log.error(exception.getMessage());
            return validateStatus();
        }
        return paymentStatus;
    }

    public static PaymentStatus choosePaymentStatus() {
        PaymentStatus.printPaymentStatuses();
        return PaymentStatus.validateStatus();
    }

    public static PaymentStatus changePaymentStatus(BankOperation operation) {
        PaymentStatus status = choosePaymentStatus();
        checkNewStatus(operation.getPaymentStatus(), status);
        operation.setPaymentStatus(status);
        return status;
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
}