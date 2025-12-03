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
    @ExceptionHandler(NotAuthorizedUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleNotAuthorizedUserException(NotAuthorizedUserException e) {
        log.warn("Unauthorized user: {}", e.getMessage());
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(List.of(e.getStackTrace()))
                .httpStatus(HttpStatus.UNAUTHORIZED.name())
                .userMessage(e.getMessage())
                .message("Unauthorized user")
                .suppressed(List.of(e.getSuppressed()))
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }

    @ExceptionHandler(NoProductsInShoppingCartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoProductsInShoppingCartException(NoProductsInShoppingCartException e) {
        log.warn("No products in shopping cart: {}", e.getMessage());
        return ErrorResponse.builder()
                .cause(e.getCause())
                .stackTrace(List.of(e.getStackTrace()))
                .httpStatus(HttpStatus.BAD_REQUEST.name())
                .userMessage(e.getMessage())
                .message("No products in shopping cart")
                .suppressed(List.of(e.getSuppressed()))
                .localizedMessage(e.getLocalizedMessage())
                .build();
    }
}
