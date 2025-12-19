package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FallBackUtility {
    public static void fastFallBack(Throwable cause) {
        if (cause instanceof ResourceNotFoundException) {
            log.warn("Not found (404): ", cause);
            throw (ResourceNotFoundException) cause;
        }

        if (cause instanceof BadRequestException) {
            log.warn("Bad request (4xx): ", cause);
            throw (BadRequestException) cause;
        }

        log.error("Server/network error ", cause);
        throw new ServiceTemporaryUnavailableException(cause.getMessage());
    }
}

