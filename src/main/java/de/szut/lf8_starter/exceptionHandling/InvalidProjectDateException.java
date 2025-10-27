package de.szut.lf8_starter.exceptionHandling;

public class InvalidProjectDateException extends RuntimeException {
    public InvalidProjectDateException(String message) {
        super(message);
    }
}
