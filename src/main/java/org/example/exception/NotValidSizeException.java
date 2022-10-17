package org.example.exception;

public class NotValidSizeException extends RuntimeException {
    public NotValidSizeException() {
        super("Your string length is not valid");
    }
}