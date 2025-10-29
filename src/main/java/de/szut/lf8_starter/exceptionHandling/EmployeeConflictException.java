package de.szut.lf8_starter.exceptionHandling;

public class EmployeeConflictException extends RuntimeException {
    public EmployeeConflictException(String message) {
        super(message);
    }
}
