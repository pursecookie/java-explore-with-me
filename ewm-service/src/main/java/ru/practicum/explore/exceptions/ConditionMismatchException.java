package ru.practicum.explore.exceptions;

public class ConditionMismatchException extends RuntimeException {
    public ConditionMismatchException(String message) {
        super(message);
    }
}