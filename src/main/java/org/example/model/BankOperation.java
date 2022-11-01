package org.example.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BankOperation {
    int id;
    String sendCardNumber;
    String getCardNumber;
    Float sendSum;
    Currency currency;
    LocalDateTime sendDateTime;
    LocalDateTime updateTime;
    String paymentPurpose;
    PaymentStatus paymentStatus;

    public BankOperation(int id, String sendCardNumber, String getCardNumber, Float sendSum, Currency currency, String paymentPurpose) {
        this.id = ++id;
        this.sendCardNumber = sendCardNumber;
        this.getCardNumber = getCardNumber;
        this.sendSum = sendSum;
        this.currency = currency;
        this.sendDateTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.paymentPurpose = paymentPurpose;
        this.paymentStatus = PaymentStatus.IN_PROCESSING;
    }
}