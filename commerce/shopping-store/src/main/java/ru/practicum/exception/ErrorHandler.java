package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.dto.exception.ErrorResponse;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class ErrorHandler extends BaseExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(ProductNotFoundException e) {
        log.warn("Exception product not found: {}", e.getMessage());
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(List.of(e.getStackTrace()))
                .httpStatus(HttpStatus.NOT_FOUND.name())
                .userMessage(e.getMessage())
                .message("Product not found")
                .suppressed(List.of(e.getSuppressed()))
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }
}
