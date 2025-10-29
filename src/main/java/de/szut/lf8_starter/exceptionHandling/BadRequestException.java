package de.szut.lf8_starter.exceptionHandling;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
