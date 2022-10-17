package org.example.model;

import lombok.extern.slf4j.Slf4j;
import org.example.utils.Validator;

import java.util.Arrays;
import java.util.Locale;

@Slf4j
public enum Currency {
    UAH, USD, EUR, GBP, JPY, CNY;

    public static void printCurrencies() {
        Arrays.stream(Currency.values()).forEach(System.out::println);
    }

    public static Currency validateCurrency() {
        Currency currency;
        try {
            currency = Currency.valueOf(Validator.validateInputText().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            System.out.println("Enter from the list : ");
            printCurrencies();
            log.error(exception.getMessage());
            return validateCurrency();
        }
        return currency;
    }

    public static Currency chooseCurrency() {
        Currency.printCurrencies();
        return Currency.validateCurrency();
    }
}