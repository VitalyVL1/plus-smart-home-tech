package ru.practicum.exception;

public class ServiceTemporaryUnavailableException extends RuntimeException {
    public ServiceTemporaryUnavailableException(String message) {
        super(message);
    }
}
