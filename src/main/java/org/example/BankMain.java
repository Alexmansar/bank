package org.example;

import org.example.controller.BankController;

import java.io.IOException;

public class BankMain {
    public static void main(String[] args) throws IOException {
        BankController controller = new BankController();
        controller.run();
    }
}