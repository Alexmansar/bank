package org.example;

import org.example.bank.BankController;

public class BankMain {
    public static void main(String[] args) {
        BankController controller = new BankController();
        controller.createNewOperation();
        controller.printTransaction();
    }
}