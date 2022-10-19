package org.example.exception;

public class NotFileFormatException extends RuntimeException {
    public NotFileFormatException(String message) {
        super(message);
    }
}