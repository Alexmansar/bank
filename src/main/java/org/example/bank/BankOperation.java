package org.example.bank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankOperation {
    String sendCardNumber;
    String getCardNumber;
    Float sendSum;
    Currency currency;
    LocalDateTime sendDateTime;
    String paymentPurpose;
    PaymentStatus paymentStatus;


    public BankOperation(String sendCardNumber, String getCardNumber, Float sendSum, Currency currency, String paymentPurpose) {
        this.sendCardNumber = sendCardNumber;
        this.getCardNumber = getCardNumber;
        this.sendSum = sendSum;
        this.currency = currency;
        this.sendDateTime = LocalDateTime.now();
        this.paymentPurpose = paymentPurpose;
        this.paymentStatus = PaymentStatus.IN_PROCESSING;
    }

    @Override
    public String toString() {
        return "BankOperation{" +
                "sendCardNumber='" + sendCardNumber + '\'' +
                ", getCardNumber='" + getCardNumber + '\'' +
                ", sendSum=" + sendSum +
                ", currency=" + currency +
                ", sendDateTime=" + sendDateTime +
                ", paymentPurpose='" + paymentPurpose + '\'' +
                ", paymentStatus=" + paymentStatus +
                '}';
    }
}