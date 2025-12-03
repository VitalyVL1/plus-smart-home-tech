package ru.practicum.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.dto.exception.ErrorResponse;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseExceptionHandler {

    @ExceptionHandler(ServiceTemporaryUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleServiceTemporaryUnavailable(
            MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        String userMessage = "Service temporary unavailable: " + String.join(", ", errors);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .cause(ex.getCause())
                .stackTrace(List.of(ex.getStackTrace()))
                .httpStatus(HttpStatus.SERVICE_UNAVAILABLE.name())
                .userMessage(userMessage)
                .message("Request validation failed")
                .suppressed(List.of(ex.getSuppressed()))
                .localizedMessage(ex.getLocalizedMessage())
                .build();

        log.warn("Validation error: {}", userMessage, ex);
        return errorResponse;
    }

    // Обработка MethodArgumentNotValidException (валидация @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        String userMessage = "Validation failed: " + String.join(", ", errors);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .cause(ex.getCause())
                .stackTrace(List.of(ex.getStackTrace()))
                .httpStatus(HttpStatus.BAD_REQUEST.name())
                .userMessage(userMessage)
                .message("Request validation failed")
                .suppressed(List.of(ex.getSuppressed()))
                .localizedMessage(ex.getLocalizedMessage())
                .build();

        log.warn("Validation error: {}", userMessage, ex);
        return errorResponse;
    }

    // Обработка ConstraintViolationException (валидация @Validated на параметрах метода)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(
            ConstraintViolationException ex) {

        List<String> errors = ex.getConstraintViolations().stream()
                .map(violation -> String.format("%s: %s",
                        violation.getPropertyPath(),
                        violation.getMessage()))
                .collect(Collectors.toList());

        String userMessage = "Constraint violation: " + String.join(", ", errors);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .cause(ex.getCause())
                .stackTrace(List.of(ex.getStackTrace()))
                .httpStatus(HttpStatus.BAD_REQUEST.name())
                .userMessage(userMessage)
                .message("Constraint violation")
                .suppressed(List.of(ex.getSuppressed()))
                .localizedMessage(ex.getLocalizedMessage())
                .build();

        log.warn("Constraint violation: {}", userMessage, ex);
        return errorResponse;
    }

    // Обработка исключений валидации Pageable
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(
            IllegalArgumentException ex) {

        String userMessage = ex.getMessage();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .cause(ex.getCause())
                .stackTrace(List.of(ex.getStackTrace()))
                .httpStatus("BAD_REQUEST")
                .userMessage(userMessage)
                .message("Invalid request parameter")
                .suppressed(List.of(ex.getSuppressed()))
                .localizedMessage(ex.getLocalizedMessage())
                .build();

        log.warn("Illegal argument: {}", userMessage, ex);
        return errorResponse;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleRuntimeException(RuntimeException ex) {

        String userMessage = ex.getMessage();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .cause(ex.getCause())
                .stackTrace(List.of(ex.getStackTrace()))
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .userMessage(userMessage)
                .message("Internal server error")
                .suppressed(List.of(ex.getSuppressed()))
                .localizedMessage(ex.getLocalizedMessage())
                .build();

        log.error("Internal server error: {}", userMessage, ex);
        return errorResponse;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception ex) {

        String userMessage = ex.getMessage();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .cause(ex.getCause())
                .stackTrace(List.of(ex.getStackTrace()))
                .httpStatus("INTERNAL_SERVER_ERROR")
                .userMessage(userMessage)
                .message("Internal server error")
                .suppressed(List.of(ex.getSuppressed()))
                .localizedMessage(ex.getLocalizedMessage())
                .build();

        log.error("Internal server error: {}", userMessage, ex);
        return errorResponse;
    }
}
