package org.example.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ConsoleUtils {
    public static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));

    public static String getString() {
        try {
            return READER.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getInt() {
        int number;
        try {
            number = Integer.parseInt(getString());
        } catch (NumberFormatException e) {
            System.out.println("try again " + e.getMessage());
            return getInt();
        }
        return number;
    }

    public static float getFloat() {
        float number;
        try {
            number = Float.parseFloat(getString());
        } catch (NumberFormatException e) {
            System.out.println("try again " + e.getMessage());
            return getInt();
        }
        return number;
    }
}