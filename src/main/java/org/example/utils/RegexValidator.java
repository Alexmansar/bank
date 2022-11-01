package org.example.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexValidator {
    public static final String REGEX_CARD_NUMBER = "(\\d{4}[-\\s]){3}\\d{4}";

    public static boolean pattern(String message) {
        Pattern pattern = Pattern.compile(REGEX_CARD_NUMBER);
        Matcher matcher = pattern.matcher(message);
        return matcher.matches();
    }
}